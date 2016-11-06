package org.algohub.engine;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.swagger.annotations.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.algohub.engine.judge.JudgeEngine;
import org.algohub.engine.judge.JudgeResult;
import org.algohub.engine.judge.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
class JudgeController {

    private static final Logger LOG = LoggerFactory.getLogger(JudgeController.class);

    private static final String DATA_SERVICE_HOST = "https://jalgoarena-data.herokuapp.com/";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final OkHttpClient CLIENT = new OkHttpClient();

    static {
        OBJECT_MAPPER.registerModule(new Jdk8Module());
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    private static Optional<Problem> problemOf(String id) {
        try {
            String problemAsJson = Resources.toString(
                    Resources.getResource(id + ".json"), Charsets.UTF_8
            );

            return Optional.of(OBJECT_MAPPER.readValue(problemAsJson, Problem.class));
        } catch (IOException | IllegalArgumentException e) {
            LOG.error("Cannot parse problem: " + id, e);
            return Optional.empty();
        }
    }

    @ApiOperation(value = "judge", nickname = "judge")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Problem's id", required = true, dataType = "string", paramType = "path", defaultValue="fib")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = JudgeResult.class),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @RequestMapping(path = "/problems/{id}/submit", method = RequestMethod.POST, produces = "application/json")
    JudgeResult judge(@PathVariable String id, @RequestBody String sourceCode) throws IOException {
        Optional<Problem> problem = problemOf(id);

        if (!problem.isPresent()) {
            return JudgeResult.runtimeError("Wrong problem id: " + id);
        }

        return JudgeEngine.judge(problem.get(), sourceCode);
    }

    @ApiOperation(value = "problems", nickname = "getProblems")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Problem.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @RequestMapping(path = "/problems", method = RequestMethod.GET, produces = "application/json")
    List<Problem> problems() throws IOException {
        Problem[] problems = requestProblems();

        return Arrays.stream(problems)
                .map(x -> x.problemWithoutFunctionAndTestCases(sourceCodeOf(x)))
                .collect(Collectors.toList());
    }

    private Problem[] requestProblems() throws IOException {
        Request request = new Request.Builder()
                .url(DATA_SERVICE_HOST + "problems")
                .build();

        Response response = CLIENT.newCall(request).execute();
        String problemsAsJsonArray = response.body().string();
        return OBJECT_MAPPER.readValue(problemsAsJsonArray, Problem[].class);
    }

    @ApiOperation(value = "problem", nickname = "getProblem")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Problem's id", required = true, dataType = "string", paramType = "path", defaultValue="fib")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Problem.class),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @RequestMapping(path = "/problems/{id}", method = RequestMethod.GET, produces = "application/json")
    Problem problem(@PathVariable String id) throws IOException {

        Problem problem = requestProblem(id);

        if (problem != null) {
            return problem.problemWithoutFunctionAndTestCases(
                sourceCodeOf(problem)
            );
        }

        throw new IllegalArgumentException("Invalid problem id: " + id);
    }

    private Problem requestProblem(String problemId) throws IOException {
        Request request = new Request.Builder()
                .url(DATA_SERVICE_HOST + "problems/" + problemId)
                .build();

        Response response = CLIENT.newCall(request).execute();
        String problemAsJson = response.body().string();
        return OBJECT_MAPPER.readValue(problemAsJson, Problem.class);
    }

    private static String sourceCodeOf(Problem problem) {
        try {
            return JavaCodeGenerator.generateEmptyFunction(problem.getFunction());
        } catch (ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
            throw new IllegalArgumentException("Illegal type: " + e.getMessage());
        }
    }
}
