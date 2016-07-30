package org.algohub.engine;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.algohub.engine.judge.JudgeEngine;
import org.algohub.engine.judge.JudgeResult;
import org.algohub.engine.judge.Problem;
import org.algohub.engine.type.ObjectMapperInstance;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
class JudgeController {

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

        return ObjectMapperInstance.INSTANCE.readValue(problemAsJson, Problem.class);
    }
}
