package org.algohub.engine.type;

import com.google.common.base.Preconditions;
import org.algohub.engine.judge.Function;
import org.algohub.engine.judge.Problem;

public class InternalTestCase {
    private final Object[] input;
    private final Object output;
    private final boolean returnsVoid;

    public InternalTestCase(final Problem.TestCase testCase, final Function function) {
        final Function.Parameter[] parameters = function.getParameters();
        Preconditions.checkArgument(parameters.length == testCase.getInput().size());

        input = new Object[testCase.getInput().size()];
        for (int i = 0; i < input.length; ++i) {
            input[i] = Deserializer.fromJson(parameters[i].getType(), testCase.getInput().get(i));
        }

        returnsVoid = IntermediateType.VOID.equals(function.getReturnStatement().getType().getValue());

        if (returnsVoid) {
            this.output = Deserializer.fromJson(parameters[0].getType(), testCase.getOutput());
        } else {
            this.output = Deserializer.fromJson(function.getReturnStatement().getType(), testCase.getOutput());
        }
    }

    public Object[] getInput() {
        return input;
    }

    public Object getOutput() {
        return output;
    }

    public boolean returnsVoid() {
        return returnsVoid;
    }
}
