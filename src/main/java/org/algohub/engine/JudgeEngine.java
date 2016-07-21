package org.algohub.engine;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.algohub.engine.pojo.Question;
import org.algohub.engine.judge.JavaJudge;
import org.algohub.engine.judge.StatusCode;
import org.algohub.engine.pojo.Function;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.util.ObjectMapperInstance;

import java.io.File;
import java.io.IOException;

/**
 * The final judge compose of multiple languages' judges.
 */
public class JudgeEngine {
  /**
   * Java judge.
   */
  private final transient JavaJudge javaJudge = new JavaJudge();


  /**
   * Entry point main function.
   */
  public static void main(final String[] args) throws IOException, InterruptedException {
    if (args.length != 3) {
      System.err.println("Usage: JudgeEngine problem.json language(PYTHON,RUBY) solution");
      return;
    }

    final String problemStr = Files.asCharSource(new File(args[0]), Charsets.UTF_8).read();
    final Question question = ObjectMapperInstance.INSTANCE.readValue(problemStr, Question.class);
    final String userCode = Files.asCharSource(new File(args[2]), Charsets.UTF_8).read();

    final JudgeEngine judgeEngine = new JudgeEngine();
    final JudgeResult result = judgeEngine.judge(question, userCode);

    if (result.getStatusCode() == StatusCode.ACCEPTED.toInt()) {
      System.out.println("Accepted!");
    } else {
      System.err.println("Wrong Answer!\n" + result);
    }
  }

  /**
   * Judge the code written by a user.
   *
   * @param function     The function prototype declaration
   * @param testCases    test cases
   * @param userCode     A function implemented by user
   * @return If the output is identical with the test case, JudgeResult.succeed will be true,
   * otherwise, JudgeResult.succeed will be false and contain both output results.
   */
  private JudgeResult judge(final Function function, final Question.TestCase[] testCases,
      final String userCode) throws InterruptedException {
    return javaJudge.judge(function, testCases, userCode);
  }

  /**
   * Judge the code written by a user.
   *
   * @param question     the question description and test cases
   * @param userCode     the function written by user.
   * @return If the output is identical with the test case, JudgeResult.succeed will be true,
   * otherwise, JudgeResult.succeed will be false and contain both output results.
   */
  @SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops"}) public JudgeResult judge(
      final Question question, final String userCode)
      throws InterruptedException {
    final Question.TestCase[] testCases = question.getTestCases();
    return judge(question.getFunction(), testCases, userCode);

  }
}
