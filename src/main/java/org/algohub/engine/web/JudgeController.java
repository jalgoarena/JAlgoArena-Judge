package org.algohub.engine.web;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.algohub.engine.codegenerator.JavaCodeGenerator;
import org.algohub.engine.judge.JudgeEngine;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.serde.ObjectMapperInstance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
class JudgeController {

    private final transient JudgeEngine judgeEngine = new JudgeEngine();

    @RequestMapping(path = "/problems/{id}/solution", method = RequestMethod.POST)
    @ResponseBody
    JudgeResult judge(@PathVariable String id, @RequestBody String sourceCode) throws Exception {
        Problem problem = getProblemById(id);
        return judgeEngine.judge(problem, sourceCode);
    }

    @RequestMapping("/problems")
    @ResponseBody
    String[] problems() {
        return new String[]{"2-sum", "fib", "stoi", "word-ladder"};
    }

    @RequestMapping("/problems/{id}")
    @ResponseBody
    Problem problem(@PathVariable String id) throws IOException {
        return getProblemById(id).toPublicProblem();
    }

    @RequestMapping("/problems/{id}/skeletonCode")
    @ResponseBody
    String problemSkeletonCode(@PathVariable String id) throws Exception {
        Problem problem = getProblemById(id);
        return JavaCodeGenerator.generateEmptyFunction(problem.getFunction());
    }

    private Problem getProblemById(String id) throws IOException {
        String problemAsJson = Resources.toString(
                Resources.getResource(id + ".json"), Charsets.UTF_8
        );

        return ObjectMapperInstance.INSTANCE.readValue(problemAsJson, Problem.class);
    }
}
