package org.algohub.engine.type;

import org.algohub.engine.pojo.Function;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.serde.Deserializer;

public class InternalTestCase {
  private final Object[] input;
  private final Object output;

  public InternalTestCase(final Problem.TestCase testCase, final Function function) {
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
