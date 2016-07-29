package org.algohub.engine.judge;

import org.algohub.engine.type.InternalTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public class JudgeEngine {

    private static final Logger LOG = LoggerFactory.getLogger(JudgeEngine.class);

    private static final FindClassName findClassName = new FindClassName();
    private static final CreateFriendlyMessage createFriendlyMessage = new CreateFriendlyMessage();

    private static final int NUMBER_OF_ITERATIONS = 10;

    private JudgeEngine() {
        // static class
    }

    private static JudgeResult judge(final Object clazz,
                                     final Method method,
                                     final InternalTestCase[] testCases,
                                     final Problem problem) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Boolean>> judge = executorService.submit(new JudgeTask(clazz, method, testCases));


        // # RUN 1 - cold run making JVM hot, mainly checks if all tests passes and we do not exceeded time limit
        try {
            List<Boolean> results = judge.get(problem.getTimeLimit(), TimeUnit.SECONDS);

            int failedTestCases = (int)results.stream().filter(x -> !x).count();

            if (failedTestCases > 0) {
                return JudgeResult.wrongAnswer(results);
            }
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error in processing solution", e);
            return JudgeResult.runtimeError(e.getMessage());
        } catch (TimeoutException e) {
            LOG.error("Timeout error", e);
            return JudgeResult.timeLimitExceeded();
        }

        // # RUN 2 - hot runs, run the code couple of times gathering time and memory measurements to return best

        PerformanceResult performanceResult = getPerformanceResult(clazz, method, testCases);
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            performanceResult = performanceResult.compare(getPerformanceResult(clazz, method, testCases));
        }

        if (performanceResult.usedMemoryInBytes > problem.getMemoryLimit()) {
            return JudgeResult.memoryLimitExceeded(
                    testCases.length,
                    performanceResult.usedMemoryInBytes
            );
        }

        return JudgeResult.accepted(
                testCases.length,
                performanceResult.usedTimeInMs,
                performanceResult.usedMemoryInBytes
        );
    }

    private static PerformanceResult getPerformanceResult(Object clazz, Method method, InternalTestCase[] testCases) {
        PerformanceSnapshot snapshotBeforeRun = takePerformanceSnapshot();

        new JudgeTask(clazz, method, testCases).run();

        PerformanceSnapshot snapshotAfterRun = takePerformanceSnapshot();

        return PerformanceResult.create(snapshotBeforeRun, snapshotAfterRun);
    }

    private static PerformanceSnapshot takePerformanceSnapshot() {
        Runtime runtime = Runtime.getRuntime();

        return PerformanceSnapshot.create(
                System.nanoTime(),
                runtime.totalMemory() - runtime.freeMemory()
        );
    }

    /**
     * Runs judge on given source code for a given problem
     * @param problem - problem to solve
     * @param userCode - source code solving the problem
     * @return - result of judge
     */
    public static synchronized JudgeResult judge(final Problem problem, final String userCode) {

        Problem.TestCase[] testCases = problem.getTestCases();
        Function function = problem.getFunction();

        final InternalTestCase[] internalTestCases = new InternalTestCase[testCases.length];
        for (int i = 0; i < testCases.length; ++i) {
            internalTestCases[i] = new InternalTestCase(testCases[i], function);
        }
        return judge(problem, internalTestCases, userCode);
    }

    private static JudgeResult judge(final Problem problem, final InternalTestCase[] testCases,
                              final String userCode) {
        final Object clazz;
        final Method method;

        Function function = problem.getFunction();

        try {
            final Optional<String> className = findClassName.in(userCode);
            if (!className.isPresent()) {
                return new JudgeResult("ClassNotFoundException: No public class found");
            }
            final Object[] tmp = MemoryJavaCompiler.INSTANCE
                    .compileMethod(className.get(), function.getName(), userCode);
            clazz = tmp[0];
            method = (Method) tmp[1];
        } catch (ClassNotFoundException e) {
            LOG.error("Class not found", e);
            return new JudgeResult(e.getClass() + " : " + e.getMessage());
        } catch (CompileErrorException e) {
            LOG.error("Compilation error", e);
            return new JudgeResult(createFriendlyMessage.from(e.getMessage()));
        }

        return judge(clazz, method, testCases, problem);
    }

    private static class PerformanceSnapshot {
        long currentNanoTime;
        long usedMemoryInBytes;

        PerformanceSnapshot(long currentNanoTime, long usedMemoryInBytes) {
            this.currentNanoTime = currentNanoTime;
            this.usedMemoryInBytes = usedMemoryInBytes;
        }

        static PerformanceSnapshot create(long timeNanoSeconds, long memoryBytes) {
            return new PerformanceSnapshot(timeNanoSeconds, memoryBytes);
        }
    }

    private static class PerformanceResult {
        long usedMemoryInBytes;
        double usedTimeInMs;

        private PerformanceResult(long usedMemoryInBytes, double usedTimeInMs) {
            this.usedMemoryInBytes = usedMemoryInBytes;
            this.usedTimeInMs = usedTimeInMs;
        }

        static PerformanceResult create(PerformanceSnapshot before, PerformanceSnapshot after) {
            return new PerformanceResult(
                    usedMemoryInBytes(before, after),
                    usedTimeInMs(before, after)
            );
        }

        private static double usedTimeInMs(PerformanceSnapshot before, PerformanceSnapshot after) {
            return (after.currentNanoTime - before.currentNanoTime) / (1000.0 * 1000.0);
        }

        private static long usedMemoryInBytes(PerformanceSnapshot before, PerformanceSnapshot after) {
            return after.usedMemoryInBytes - before.usedMemoryInBytes;
        }

        PerformanceResult compare(PerformanceResult performanceResult) {
            return new PerformanceResult(
                    Math.min(performanceResult.usedMemoryInBytes, usedMemoryInBytes),
                    Math.min(performanceResult.usedTimeInMs, usedTimeInMs)
            );
        }
    }
}
