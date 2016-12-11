package org.algohub.engine.judge

import org.slf4j.LoggerFactory
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.*

object JudgeEngine {

    private val LOG = LoggerFactory.getLogger(JudgeEngine::class.java)

    private val findClassName = FindClassName()
    private val createFriendlyMessage = CreateFriendlyMessage()

    private val NUMBER_OF_ITERATIONS = 5
    private val RANDOM = Random()

    @Throws(InterruptedException::class)
    private fun judge(clazz: Any,
                      method: Method,
                      problem: Problem): JudgeResult {

        val executorService = Executors.newSingleThreadExecutor()
        val testCases = readInternalTestCases(problem)
        val judge = executorService.submit(JudgeTask(clazz, method, testCases))


        // # RUN 1 - cold run making JVM hot, mainly checks if all tests passes and we do not exceeded time limit
        try {
            val results: List<Boolean> = judge.get(problem.timeLimit, TimeUnit.SECONDS)

            val failedTestCases = results.filter({ !it }).count()

            if (failedTestCases > 0) {
                return JudgeResult.wrongAnswer(results)
            }
        } catch (e: InterruptedException) {
            LOG.error("Error in processing solution", e)
            return JudgeResult.runtimeError(e.message)
        } catch (e: ExecutionException) {
            LOG.error("Error in processing solution", e)
            return JudgeResult.runtimeError(e.message)
        } catch (e: TimeoutException) {
            LOG.error("Timeout error", e)
            return JudgeResult.timeLimitExceeded()
        }

        // # RUN 2 - hot runs, run the code couple of times gathering time and memory measurements to return best
        return runPerformanceEvaluation(clazz, method, problem, testCases)
    }

    private fun runPerformanceEvaluation(clazz: Any, method: Method, problem: Problem, testCases: Array<InternalTestCase>): JudgeResult {
        try {
            val executorService = Executors.newSingleThreadExecutor()
            var performanceResultFuture = evaluatePerformance(clazz, method, problem, executorService)
            var performanceResult = performanceResultFuture.get(problem.timeLimit, TimeUnit.SECONDS)

            for (i in 0..NUMBER_OF_ITERATIONS - 1) {
                performanceResultFuture = evaluatePerformance(clazz, method, problem, executorService)
                performanceResult = performanceResult.chooseBestResults(performanceResultFuture.get(problem.timeLimit, TimeUnit.SECONDS))
            }

            if (performanceResult.usedMemoryInBytes / 1024 > problem.memoryLimit) {
                return JudgeResult.memoryLimitExceeded(
                        testCases.size,
                        performanceResult.usedMemoryInBytes
                )
            }

            return JudgeResult.accepted(
                    testCases.size,
                    performanceResult.usedTimeInMs,
                    performanceResult.usedMemoryInBytes
            )
        } catch (e: InterruptedException) {
            LOG.error("Error in processing solution", e)
            return JudgeResult.runtimeError(e.message)
        } catch (e: ExecutionException) {
            LOG.error("Error in processing solution", e)
            return JudgeResult.runtimeError(e.message)
        } catch (e: TimeoutException) {
            LOG.error("Timeout error", e)
            return JudgeResult.timeLimitExceeded()
        }

    }

    private fun evaluatePerformance(clazz: Any, method: Method, problem: Problem, executorService: ExecutorService): Future<PerformanceResult> {
        return executorService.submit(JudgePerformanceTask(clazz, method, readInternalTestCases(problem)))
    }


    /**
     * Runs judge on given source code for a given problem
     * @param problem  - problem to solve
     * *
     * @param userCode - source code solving the problem
     * *
     * @return - result of judge
     */
    fun judge(problem: Problem, userCode: String): JudgeResult {

        try {
            return compileAndJudge(problem, userCode)
        } catch (e: ClassNotFoundException) {
            LOG.error("Class not found", e)
            return JudgeResult(e.javaClass.toString() + " : " + e.message)
        } catch (e: CompileErrorException) {
            LOG.error("Compilation error", e)
            return JudgeResult(createFriendlyMessage.from(e.message!!))
        } catch (e: NoSuchMethodError) {
            LOG.error("No such method error", e)
            return JudgeResult("No such method: " + e.message)
        }

    }

