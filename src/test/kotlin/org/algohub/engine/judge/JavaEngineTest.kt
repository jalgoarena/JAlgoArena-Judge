package org.algohub.engine.judge

import com.google.common.base.Charsets
import com.google.common.io.Resources
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.algohub.engine.ObjectMapperInstance
import org.junit.Test
import org.junit.runner.RunWith

import java.io.IOException

import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.fail

@RunWith(JUnitParamsRunner::class)
class JavaEngineTest {

    private fun judgeSolution(problemId: String, solutionId: String, statusCode: StatusCode) {
        try {
            val questionStr = Resources.toString(Resources.getResource("$problemId.json"), Charsets.UTF_8)
            val problem = ObjectMapperInstance.INSTANCE.readValue(questionStr, Problem::class.java)
            val sourceCode = Resources.toString(Resources.getResource("$solutionId.java"), Charsets.UTF_8)

            val result = JudgeEngine.judge(problem, sourceCode)

            assertThat(result.statusCode).isEqualTo(statusCode.toString())
        } catch (e: IOException) {
            fail(e.message)
        }

    }

    @Test
    @Parameters("2-sum, TwoSum", "fib, FibFast", "stoi, MyStoi", "word-ladder, WordLadder")
    @Throws(Exception::class)
    fun acceptsCorrectSolution(problemId: String, solutionId: String) {
        judgeSolution(problemId, solutionId, StatusCode.ACCEPTED)
    }

    @Test
    @Throws(Exception::class)
    fun failsWithCompilationErrorWhenSourceCodeDoesNotCompile() {
        judgeSolution("fib", "FibBroken", StatusCode.COMPILE_ERROR)
    }

    @Test
    @Throws(Exception::class)
    fun returnsWrongAnswerForIncorrectSolution() {
        judgeSolution("fib", "FibWrongAnswer", StatusCode.WRONG_ANSWER)
    }
}
