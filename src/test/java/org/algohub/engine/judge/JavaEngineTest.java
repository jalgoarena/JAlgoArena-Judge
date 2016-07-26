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

    private static void judgeSolution(final String problemId, final String solutionId, StatusCode expectedStatusCode) {
        try {
            final String questionStr =
                    Resources.toString(Resources.getResource(problemId + ".json"), Charsets.UTF_8);
            final Problem problem =
                    ObjectMapperInstance.INSTANCE.readValue(questionStr, Problem.class);
            final String pythonCode =
                    Resources.toString(Resources.getResource(solutionId + ".java"), Charsets.UTF_8);

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
        judgeSolution(problemId, solutionId, StatusCode.ACCEPTED);
    }

    @Test
    public void failsWithCompilationErrorWhenSourceCodeDoesNotCompile() throws Exception {
        judgeSolution("fib", "FibBroken", StatusCode.COMPILE_ERROR);
    }

    @Test
    public void returnsWrongAnswerForUncorrectSolution() throws Exception {
        judgeSolution("fib", "FibWrongAnswer", StatusCode.WRONG_ANSWER);
    }
}
