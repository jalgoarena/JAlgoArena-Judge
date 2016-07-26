package org.algohub.engine.judge;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.serde.ObjectMapperInstance;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnitParamsRunner.class)
public class JavaEngineTest {

    private static final JudgeEngine JUDGE_ENGINE = new JudgeEngine();

    private static void judgeOne(final String questionPath, final String solutionPath, StatusCode expectedStatusCode) {
        try {
            final String questionStr =
                    Resources.toString(Resources.getResource(questionPath), Charsets.UTF_8);
            final Problem problem =
                    ObjectMapperInstance.INSTANCE.readValue(questionStr, Problem.class);
            final String pythonCode =
                    Resources.toString(Resources.getResource(solutionPath), Charsets.UTF_8);

            final JudgeResult result = JUDGE_ENGINE.judge(problem, pythonCode);
            assertThat(result.getStatusCode()).isEqualTo(expectedStatusCode.toString());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @Parameters({
        "2-sum, TwoSum",
        "fib, Fib",
        "stoi, MyStoi",
        "word-ladder, WordLadder"
    })
    public void acceptsCorrectSolution(String problemId, String solutionId) throws Exception {
        String problemPath = problemId + ".json";
        String solutionPath = solutionId + ".java";

        judgeOne(problemPath, solutionPath, StatusCode.ACCEPTED);
    }

    @Test
    public void failsWithCompilationErrorWhenSourceCodeDoesNotCompile() throws Exception {
        judgeOne("fib.json", "FibBroken.java", StatusCode.COMPILE_ERROR);
    }
}
