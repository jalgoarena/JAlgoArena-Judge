package org.algohub.engine;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.swagger.annotations.*;
import org.algohub.engine.judge.JudgeEngine;
import org.algohub.engine.judge.JudgeResult;
import org.algohub.engine.judge.Problem;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin
@RestController

class JudgeController {

    private static final Logger LOG = LoggerFactory.getLogger(JudgeController.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final List<Problem> AVAILABLE_PROBLEMS;

    static {
        OBJECT_MAPPER.registerModule(new Jdk8Module());
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        AVAILABLE_PROBLEMS = jsonFilesFromResources()
                .filter(x -> !x.contains("/"))
                .map(x -> x.substring(0, x.length() - ".json".length()))
                .map(JudgeController::problemOf)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private static Stream<String> jsonFilesFromResources() {
        return new Reflections(
                "", new ResourcesScanner()
        ).getResources(Pattern.compile("[a-z0-9-]+\\.json")).stream();
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
        return AVAILABLE_PROBLEMS.stream()
                .map(x -> x.problemWithoutFunctionAndTestCases(sourceCodeOf(x)))
                .collect(Collectors.toList());
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

        Optional<Problem> problem = AVAILABLE_PROBLEMS
                .stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();

        if (problem.isPresent()) {
            return problem.get().problemWithoutFunctionAndTestCases(
                    sourceCodeOf(problem.get())
            );
        }

        throw new IllegalArgumentException("Invalid problem id: " + id);
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
