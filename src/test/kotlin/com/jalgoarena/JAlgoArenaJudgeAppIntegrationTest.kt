package com.jalgoarena

import org.assertj.core.api.Assertions.assertThat
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.kafka.test.rule.KafkaEmbedded
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class JAlgoArenaJudgeAppIntegrationTest {

    @Inject
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun check_if_spring_configuration_works_properly() {
        val body = this.restTemplate.getForObject("/info", String::class.java)
        assertThat(body).isEqualTo("{}")
    }

    companion object {
        @ClassRule
        @JvmField
        var embeddedKafka = KafkaEmbedded(1, true, "results", "submissions")
    }
}
