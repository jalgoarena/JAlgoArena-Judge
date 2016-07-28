package org.algohub.engine.web;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.algohub.engine.codegenerator.JavaCodeGenerator;
import org.algohub.engine.judge.JudgeEngine;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.serde.ObjectMapperInstance;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
class JudgeController {

    @RequestMapping(path = "/problems/{id}/solution", method = RequestMethod.POST)
    JudgeResult judge(@PathVariable String id, @RequestBody String sourceCode) throws Exception {
        Problem problem = problemOf(id);
        JudgeResult judge = JudgeEngine.judge(problem, sourceCode);
        return judge;
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
    String problemSkeletonCode(@PathVariable String id) throws Exception {
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
