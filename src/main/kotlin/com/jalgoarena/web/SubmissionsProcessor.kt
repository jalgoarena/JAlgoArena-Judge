package com.jalgoarena.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Submission
import com.jalgoarena.domain.JudgeResult
import com.jalgoarena.domain.SubmissionResult
import com.jalgoarena.judge.JudgeEngine
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFutureCallback
import javax.inject.Inject

@Service
@KafkaListener(topics = ["submissions"])
class SubmissionsProcessor(
        @Inject private val problemsRepository: ProblemsRepository,
        @Inject private val judgeEngine: JudgeEngine
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var template: KafkaTemplate<Int, SubmissionResult>

    @KafkaHandler
    fun judge(message: String): SubmissionResult {

        val submission = jacksonObjectMapper().readValue(message, Submission::class.java)

        val judgeResult = judgeEngine.judge(
                problemsRepository.find(submission.problemId),
                submission
        )

        return submitAndReturnResult(submission, judgeResult)
    }

    private fun submitAndReturnResult(submission: Submission, judgeResult: JudgeResult): SubmissionResult {

        val submissionResult = SubmissionResult(
                problemId = submission.problemId,
                userId = submission.userId,
                statusCode = judgeResult.statusCode,
                sourceCode = submission.sourceCode,
                elapsedTime = judgeResult.elapsedTime,
                language = submission.language,
                submissionId = submission.submissionId,
                consumedMemory = judgeResult.consumedMemory,
                errorMessage = judgeResult.errorMessage,
                testcaseResults = judgeResult.testcaseResults
        )

        logger.info("Submit result for submission id: {}", submissionResult.submissionId)
        template.send("results", submissionResult)
                .addCallback(SubmissionResultHandler(submissionResult.submissionId))

        return submissionResult
    }

    class SubmissionResultHandler(
            private val submissionId: String
    ) : ListenableFutureCallback< SendResult<Int, SubmissionResult>> {

        private val logger = LoggerFactory.getLogger(this.javaClass)

        override fun onSuccess(result: SendResult<Int, SubmissionResult>?) {
            logger.info("Published submission result [submissionId={}]", submissionId)
        }

        override fun onFailure(ex: Throwable?) {
            logger.error("Couldn't publish submission result [submissionId={}]", submissionId, ex)
        }

    }
}