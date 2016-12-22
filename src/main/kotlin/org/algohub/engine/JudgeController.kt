package org.algohub.engine

import okhttp3.OkHttpClient
import okhttp3.Request
import org.algohub.engine.codegeneration.JavaCodeGenerator
import org.algohub.engine.codegeneration.KotlinCodeGenerator
import org.algohub.engine.judge.Function
import org.algohub.engine.judge.JudgeEngine
import org.algohub.engine.judge.JudgeResult
import org.algohub.engine.judge.Problem
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collectors

@CrossOrigin
@RestController
class JudgeController {

    @RequestMapping(path = arrayOf("/problems/{id}/submit"), method = arrayOf(RequestMethod.POST), produces = arrayOf("application/json"))
    fun judge(@PathVariable id: String, @RequestBody sourceCode: String): JudgeResult {
        val problem = requestProblem(id) ?: return JudgeResult.runtimeError("Wrong problem id: " + id)
        return JudgeEngine.judge(problem, sourceCode)
    }

    @RequestMapping(path = arrayOf("/problems"), method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    fun problems(): List<Problem> {
        return Arrays.stream(requestProblems())
                .map { x -> x.problemWithoutFunctionAndTestCases(
                        sourceCodeOf(x.function!!), kotlinSourceCodeOf(x.function)
                )}
                .collect(Collectors.toList<Problem>())
    }

    private fun requestProblems(): Array<Problem> {
        val request = Request.Builder()
                .url(DATA_SERVICE_HOST + "problems")
                .build()

        val response = CLIENT.newCall(request).execute()
        val problemsAsJsonArray = response.body().string()

        return ObjectMapperInstance.INSTANCE.readValue(problemsAsJsonArray, Array<Problem>::class.java)
    }

    @RequestMapping(path = arrayOf("/problems/{id}"), method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    fun problem(@PathVariable id: String): Problem {

        val problem = requestProblem(id) ?: throw IllegalArgumentException("Invalid problem id: " + id)

        return problem.problemWithoutFunctionAndTestCases(
                sourceCodeOf(problem.function!!), kotlinSourceCodeOf(problem.function)
        )
    }

    internal fun requestProblem(problemId: String): Problem? {
        val request = Request.Builder()
                .url("${DATA_SERVICE_HOST}problems/$problemId")
                .build()

        val response = CLIENT.newCall(request).execute()
        val problemAsJson = response.body().string()
        return ObjectMapperInstance.INSTANCE.readValue(problemAsJson, Problem::class.java)
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(JudgeController::class.java)

        private val DATA_SERVICE_HOST = "https://jalgoarena-data.herokuapp.com/"

        private val CLIENT = OkHttpClient()

        private fun sourceCodeOf(function: Function): String {
            try {
                return JavaCodeGenerator.generateEmptyFunction(function)
            } catch (e: ClassNotFoundException) {
                LOG.error(e.message, e)
                throw IllegalArgumentException("Illegal type: " + e.message)
            }
        }

        private fun kotlinSourceCodeOf(function: Function): String {
            try {
                return KotlinCodeGenerator.generateEmptyFunction(function)
            } catch (e: ClassNotFoundException) {
                LOG.error(e.message, e)
                throw IllegalArgumentException("Illegal type: " + e.message)
            }
        }
    }
}
