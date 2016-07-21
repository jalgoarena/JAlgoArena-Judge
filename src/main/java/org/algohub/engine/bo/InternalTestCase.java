package org.algohub.engine.bo;

import org.algohub.engine.pojo.Function;
import org.algohub.engine.pojo.Question;
import org.algohub.engine.serde.Deserializer;

public class InternalTestCase {
  private final Object[] input;
  private final Object output;

  public InternalTestCase(final Object[] input, final Object output) {
    this.input = input;
    this.output = output;
  }

  public InternalTestCase(final Question.TestCase testCase, final Function function) {
    final Function.Parameter[] parameters = function.getParameters();
    assert (parameters.length == testCase.getInput().size());

    input = new Object[testCase.getInput().size()];
    for (int i = 0; i < input.length; ++i) {
      input[i] = Deserializer.fromJson(parameters[i].getType(), testCase.getInput().get(i));
    }

    this.output = Deserializer.fromJson(function.getReturn_().getType(), testCase.getOutput());
  }

  public Object[] getInput() {
    return input;
  }

  public Object getOutput() {
    return output;
  }
}
