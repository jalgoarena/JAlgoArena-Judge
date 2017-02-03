package com.jalgoarena.web

import com.jalgoarena.data.SubmissionsRepository
import com.jalgoarena.domain.Submission
import com.netflix.discovery.EurekaClient
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.client.RestOperations
import javax.inject.Inject

@Service
class SubmissionsClient(
        @Inject private val discoveryClient: EurekaClient,
        @Inject private val restTemplate: RestOperations
) : SubmissionsRepository {

    private fun submissionsServiceUrl(): String =
            discoveryClient.getNextServerFromEureka("jalgoarena-submissions", false).homePageUrl

    override fun save(
            @RequestBody submission: Submission,
            @RequestHeader("X-Authorization", required = false) token: String?
    ): Submission {
        val headers = HttpHeaders().apply {
            set("X-Authorization", token)
        }

        val requestEntity = HttpEntity<Submission>(submission, headers)

        return restTemplate.exchange(
                "${submissionsServiceUrl()}/submissions",
                HttpMethod.PUT,
                requestEntity,
                Submission::class.java
        ).body
    }
}
