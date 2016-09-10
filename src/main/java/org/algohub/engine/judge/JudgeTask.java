package org.algohub.engine.judge;

import org.algohub.engine.type.InternalTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

class JudgeTask implements Callable<List<Boolean>> {

    private static final Logger LOG = LoggerFactory.getLogger(JudgeTask.class);

    private final Object clazz;
    private final Method method;
    private final InternalTestCase[] testCases;

    JudgeTask(Object clazz, Method method, InternalTestCase[] testCases) {
        this.clazz = clazz;
        this.method = method;
        this.testCases = testCases;
    }

    @Override
    public List<Boolean> call() throws Exception {
        return run();
    }

    List<Boolean> run() throws InterruptedException {
        ArrayList<Boolean> results = new ArrayList<>();

        for (final InternalTestCase internalTestCase : testCases) {
            final boolean isCorrect = judge(clazz, method, internalTestCase);

            results.add(isCorrect);
        }

        return results;
    }

    private static boolean judge(final Object clazz, final Method method,
                                 final InternalTestCase testCase) throws InterruptedException {
        final Object output;
        Object[] input = null;
        try {
            input = testCase.getInput();
            output = method.invoke(clazz, input);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Throwable cause = getCause(e);
            LOG.error("Error during processing of solution", cause);
            throw new InterruptedException(cause.getClass().getName() + ": " + cause.getMessage());
        } catch (IllegalArgumentException e) {
            if (input != null) {
                LOG.error("Input count: " + input.length);
                Arrays.asList(input).forEach(
                        item -> LOG.error("Input type: " + item.getClass().toGenericString())
                );
            }

            LOG.error("Method parameters count: " + method.getParameters().length);
            Arrays.asList(method.getParameters()).forEach(
                    item -> LOG.error("Parameter type: " + item.getClass().toGenericString())
            );

            Throwable cause = getCause(e);
            LOG.error("Error during processing of solution", cause);

            throw new InterruptedException(cause.getClass().getName() + ": " + cause.getMessage());
        }

        if (testCase.returnsVoid()) {
            return BetterObjects.equalForObjectsOrArrays(testCase.getOutput(), input[0]);
        }

        return BetterObjects.equalForObjectsOrArrays(testCase.getOutput(), output);
    }

    private static Throwable getCause(Throwable e) {
        Throwable cause;
        Throwable result = e;

        while(null != (cause = result.getCause())  && (result != cause) ) {
            result = cause;
        }
        return result;
    }
}
