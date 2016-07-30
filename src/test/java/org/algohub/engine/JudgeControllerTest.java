package org.algohub.engine;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.algohub.engine.judge.JudgeResult;
import org.algohub.engine.judge.Problem;
import org.algohub.engine.judge.StatusCode;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class JudgeControllerTest {

    private JudgeController controller = new JudgeController();

    @Test
    @Parameters({
        "fib, Fibonacci",
        "2-sum, 2Sum",
        "stoi, String to Integer (atoi)",
        "word-ladder, Word Ladder"
    })
    public void findsExistingProblems(String problemId, String problemTitle) throws Exception {
        Problem fib = controller.problem(problemId);
        assertThat(fib.getTitle()).isEqualTo(problemTitle);
    }

    @Test
    @Parameters({
            "fib",
            "2-sum",
            "stoi",
            "word-ladder"
    })
    public void includesProblemInListOfAllProblems(String problemId) throws Exception {
        boolean result = Arrays.asList(controller.problems()).contains(problemId);
        assertThat(result).isTrue();
    }

    @Test
    @Parameters({
            "fib",
            "2-sum",
            "stoi",
            "word-ladder"
    })
    public void generatesNonEmptySkeletonCode(String problemId) throws Exception {
        String skeletonCode = controller.problemSkeletonCode(problemId);
        assertThat(skeletonCode).isNotEmpty();
    }

    @Test
    @Parameters({
            "2-sum, TwoSum",
            "fib, FibFast",
            "stoi, MyStoi",
            "word-ladder, WordLadder",
            "is-string-unique, IsStringUnique2",
            "check-perm, CheckPerm",
            "palindrome-perm, PalindromePerm",
            "one-away, OneAway",
            "string-compress, StringCompress",
            "rotate-matrix, RotateMatrix",
            "zero-matrix, ZeroMatrix",
            "remove-dups, RemoveDups"
    })
    public void judgesCorrectSolution(String problemId, String solutionId) throws Exception {
        final String sourceCode =
                Resources.toString(Resources.getResource(solutionId + ".java"), Charsets.UTF_8);
        JudgeResult result = controller.judge(problemId, sourceCode);

        assertThat(result.getStatusCode()).isEqualTo(StatusCode.ACCEPTED.toString());
    }
}