package com.jalgoarena.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.io.Resources
import com.jalgoarena.domain.Problem
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.util.ArrayList
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

@Repository
open class JsonProblemsRepository(
        @Value("\${jalgoarena.problems.source:problems.json}")
        private val jalgoarenaProblemsSource: String = "problems.json"
) : ProblemsRepository {

    private val problems: CopyOnWriteArrayList<Problem> = CopyOnWriteArrayList()
    private val problemsById: ConcurrentHashMap<String, Problem> = ConcurrentHashMap()

    override fun find(id: String): Problem? {
        if (problemsById.isEmpty()) {
            init(jalgoarenaProblemsSource)
        }

        return problemsById[id]
    }

    override fun findAll(): List<Problem> {
        if (problems.isEmpty()) {
            init(jalgoarenaProblemsSource)
        }

        return problems
    }

    private fun init(jalgoarenaProblemsSource: String) {
        val problems: List<Problem> = ArrayList(jacksonObjectMapper().readValue(
                Resources.toString(Resources.getResource(jalgoarenaProblemsSource), Charsets.UTF_8),
                Array<Problem>::class.java
        ).asList())

        this.problems.addAllAbsent(problems)
        this.problemsById.putAll(problems.map {it.id to it})
    }
}