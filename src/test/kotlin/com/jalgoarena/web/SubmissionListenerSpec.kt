package com.jalgoarena.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.ApplicationConfiguration
import com.jalgoarena.config.TestApplicationConfiguration
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Problem
import com.jalgoarena.domain.StatusCode
import com.jalgoarena.domain.Submission
import com.jalgoarena.domain.SubmissionResult
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.IntegerSerializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import org.springframework.kafka.listener.MessageListener
import org.springframework.kafka.listener.config.ContainerProperties
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.rule.KafkaEmbedded
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit


@RunWith(SpringRunner::class)
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = ["submissions", "results"])
@ContextConfiguration(classes = [TestApplicationConfiguration::class])
class SubmissionListenerSpec {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var embeddedKafka: KafkaEmbedded

    private lateinit var submissionTemplate: KafkaTemplate<Int, String>
    private lateinit var submissionResultsRecords: LinkedBlockingQueue<ConsumerRecord<Int, String>>

    @Before
    fun setUp() {
        val props = KafkaTestUtils.producerProps(embeddedKafka)
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = IntegerSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java

        val producerFactoryForSubmissionResult = DefaultKafkaProducerFactory<Int, SubmissionResult>(props)

        val resultTopicTemplate = KafkaTemplate(producerFactoryForSubmissionResult)
        resultTopicTemplate.defaultTopic = RESULTS_TOPIC

        submissionsListener.template = resultTopicTemplate

        setupSubmissionContainer()
        submissionResultsRecords = setupSubmissionResultsContainer()

        val senderProps = KafkaTestUtils.senderProps(embeddedKafka.brokersAsString)
        val producerFactory = DefaultKafkaProducerFactory<Int, String>(senderProps)

        submissionTemplate = KafkaTemplate(producerFactory)
        submissionTemplate.defaultTopic = SUBMISSIONS_TOPIC
    }

    @Test
    fun judge_different_submissions() {
        sendMessage(SUBMISSION_FIB)
        assertThat(receiveMessage().statusCode)
                .isEqualTo(StatusCode.ACCEPTED.toString())

        sendMessage(SUBMISSION_FIB_INCORRECT)
        assertThat(receiveMessage().statusCode)
                .isEqualTo(StatusCode.WRONG_ANSWER.toString())

        sendMessage(SUBMISSION_FIB_NOT_COMPILING)
        assertThat(receiveMessage().statusCode)
                .isEqualTo(StatusCode.COMPILE_ERROR.toString())

        sendMessage(SUBMISSION_FIB_RUNTIME_ERROR)
        assertThat(receiveMessage().statusCode)
                .isEqualTo(StatusCode.RUNTIME_ERROR.toString())
    }

    private fun receiveMessage(): SubmissionResult {
        val consumerRecord = submissionResultsRecords.poll(10, TimeUnit.SECONDS)

        logger.info("Received record: {}", consumerRecord)

        return jacksonObjectMapper()
                .readValue(consumerRecord.value(), SubmissionResult::class.java)
    }

    private fun sendMessage(submission: Submission) {
        val submissionJson = OBJECT_MAPPER.writeValueAsString(submission)
        submissionTemplate.sendDefault(submissionJson)
        logger.info("Submission is sent")
    }

    private fun setupSubmissionResultsContainer(): LinkedBlockingQueue<ConsumerRecord<Int, String>> {
        val consumerProperties = KafkaTestUtils.consumerProps("submissionResultsConsumer", "false", embeddedKafka)
        val consumerFactory = DefaultKafkaConsumerFactory<Int, String>(consumerProperties)
        val containerProperties = ContainerProperties(RESULTS_TOPIC)
        val container = KafkaMessageListenerContainer<Int, String>(consumerFactory, containerProperties)
        val records = LinkedBlockingQueue<ConsumerRecord<Int, String>>()
        container.setupMessageListener(SubmissionResultMessageListener(records))
        container.start()
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.partitionsPerTopic)

