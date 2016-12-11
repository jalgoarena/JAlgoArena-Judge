package org.algohub.engine

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import io.swagger.annotations.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.algohub.engine.judge.JudgeEngine
import org.algohub.engine.judge.JudgeResult
import org.algohub.engine.judge.Problem
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

import java.io.IOException
import java.util.Arrays
import java.util.function.Function
import java.util.stream.Collectors

@CrossOrigin
@RestController
internal class JudgeController {

    @ApiOperation(value = "judge", nickname = "judge")
    @ApiImplicitParams(ApiImplicitParam(name = "id", value = "Problem's id", required = true, dataType = "string", paramType = "path", defaultValue = "fib"))
    @ApiResponses(value = *arrayOf(ApiResponse(code = 200, message = "Success", response = JudgeResult::class), ApiResponse(code = 404, message = "Not Found"), ApiResponse(code = 500, message = "Failure")))
    @RequestMapping(path = arrayOf("/problems/{id}/submit"), method = arrayOf(RequestMethod.POST), produces = arrayOf("application/json"))
    @Throws(IOException::class)
    fun judge(@PathVariable id: String, @RequestBody sourceCode: String): JudgeResult {
        val problem = requestProblem(id) ?: return JudgeResult.runtimeError("Wrong problem id: " + id)

        return JudgeEngine.judge(problem, sourceCode)
    }

    @ApiOperation(value = "problems", nickname = "getProblems")
    @ApiResponses(value = *arrayOf(ApiResponse(code = 200, message = "Success", response = Problem::class, responseContainer = "List"), ApiResponse(code = 404, message = "Not Found"), ApiResponse(code = 500, message = "Failure")))
    @RequestMapping(path = arrayOf("/problems"), method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    @Throws(IOException::class)
    fun problems(): List<Problem> {
        val problems = requestProblems()

        return Arrays.stream(problems)
                .map { x -> x.problemWithoutFunctionAndTestCases(sourceCodeOf(x)) }
                .collect(Collectors.toList<Problem>())
    }

    @Throws(IOException::class)
    private fun requestProblems(): Array<Problem> {
        val request = Request.Builder()
                .url(DATA_SERVICE_HOST + "problems")
                .build()

        val response = CLIENT.newCall(request).execute()
        val problemsAsJsonArray = response.body().string()
        return OBJECT_MAPPER.readValue(problemsAsJsonArray, Array<Problem>::class.java)
    }

    @ApiOperation(value = "problem", nickname = "getProblem")
    @ApiImplicitParams(ApiImplicitParam(name = "id", value = "Problem's id", required = true, dataType = "string", paramType = "path", defaultValue = "fib"))
    @ApiResponses(value = *arrayOf(ApiResponse(code = 200, message = "Success", response = Problem::class), ApiResponse(code = 404, message = "Not Found"), ApiResponse(code = 500, message = "Failure")))
    @RequestMapping(path = arrayOf("/problems/{id}"), method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    @Throws(IOException::class)
    fun problem(@PathVariable id: String): Problem {

        val problem = requestProblem(id)

        if (problem != null) {
            return problem.problemWithoutFunctionAndTestCases(
                    sourceCodeOf(problem)
            )
        }

        throw IllegalArgumentException("Invalid problem id: " + id)
    }

    @Throws(IOException::class)
    private fun requestProblem(problemId: String): Problem? {
        val request = Request.Builder()
                .url(DATA_SERVICE_HOST + "problems/" + problemId)
                .build()

        val response = CLIENT.newCall(request).execute()
        val problemAsJson = response.body().string()
        return OBJECT_MAPPER.readValue(problemAsJson, Problem::class.java)
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(JudgeController::class.java)

        private val DATA_SERVICE_HOST = "https://jalgoarena-data.herokuapp.com/"

        private val OBJECT_MAPPER = ObjectMapper()
        private val CLIENT = OkHttpClient()

        init {
            OBJECT_MAPPER.registerModule(Jdk8Module())
            OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        }

        private fun sourceCodeOf(problem: Problem): String {
            try {
                return JavaCodeGenerator.generateEmptyFunction(problem.function)
            } catch (e: ClassNotFoundException) {
                LOG.error(e.message, e)
                throw IllegalArgumentException("Illegal type: " + e.message)
            }

        }
    }
}
