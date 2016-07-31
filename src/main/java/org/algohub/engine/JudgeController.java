package org.algohub.engine;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.algohub.engine.judge.JudgeEngine;
import org.algohub.engine.judge.JudgeResult;
import org.algohub.engine.judge.Problem;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
class JudgeController {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new Jdk8Module());
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @RequestMapping(path = "/problems/{id}/solution", method = RequestMethod.POST)
    JudgeResult judge(@PathVariable String id, @RequestBody String sourceCode) throws IOException {
        Problem problem = problemOf(id);
        return JudgeEngine.judge(problem, sourceCode);
    }

    @RequestMapping("/problems")
    String[] problems() {
        return new String[]{"2-sum", "fib", "stoi", "word-ladder"};
    }

    @RequestMapping("/problems/{id}")
    Problem problem(@PathVariable String id) throws IOException {
        return problemOf(id).toPublicProblem();
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
