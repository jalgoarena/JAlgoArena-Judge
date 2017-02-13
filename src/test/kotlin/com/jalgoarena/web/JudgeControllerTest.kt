package com.jalgoarena.web

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.data.SubmissionsRepository
import com.jalgoarena.domain.*
import com.jalgoarena.domain.Function
import com.jalgoarena.judge.JvmJudgeEngine
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mockito.mock
import org.mockito.internal.verification.Only

class JudgeControllerTest {

    private val problemsRepository = mock(ProblemsRepository::class.java)
    private val submissionsRepository = mock(SubmissionsRepository::class.java)
    private val judgeEngine = mock(JvmJudgeEngine::class.java)

    private val controller: JudgeController = JudgeController(
            problemsRepository,
            submissionsRepository,
            judgeEngine
    )

    @Test
    fun sends_submission_when_judge_was_successful() {
        given(problemsRepository.find(FIB_PROBLEM.id)).willReturn(
                FIB_PROBLEM
        )

        given(judgeEngine.judge(FIB_PROBLEM, JudgeRequest(DUMMY_SOURCE_CODE, "0-0", "java"))).willReturn(
                JudgeResult.Accepted(NUMBER_OF_TEST_CASES, ELAPSED_TIME, USED_MEMORY)
        )

        controller.judge(
                FIB_PROBLEM.id,
                JudgeRequest(DUMMY_SOURCE_CODE, USER_ID, LANGUAGE)
        )

        then(submissionsRepository).should(Only()).save(SUBMISSION, null)
    }

    private val DUMMY_SOURCE_CODE = "dummy source code"
    private val ELAPSED_TIME = 0.01
    private val NUMBER_OF_TEST_CASES = 5
    private val USED_MEMORY = 0L
    private val LANGUAGE = "java"
    private val USER_ID = "0-0"

    private val FIB_FUNCTION = Function("fib",
            Function.Return("java.lang.Integer",
                    "Fibonacci number"),
            listOf(Function.Parameter("input", "java.lang.Integer", "Input"))
    )

    private val FIB_PROBLEM = Problem(
            id = "fib",
            title = "Fibonacci",
            description = "dummy description",
            level = 1,
            timeLimit = 1,
            func = FIB_FUNCTION,
            skeletonCode = mapOf(
                    Pair("java", "dummy code"),
                    Pair("kotln", "kotlin dummy code")
            ),
            testCases = listOf(
                    Problem.TestCase(
                            ArrayNode(JsonNodeFactory.instance).add(1).add(2),
                            IntNode(3)
                    )
            )
    )

    private val SUBMISSION = Submission(
            problemId = FIB_PROBLEM.id,
            elapsedTime = ELAPSED_TIME,
            language = LANGUAGE,
            sourceCode = DUMMY_SOURCE_CODE,
            statusCode = StatusCode.ACCEPTED.toString(),
            userId = USER_ID
    )
}
