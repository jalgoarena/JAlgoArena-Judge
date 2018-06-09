package com.jalgoarena.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.io.Resources
import com.jalgoarena.domain.Problem
import org.springframework.stereotype.Repository
import java.util.ArrayList

@Repository
open class JsonProblemsRepository : ProblemsRepository {

    override fun find(id: String): Problem? {
        return db[id]
    }

    override fun findAll(): List<Problem> {
        return problems
    }

    companion object {
        private val problems: List<Problem> = ArrayList(jacksonObjectMapper().readValue(
                Resources.toString(Resources.getResource("problems.json"), Charsets.UTF_8),
                Array<Problem>::class.java
        ).asList())

        private val db: MutableMap<String, Problem> = mutableMapOf()

        init {
            problems.forEach { problem -> db[problem.id] = problem }
        }
    }
}