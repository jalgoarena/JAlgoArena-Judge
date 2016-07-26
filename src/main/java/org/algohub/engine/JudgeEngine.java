package org.algohub.engine;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.algohub.engine.codegenerator.JavaCodeGenerator;
import org.algohub.engine.judge.JavaJudge;
import org.algohub.engine.judge.StatusCode;
import org.algohub.engine.pojo.Function;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.serde.ObjectMapperInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@SpringBootApplication
@Controller
public class JudgeEngine {

    private final transient JavaJudge javaJudge = new JavaJudge();

    @RequestMapping("/problems")
    @ResponseBody
    public String[] problems() {
        return new String[] {"2-sum", "fib", "stoi", "word-ladder"};
    }

    @RequestMapping("/problem/{id}")
    @ResponseBody
    public Problem problem(@PathVariable String id) throws IOException {
        URL resource = Resources.getResource(id + ".json");
        return getProblem(resource.getFile()).toPublicProblem();
    }

    public static void main(final String[] args) throws IOException, InterruptedException {

        if (args.length == 0) {
            SpringApplication.run(JudgeEngine.class, args);
            return;
        }

        if (args.length == 1) {
            final Problem problem = getProblem(args[0]);
            Function function = problem.getFunction();
            System.out.println(JavaCodeGenerator.generateEmptyFunction(function));
            return;
        }

        if (args.length != 2) {
            System.err.println("Usage: JudgeEngine problem.json solution");
            return;
        }

        final Problem problem = getProblem(args[0]);
        final String userCode = asCharSource(args[1]);

        final JudgeEngine judgeEngine = new JudgeEngine();
        final JudgeResult result = judgeEngine.judge(problem, userCode);

        if (result.getStatusCode() == StatusCode.ACCEPTED.toInt()) {
            System.out.println("Accepted!");
            System.out.println(result);
        } else {
            System.err.println("Wrong Answer!\n" + result);
        }
    }

    private static Problem getProblem(String arg) throws IOException {
        final String problemStr = asCharSource(arg);
        return ObjectMapperInstance.INSTANCE.readValue(problemStr, Problem.class);
    }

    private static String asCharSource(String problemName) throws IOException {
        return Files.asCharSource(new File(problemName), Charsets.UTF_8).read();
    }

    /**
     * Judge the code written by a user.
     *
     * @param problem  the problem description and test cases
     * @param userCode the function written by user.
     * @return If the output is identical with the test case, JudgeResult.succeed will be true,
     * otherwise, JudgeResult.succeed will be false and contain both output results.
     */
    public JudgeResult judge(final Problem problem, final String userCode)
            throws InterruptedException {

        return javaJudge.judge(problem, userCode);
    }
}
