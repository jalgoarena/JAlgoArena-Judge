package org.algohub.engine.judge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.*;

public class JudgeEngine {

    private static final Logger LOG = LoggerFactory.getLogger(JudgeEngine.class);

    private static final FindClassName findClassName = new FindClassName();
    private static final CreateFriendlyMessage createFriendlyMessage = new CreateFriendlyMessage();

    private static final int NUMBER_OF_ITERATIONS = 5;
    private static final Random RANDOM = new Random();

    private JudgeEngine() {
        // static class
    }

    private static JudgeResult judge(final Object clazz,
                                     final Method method,
                                     final Problem problem) throws InterruptedException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        InternalTestCase[] testCases = readInternalTestCases(problem);
        Future<List<Boolean>> judge = executorService.submit(new JudgeTask(clazz, method, testCases));


        // # RUN 1 - cold run making JVM hot, mainly checks if all tests passes and we do not exceeded time limit
        try {
            List<Boolean> results = judge.get(problem.getTimeLimit(), TimeUnit.SECONDS);

            int failedTestCases = (int) results.stream().filter(x -> !x).count();

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
        return runPerformanceEvaluation(clazz, method, problem, testCases);
    }

    private static JudgeResult runPerformanceEvaluation(Object clazz, Method method, Problem problem, InternalTestCase[] testCases) {
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<PerformanceResult> performanceResultFuture = evaluatePerformance(clazz, method, problem, executorService);
            PerformanceResult performanceResult = performanceResultFuture.get(problem.getTimeLimit(), TimeUnit.SECONDS);
            for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
                performanceResultFuture = evaluatePerformance(clazz, method, problem, executorService);
                performanceResult = performanceResult.chooseBestResults(performanceResultFuture.get(problem.getTimeLimit(), TimeUnit.SECONDS));
            }

            if (performanceResult.usedMemoryInBytes / 1024 > problem.getMemoryLimit()) {
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
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error in processing solution", e);
            return JudgeResult.runtimeError(e.getMessage());
        } catch (TimeoutException e) {
            LOG.error("Timeout error", e);
            return JudgeResult.timeLimitExceeded();
        }
    }

    private static Future<PerformanceResult> evaluatePerformance(Object clazz, Method method, Problem problem, ExecutorService executorService) {
        return executorService.submit(new JudgePerformanceTask(clazz, method, readInternalTestCases(problem)));
    }


    /**
     * Runs judge on given source code for a given problem
     *
     * @param problem  - problem to solve
     * @param userCode - source code solving the problem
     * @return - result of judge
     */
    public static JudgeResult judge(final Problem problem, final String userCode) {

        final Object instance;
        final Method method;

        Function function = problem.getFunction();

        try {
            final Optional<String> className = findClassName.in(userCode);
            if (!className.isPresent()) {
                return new JudgeResult("ClassNotFoundException: No public class found");
            }
            final Object[] tmp = new MemoryJavaCompiler()
                    .compileMethod(className.get(), function.getName(), userCode);
            instance = tmp[0];
            method = (Method) tmp[1];
        } catch (ClassNotFoundException e) {
            LOG.error("Class not found", e);
            return new JudgeResult(e.getClass() + " : " + e.getMessage());
        } catch (CompileErrorException e) {
            LOG.error("Compilation error", e);
            return new JudgeResult(createFriendlyMessage.from(e.getMessage()));
        } catch (NoSuchMethodError e) {
            LOG.error("No such method error", e);
            return new JudgeResult("No such method: " + e.getMessage());
        }

        try {
            return judge(instance, method, problem);
        } catch (Exception e) {
            return JudgeResult.runtimeError(e.getMessage());
        }
    }

    private static InternalTestCase[] readInternalTestCases(Problem problem) {
        Problem.TestCase[] testCases = problem.getTestCases();
        Function function = problem.getFunction();

        final InternalTestCase[] internalTestCases = new InternalTestCase[testCases.length];
        for (int i = 0; i < testCases.length; ++i) {
            internalTestCases[i] = new InternalTestCase(testCases[i], function);
        }

        shuffle(internalTestCases);
        return internalTestCases;
    }

    private static void shuffle(InternalTestCase[] array) {
        int count = array.length;

        for (int i = count; i > 1; i--) {
            swap(array, i - 1, RANDOM.nextInt(i));
        }
    }

    private static void swap(InternalTestCase[] array, int i, int j) {
        InternalTestCase temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static class PerformanceSnapshot {
        final long currentNanoTime;
        final long usedMemoryInBytes;

        PerformanceSnapshot(long currentNanoTime, long usedMemoryInBytes) {
            this.currentNanoTime = currentNanoTime;
            this.usedMemoryInBytes = usedMemoryInBytes;
        }

        static PerformanceSnapshot create(long timeNanoSeconds, long memoryBytes) {
            return new PerformanceSnapshot(timeNanoSeconds, memoryBytes);
        }
    }

    private static class PerformanceResult {
        final long usedMemoryInBytes;
        final double usedTimeInMs;

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

        PerformanceResult chooseBestResults(PerformanceResult performanceResult) {
            return new PerformanceResult(
                    Math.min(performanceResult.usedMemoryInBytes, usedMemoryInBytes),
                    Math.min(performanceResult.usedTimeInMs, usedTimeInMs)
            );
        }
    }

    private static class JudgePerformanceTask implements Callable<PerformanceResult> {

        private final Object clazz;
        private final Method method;
        private final InternalTestCase[] testCases;

        JudgePerformanceTask(Object clazz, Method method, InternalTestCase[] testCases) {
            this.clazz = clazz;
            this.method = method;
            this.testCases = testCases;
        }

        @Override
        public PerformanceResult call() throws Exception {
            PerformanceSnapshot snapshotBeforeRun = takePerformanceSnapshot();

            new JudgeTask(this.clazz, this.method, this.testCases).call();

            PerformanceSnapshot snapshotAfterRun = takePerformanceSnapshot();

            return PerformanceResult.create(snapshotBeforeRun, snapshotAfterRun);
        }

        private PerformanceSnapshot takePerformanceSnapshot() {
            Runtime runtime = Runtime.getRuntime();

            return PerformanceSnapshot.create(
                    System.nanoTime(),
                    runtime.totalMemory() - runtime.freeMemory()
            );
        }
    }
}
