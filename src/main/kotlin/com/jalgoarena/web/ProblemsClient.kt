package com.jalgoarena.web

import com.jalgoarena.data.DataRepository
import com.jalgoarena.domain.Problem
import com.netflix.discovery.EurekaClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import javax.inject.Inject

@Service
class ProblemsClient(
        @Inject private val discoveryClient: EurekaClient,
        @Inject private val restTemplate: RestOperations
) : DataRepository<Problem> {

    private val LOG = LoggerFactory.getLogger(this.javaClass)

    private fun problemsServiceUrl(): String =
            discoveryClient.getNextServerFromEureka("jalgoarena-problems", false).homePageUrl

    override fun find(id: String): Problem? {
        val problem = restTemplate.getForObject(
                "${problemsServiceUrl()}/problems/$id", Problem::class.java
        )

        if (problem == null) {
            LOG.error("There is error in querying for $id problem. Response: $problem")
            return null
        }

        return problem
    }

    override fun findAll(): Array<Problem> {
        return restTemplate.getForObject(
                "${problemsServiceUrl()}/problems", Array<Problem>::class.java
        )
    }
}
