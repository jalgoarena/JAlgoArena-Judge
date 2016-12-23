package org.algohub.engine.judge

import com.google.common.base.Charsets
import com.google.common.io.Resources
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.algohub.engine.data.ProblemsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class JavaEngineIntegrationTest {

    val repository = ProblemsRepository()
    val judgeEngine = JudgeEngine()

    private fun judgeSolution(problemId: String, solutionId: String, statusCode: StatusCode) {
        try {
            val problem = repository.find(problemId)!!
            val sourceCode = Resources.toString(Resources.getResource("$solutionId.java"), Charsets.UTF_8)

            val result = judgeEngine.judge(problem, sourceCode)

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
}
