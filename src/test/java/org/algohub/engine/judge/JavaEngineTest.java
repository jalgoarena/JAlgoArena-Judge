package org.algohub.engine.judge;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.algohub.engine.ObjectMapperInstance;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnitParamsRunner.class)
public class JavaEngineTest {

    private static void judgeSolution(final String problemId, final String solutionId, StatusCode statusCode) {
        try {
            final String questionStr =
                    Resources.toString(Resources.getResource(problemId + ".json"), Charsets.UTF_8);
            final Problem problem =
                    ObjectMapperInstance.INSTANCE.getINSTANCE().readValue(questionStr, Problem.class);
            final String sourceCode =
                    Resources.toString(Resources.getResource(solutionId + ".java"), Charsets.UTF_8);

            final JudgeResult result = JudgeEngine.INSTANCE.judge(problem, sourceCode);

            assertThat(result.getStatusCode()).isEqualTo(statusCode.toString());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @Parameters({
            "2-sum, TwoSum",
            "fib, FibFast",
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
