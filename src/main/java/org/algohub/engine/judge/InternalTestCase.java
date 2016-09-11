package org.algohub.engine.judge;

import com.google.common.base.Preconditions;
import org.algohub.engine.ObjectMapperInstance;

import java.io.IOException;

class InternalTestCase {
    private final Object[] input;
    private final Object output;
    private final boolean returnsVoid;

    InternalTestCase(final Problem.TestCase testCase, final Function function) {
        final Function.Parameter[] parameters = function.getParameters();
        Preconditions.checkArgument(parameters.length == testCase.getInput().size());

        try {
            input = new Object[testCase.getInput().size()];
            for (int i = 0; i < input.length; ++i) {
                input[i] = deserialize(
                        testCase.getInput().get(i).toString(),
                        Class.forName(parameters[i].getType())
                );
            }

            returnsVoid = "void".equals(function.getReturnStatement().getType());

            if (returnsVoid) {
                this.output = deserialize(
                        testCase.getOutput().toString(),
                        Class.forName(parameters[0].getType())
                );
            } else {
                this.output = deserialize(
                        testCase.getOutput().toString(),
                        Class.forName(function.getReturnStatement().getType())
                );
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private Object deserialize(String content, Class<?> type) throws IOException {
        return ObjectMapperInstance.INSTANCE.readValue(content, type);
    }

    public Object[] getInput() {
        return input;
    }

    public Object getOutput() {
        return output;
    }

    boolean returnsVoid() {
        return returnsVoid;
    }
}
