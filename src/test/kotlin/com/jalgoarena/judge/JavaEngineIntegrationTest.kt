package com.jalgoarena.judge

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.google.common.io.Resources
import com.jalgoarena.SandboxSecurityManger
import com.jalgoarena.config.TestApplicationConfiguration
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Function
import com.jalgoarena.domain.Problem
import com.jalgoarena.domain.StatusCode
import com.jalgoarena.domain.Submission
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.ClassRule
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.rules.SpringClassRule
import org.springframework.test.context.junit4.rules.SpringMethodRule
import java.time.LocalDateTime
import java.util.concurrent.Executors
import java.util.concurrent.Future

@RunWith(JUnitParamsRunner::class)
@ContextConfiguration(classes = [TestApplicationConfiguration::class])
open class JavaEngineIntegrationTest {

    companion object {
        @ClassRule
        @JvmField val SCR = SpringClassRule()
        private val WORD_LADDER_FUNCTION = Function("ladderLength",
                Function.Return("java.lang.Integer", "The shortest length"),
                listOf(Function.Parameter("begin_word", "java.lang.String", "the begin word"),
                        Function.Parameter("end_word", "java.lang.String", "the end word"),
                        Function.Parameter("dict", "java.util.HashSet", "the dictionary", "String")
                )
        )
        private val PROBLEM = Problem(
                id = "dummy_id",
                title = "dummy_title",
                description = "dummy description",
                level = 3,
                timeLimit = 5,
                func = WORD_LADDER_FUNCTION,
                skeletonCode = "dummy code",
                testCases = listOf(
                        Problem.TestCase(
                                ArrayNode(JsonNodeFactory.instance).add("a").add("c").add(
                                        ArrayNode(JsonNodeFactory.instance).add("a").add("b").add("c")),
                                IntNode(2)
                        )
                )
        )
    }

    @Rule
    @JvmField val springMethodRule = SpringMethodRule()

    @Autowired
    private lateinit var repository: ProblemsRepository

    @Autowired
    private lateinit var judgeEngine: JvmJudgeEngine

    private fun judgeSolution(problemId: String, solutionId: String, statusCode: StatusCode) {
        try {
            val problem = repository.find(problemId)!!
            val sourceCode = Resources.toString(Resources.getResource("$solutionId.java"), Charsets.UTF_8)

            val result = judgeEngine.judge(problem, Submission(sourceCode, "0-0", "0", problemId, LocalDateTime.now(), 0, "dummy_token"))

            assertThat(result.statusCode).isEqualTo(statusCode.toString())
        } catch (e: Exception) {
            fail(e.message)
        }
    }

    @Test
    @Parameters("2-sum, TwoSum", "fib, FibFast", "stoi, MyStoi", "word-ladder, WordLadder")
    fun acceptsCorrectSolution(problemId: String, solutionId: String) {
        judgeSolution(problemId, solutionId, StatusCode.ACCEPTED)
    }

    @Test
    fun failsWithCompilationErrorWhenSourceCodeDoesNotCompile() {
        judgeSolution("fib", "FibBroken", StatusCode.COMPILE_ERROR)
    }

    @Test
    fun returnsWrongAnswerForIncorrectSolution() {
        judgeSolution("fib", "FibWrongAnswer", StatusCode.WRONG_ANSWER)
    }

    @Test
    fun returnsRuntimeExceptionForSystemExit() {
        System.setSecurityManager(SandboxSecurityManger())
        judgeSolution("fib", "FibSystemExit", StatusCode.RUNTIME_ERROR)
    }

    @Test
    @Ignore
    fun infiniteLoop() {
        val executor = Executors.newCachedThreadPool()
        val futures = mutableListOf<Future<*>>()

        (1..20).forEach {

            val future = executor.submit {
                try {
                    val problem = PROBLEM
                    val sourceCode = Resources.toString(Resources.getResource("InfiniteLoop.java"), Charsets.UTF_8)
                    val result = judgeEngine.judge(problem, Submission(sourceCode, "0-0", "0", "InfiniteLoop", LocalDateTime.now(), 0, "dummy_token"))

                    assertThat(result.statusCode).isEqualTo(StatusCode.TIME_LIMIT_EXCEEDED.toString())
                } catch (e: Exception) {
                    fail(e.message)
                }
            }

            futures.add(future)
        }

        futures.forEach {
            it.get()
        }

        executor.shutdown()
    }

}
