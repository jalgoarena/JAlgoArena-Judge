package org.algohub.engine.judge;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.algohub.engine.JudgeEngine;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.serde.ObjectMapperInstance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public final class JudgeEngineTestUtil {
  private static final JudgeEngine JUDGE_ENGINE = new JudgeEngine();

  public static void batchJudge() {
    File rootDir = new File("src/test/resources/questions/");
    Pattern pattern = Pattern.compile("\\w+\\.java");

    for (final File file : Files.fileTreeTraverser().preOrderTraversal(rootDir)) {

      final Path relativePath = rootDir.toPath().relativize(file.toPath());
      final String fileName = relativePath.getFileName().toString();
      final Matcher matcher = pattern.matcher(fileName);
      if (matcher.matches() && relativePath.getParent().endsWith("solutions")) {
        final String solutionPath = "questions/" + relativePath.toString();
        final String questionId = relativePath.getParent().getParent().toString();
        final String questionPath = "questions/" + questionId + "/" + questionId + ".json";

        judgeOne(questionPath, solutionPath);
      }
    }
  }

  private static void judgeOne(final String questionPath, final String solutionPath) {
    try {
      final String questionStr =
          Resources.toString(Resources.getResource(questionPath), Charsets.UTF_8);
      final Problem problem =
          ObjectMapperInstance.INSTANCE.readValue(questionStr, Problem.class);
      final String pythonCode =
          Resources.toString(Resources.getResource(solutionPath), Charsets.UTF_8);

      final JudgeResult result = JUDGE_ENGINE.judge(problem, pythonCode);
      assertEquals(StatusCode.ACCEPTED.toInt(), result.getStatusCode());
    } catch (InterruptedException | IOException e) {
      fail(e.getMessage());
    }
  }
}