    @Throws(ClassNotFoundException::class, CompileErrorException::class)
    private fun compileAndJudge(problem: Problem, userCode: String): JudgeResult {

        val className = findClassName.`in`(userCode)

        if (!className.isPresent) {
            return JudgeResult("ClassNotFoundException: No public class found")
        }

        val tmp = MemoryJavaCompiler().compileMethod(
                className.get(), problem.function.name, userCode
        )

        val instance = tmp[0]
        val method = tmp[1] as Method

        try {
            return judge(instance, method, problem)
        } catch (e: Exception) {
            LOG.error(e.message, e)
            return JudgeResult.runtimeError(e.message)
        }

    }

    private fun readInternalTestCases(problem: Problem): Array<InternalTestCase> {
        val testCases = problem.testCases
        val function = problem.function

        val internalTestCases = testCases.indices
                .map { InternalTestCase(testCases[it], function) }
                .toTypedArray()


        shuffle(internalTestCases)
        return internalTestCases
    }

    private fun shuffle(array: Array<InternalTestCase>) {
        val count = array.size

        for (i in count downTo 2) {
            swap(array, i - 1, RANDOM.nextInt(i))
        }
    }

    private fun swap(array: Array<InternalTestCase>, i: Int, j: Int) {
        val temp = array[i]
        array[i] = array[j]
        array[j] = temp
    }

    private class PerformanceSnapshot internal constructor(internal val currentNanoTime: Long, internal val usedMemoryInBytes: Long) {
        companion object {
            internal fun create(timeNanoSeconds: Long, memoryBytes: Long): PerformanceSnapshot {
                return PerformanceSnapshot(timeNanoSeconds, memoryBytes)
            }
        }
    }

    private class PerformanceResult private constructor(internal val usedMemoryInBytes: Long, internal val usedTimeInMs: Double) {

        internal fun chooseBestResults(performanceResult: PerformanceResult): PerformanceResult {
            return PerformanceResult(
                    Math.min(performanceResult.usedMemoryInBytes, usedMemoryInBytes),
                    Math.min(performanceResult.usedTimeInMs, usedTimeInMs)
            )
        }

        companion object {

            internal fun create(before: PerformanceSnapshot, after: PerformanceSnapshot): PerformanceResult {
                return PerformanceResult(
                        usedMemoryInBytes(before, after),
                        usedTimeInMs(before, after)
                )
            }

            private fun usedTimeInMs(before: PerformanceSnapshot, after: PerformanceSnapshot): Double {
                return (after.currentNanoTime - before.currentNanoTime) / (1000.0 * 1000.0)
            }

            private fun usedMemoryInBytes(before: PerformanceSnapshot, after: PerformanceSnapshot): Long {
                return after.usedMemoryInBytes - before.usedMemoryInBytes
            }
        }
    }

    private class JudgePerformanceTask internal constructor(private val clazz: Any, private val method: Method, private val testCases: Array<InternalTestCase>) : Callable<PerformanceResult> {

        @Throws(Exception::class)
        override fun call(): PerformanceResult {
            val snapshotBeforeRun = takePerformanceSnapshot()

            JudgeTask(this.clazz, this.method, this.testCases).call()

            val snapshotAfterRun = takePerformanceSnapshot()

            return PerformanceResult.create(snapshotBeforeRun, snapshotAfterRun)
        }

        private fun takePerformanceSnapshot(): PerformanceSnapshot {
            val runtime = Runtime.getRuntime()

            return PerformanceSnapshot.create(
                    System.nanoTime(),
                    runtime.totalMemory() - runtime.freeMemory()
            )
        }
    }
}
