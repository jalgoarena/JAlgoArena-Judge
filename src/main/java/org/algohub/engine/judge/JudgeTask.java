package org.algohub.engine.judge;

import org.algohub.engine.type.InternalTestCase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

class JudgeTask implements Callable<Integer> {

    private final Object clazz;
    private final Method method;
    private final InternalTestCase[] testCases;

    JudgeTask(Object clazz, Method method, InternalTestCase[] testCases) {
        this.clazz = clazz;
        this.method = method;
        this.testCases = testCases;
    }

    @Override
    public Integer call() throws Exception {
        return run();
    }

    int run() {
        int failedTestCases = 0;

        for (final InternalTestCase internalTestCase : testCases) {
            final boolean isCorrect = judge(clazz, method, internalTestCase);

            if (!isCorrect) {
                failedTestCases++;
            }
        }
        return failedTestCases;
    }

    private static boolean judge(final Object clazz, final Method method,
                                 final InternalTestCase testCase) {
        final Object output;
        try {
            output = method.invoke(clazz, testCase.getInput());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e.getMessage());
        }

        return BetterObjects.equal(testCase.getOutput(), output);
    }
}
