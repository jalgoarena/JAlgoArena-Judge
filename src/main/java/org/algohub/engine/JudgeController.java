package org.algohub.engine;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.algohub.engine.judge.JudgeEngine;
import org.algohub.engine.judge.JudgeResult;
import org.algohub.engine.judge.Problem;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
class JudgeController {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final List<String> AVAILABLE_PROBLEMS;

    static {
        OBJECT_MAPPER.registerModule(new Jdk8Module());
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        AVAILABLE_PROBLEMS = jsonFilesFromResources().map(
                x -> x.substring(0, x.length() - ".json".length())
        ).collect(Collectors.toList());
    }

    @RequestMapping(path = "/problems/{id}/solution", method = RequestMethod.POST)
    JudgeResult judge(@PathVariable String id, @RequestBody String sourceCode) throws IOException {
        Problem problem = problemOf(id);
        return JudgeEngine.judge(problem, sourceCode);
    }

    @RequestMapping("/problems")
    List<String> problems() throws IOException {
        return AVAILABLE_PROBLEMS;
    }

    private static Stream<String> jsonFilesFromResources() {
        return new Reflections(
                "", new ResourcesScanner()
        ).getResources(Pattern.compile(".+\\.json")).stream();
    }

    @RequestMapping("/problems/{id}")
    Problem problem(@PathVariable String id) throws IOException {
        return problemOf(id).problemWithoutFunctionAndTestCases();
    }

    @RequestMapping("/problems/{id}/skeletonCode")
    String problemSkeletonCode(@PathVariable String id) throws IOException {
        Problem problem = problemOf(id);
        return JavaCodeGenerator.generateEmptyFunction(problem.getFunction());
    }

    private Problem problemOf(String id) throws IOException {
        String problemAsJson = Resources.toString(
                Resources.getResource(id + ".json"), Charsets.UTF_8
        );

        return OBJECT_MAPPER.readValue(problemAsJson, Problem.class);
    }
}
