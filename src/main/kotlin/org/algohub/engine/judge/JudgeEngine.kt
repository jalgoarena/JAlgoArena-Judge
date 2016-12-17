package org.algohub.engine.judge

import org.algohub.engine.compile.*
import org.slf4j.LoggerFactory
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.*

object JudgeEngine {

    private val LOG = LoggerFactory.getLogger(JudgeEngine::class.java)
    private val NUMBER_OF_ITERATIONS = 5

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

    fun judge(problem: Problem, userCode: String): JudgeResult {

        try {
            return compileAndJudge(problem, userCode)
        } catch (e: ClassNotFoundException) {
            LOG.warn("Class not found", e)
            return JudgeResult.compileError("${e.javaClass} : ${e.message}")
        } catch (e: CompileErrorException) {
            LOG.warn("Compilation error${e.message}")
            return JudgeResult.compileError(CreateFriendlyMessage().from(e.message!!))
        } catch (e: NoSuchMethodError) {
            LOG.warn("No such method error", e)
            return JudgeResult.compileError("No such method: ${e.message}")
        }

    }

    private fun compileAndJudge(problem: Problem, userCode: String): JudgeResult {

        val isKotlin = IsKotlinSourceCode().findIn(userCode, problem.function!!)
        val className = findClassName(isKotlin, userCode)

        if (!className.isPresent) {
            return JudgeResult.compileError("ClassNotFoundException: No public class found")
        }

        val compiler = if (isKotlin) KotlinCompiler() else MemoryJavaCompiler()

        val (instance, method) = compiler.compileMethod(
                className.get(),
                problem.function.name,
                userCode
        )

        try {
            return judge(instance, method, problem)
        } catch (e: Exception) {
            LOG.error(e.message, e)
            return JudgeResult.runtimeError(e.message)
        }

    }

    private fun findClassName(isKotlin: Boolean, userCode: String): Optional<String> = when (isKotlin) {
        true -> FindKotlinClassName().findIn(userCode)
        false -> FindJavaClassName().findIn(userCode)
    }

    private fun readInternalTestCases(problem: Problem): Array<InternalTestCase> {
        val testCases = problem.testCases
        val function = problem.function

        val internalTestCases = testCases!!.indices
                .map { InternalTestCase(testCases[it], function!!) }
                .toTypedArray()

        return shuffle(internalTestCases)
    }

    private fun shuffle(internalTestCases: Array<InternalTestCase>): Array<InternalTestCase> {
        val internalTestCasesAsList = Arrays.asList(*internalTestCases)
        Collections.shuffle(internalTestCasesAsList)
        return internalTestCasesAsList.toTypedArray()
    }


    private class PerformanceSnapshot(val currentNanoTime: Long, val usedMemoryInBytes: Long)

    private class PerformanceResult(val usedMemoryInBytes: Long, val usedTimeInMs: Double) {

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

    private class JudgePerformanceTask(val clazz: Any, val method: Method, val testCases: Array<InternalTestCase>) : Callable<PerformanceResult> {

        override fun call(): PerformanceResult {
            val snapshotBeforeRun = takePerformanceSnapshot()

            JudgeTask(this.clazz, this.method, this.testCases).call()

            val snapshotAfterRun = takePerformanceSnapshot()

            return PerformanceResult.create(snapshotBeforeRun, snapshotAfterRun)
        }

        private fun takePerformanceSnapshot(): PerformanceSnapshot {
            val runtime = Runtime.getRuntime()

            return PerformanceSnapshot(
                    System.nanoTime(),
                    runtime.totalMemory() - runtime.freeMemory()
            )
        }
    }
}
