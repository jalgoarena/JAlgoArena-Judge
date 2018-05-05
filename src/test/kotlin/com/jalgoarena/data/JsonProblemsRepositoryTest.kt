package com.jalgoarena.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class JsonProblemsRepositoryTest {

    private lateinit var problemsRepository: ProblemsRepository

    @Before
    fun setUp() {
        problemsRepository = JsonProblemsRepository()
    }

    @Test
    fun loads_all_problems() {
        assertThat(problemsRepository.findAll().size).isGreaterThan(85)
    }

    @Test
    fun loads_fib_problem() {
        assertThat(problemsRepository.find("fib")!!.title).isEqualTo("Fibonacci")
    }
}