        logger.info("Container for submissions results started")
        return records
    }

    private fun setupSubmissionContainer() {
        val consumerProperties = KafkaTestUtils.consumerProps("submissionConsumer", "false", embeddedKafka)
        val consumerFactory = DefaultKafkaConsumerFactory<Int, String>(consumerProperties)
        val containerProperties = ContainerProperties(SUBMISSIONS_TOPIC)
        val container = KafkaMessageListenerContainer<Int, String>(consumerFactory, containerProperties)

        container.setupMessageListener(SubmissionMessageListener())
        container.start()
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.partitionsPerTopic)

        logger.info("Container for submissions started")
    }

    class SubmissionResultMessageListener(
            private val records: BlockingQueue<ConsumerRecord<Int, String>>
    ): MessageListener<Int, String> {
        override fun onMessage(record: ConsumerRecord<Int, String>?) {
            println(record)
            records.add(record)
        }

    }

    class SubmissionMessageListener : MessageListener<Int, String> {
        override fun onMessage(record: ConsumerRecord<Int, String>) {
            println(record)
            submissionsListener.judge(record.value())
        }

    }

    class ProblemsOnlyFibRepo : ProblemsRepository {

        private val objectMapper = jacksonObjectMapper()

        override fun find(id: String): Problem {
            val problemAsJson = "{\"id\":\"fib\",\"title\":\"Fibonacci\",\"description\":\"Write the `fib` method to return the N'th term.\\r\\nWe start counting from:\\r\\n* fib(0) = 0\\r\\n* fib(1) = 1.\\r\\n\\r\\n### Examples\\r\\n\\r\\n* `0` -> `0`\\r\\n* `6` -> `8`\",\"timeLimit\":5,\"func\":{\"name\":\"fib\",\"returnStatement\":{\"type\":\"java.lang.Long\",\"comment\":\" N'th term of Fibonacci sequence\"},\"parameters\":[{\"name\":\"n\",\"type\":\"java.lang.Integer\",\"comment\":\"id of fibonacci term to be returned\"}]},\"testCases\":[{\"input\":[\"0\"],\"output\":0},{\"input\":[\"1\"],\"output\":1},{\"input\":[\"2\"],\"output\":1},{\"input\":[\"3\"],\"output\":2},{\"input\":[\"4\"],\"output\":3},{\"input\":[\"5\"],\"output\":5},{\"input\":[\"6\"],\"output\":8},{\"input\":[\"20\"],\"output\":6765},{\"input\":[\"40\"],\"output\":102334155}],\"level\":1}"

            return objectMapper.readValue(problemAsJson, Problem::class.java)
        }

        override fun findAll(): List<Problem> {
            val problemsAsJsonArray = "[{\"id\":\"fib\",\"title\":\"Fibonacci\",\"description\":\"Write the `fib` method to return the N'th term.\\r\\nWe start counting from:\\r\\n* fib(0) = 0\\r\\n* fib(1) = 1.\\r\\n\\r\\n### Examples\\r\\n\\r\\n* `0` -> `0`\\r\\n* `6` -> `8`\",\"timeLimit\":5,\"func\":{\"name\":\"fib\",\"returnStatement\":{\"type\":\"java.lang.Long\",\"comment\":\" N'th term of Fibonacci sequence\"},\"parameters\":[{\"name\":\"n\",\"type\":\"java.lang.Integer\",\"comment\":\"id of fibonacci term to be returned\"}]},\"testCases\":[{\"input\":[\"0\"],\"output\":0},{\"input\":[\"1\"],\"output\":1},{\"input\":[\"2\"],\"output\":1},{\"input\":[\"3\"],\"output\":2},{\"input\":[\"4\"],\"output\":3},{\"input\":[\"5\"],\"output\":5},{\"input\":[\"6\"],\"output\":8},{\"input\":[\"20\"],\"output\":6765},{\"input\":[\"40\"],\"output\":102334155}],\"level\":1}]"

            return objectMapper.readValue(problemsAsJsonArray, Array<Problem>::class.java).asList()
        }
    }

    companion object {

        private val submissionsListener = SubmissionsListener(
                ProblemsOnlyFibRepo(),
                TestApplicationConfiguration().judgeEngine(ApplicationConfiguration().objectMapper())
        )

        private const val SUBMISSIONS_TOPIC = "submissions"
        private const val RESULTS_TOPIC = "results"

        private val OBJECT_MAPPER = ObjectMapper()

        private const val SOURCE_CODE_FIB = """public class Solution {
    public long fib(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;

        return fib(n - 1) + fib(n - 2);
    }
}"""

        private const val SOURCE_CODE_FIB_INCORRECT = """public class Solution {
    public long fib(int n) {
        if (n <= 0) return 1;
        if (n == 1) return 1;

        return fib(n - 1) + fib(n - 2);
    }
}"""

        private const val SOURCE_CODE_FIB_RUNTIME_ERROR = """public class Solution {
    public long fib(int n) {
        throw new RuntimeException("test");
    }
}"""

        private val SUBMISSION_FIB = Submission(
                SOURCE_CODE_FIB,
                "0-0",
                "java",
                "0-1-2-3",
                "fib",
                "dummy"
        )

        private val SUBMISSION_FIB_INCORRECT = Submission(
                SOURCE_CODE_FIB_INCORRECT,
                "0-0",
                "java",
                "0-1-2-3",
                "fib",
                "dummy"
        )

        private val SUBMISSION_FIB_NOT_COMPILING = Submission(
                "dummy",
                "0-0",
                "java",
                "0-1-2-3",
                "fib",
                "dummy"
        )

        private val SUBMISSION_FIB_RUNTIME_ERROR = Submission(
                SOURCE_CODE_FIB_RUNTIME_ERROR,
                "0-0",
                "java",
                "0-1-2-3",
                "fib",
                "dummy"
        )
    }
}