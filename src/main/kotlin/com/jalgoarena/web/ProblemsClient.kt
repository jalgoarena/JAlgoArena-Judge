package com.jalgoarena.web

import com.jalgoarena.data.DataRepository
import com.jalgoarena.domain.Problem
import com.netflix.discovery.EurekaClient
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import javax.inject.Inject

@Service
open class ProblemsClient(
        @Inject private val discoveryClient: EurekaClient,
        @Inject private val restTemplate: RestOperations
) : DataRepository<Problem> {

    private fun problemsServiceUrl(): String =
            discoveryClient.getNextServerFromEureka("jalgoarena-problems", false).homePageUrl

    @Cacheable("problem")
    override fun find(id: String): Problem = restTemplate.getForObject(
            "${problemsServiceUrl()}/problems/$id", Problem::class.java
    )

    @Cacheable("problems")
    override fun findAll(): Array<Problem> = restTemplate.getForObject(
            "${problemsServiceUrl()}/problems", Array<Problem>::class.java
    )
}
