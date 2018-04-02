package com.jalgoarena.judge

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.google.common.io.Resources
import com.jalgoarena.config.TestApplicationConfiguration
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Function
import com.jalgoarena.domain.Submission
import com.jalgoarena.domain.Problem
import com.jalgoarena.domain.StatusCode
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.rules.SpringClassRule
import org.springframework.test.context.junit4.rules.SpringMethodRule
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@RunWith(JUnitParamsRunner::class)
@ContextConfiguration(classes = [TestApplicationConfiguration::class])
open class JavaEngineIntegrationTest {

    companion object {

        private val client = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build()

        @ClassRule
        @JvmField val SCR = SpringClassRule()

        @BeforeClass
        @JvmStatic fun setUp() {

            fun ping(url: String): Response {
                val apiServiceRequest = Request.Builder()
                        .url(url)
                        .build()
                return client.newCall(apiServiceRequest).execute()
            }

            val response = ping("https://jalgoarena-api.herokuapp.com/health")
            assertThat(response.isSuccessful).isTrue()
            val response2 = ping("https://jalgoarena-problems.herokuapp.com/health")
            assertThat(response2.isSuccessful).isTrue()
        }
    }

    @Rule
    @JvmField val springMethodRule = SpringMethodRule()

    @Inject
    lateinit var repository: ProblemsRepository

    @Inject
    lateinit var judgeEngine: JvmJudgeEngine

    private fun judgeSolution(problemId: String, solutionId: String, statusCode: StatusCode) {
        try {
            val problem = repository.find(problemId)
            val sourceCode = Resources.toString(Resources.getResource("$solutionId.java"), Charsets.UTF_8)

            val result = judgeEngine.judge(problem, Submission(sourceCode, "0-0", "java", "0", problemId, null))

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
    fun infiniteLoop() {
        val executor = Executors.newCachedThreadPool()
        val futures = mutableListOf<Future<*>>()

        (1..20).forEach {

            val future = executor.submit {
                try {
                    val problem = PROBLEM
                    val sourceCode = Resources.toString(Resources.getResource("InfiniteLoop.java"), Charsets.UTF_8)
                    val result = judgeEngine.judge(problem, Submission(sourceCode, "0-0", "java", "0", "InfiniteLoop", null))

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
            skeletonCode = mapOf(
                    Pair("java", "dummy code"),
                    Pair("kotlin", "kotlin dummy code")
            ),
            testCases = listOf(
                    Problem.TestCase(
                            ArrayNode(JsonNodeFactory.instance).add("a").add("c").add(
                                    ArrayNode(JsonNodeFactory.instance).add("a").add("b").add("c")),
                            IntNode(2)
                    )
            )
    )
}
