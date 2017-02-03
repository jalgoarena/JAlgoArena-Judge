package com.jalgoarena.judge

import com.fasterxml.jackson.databind.ObjectMapper
import com.jalgoarena.compile.*
import com.jalgoarena.domain.Function
import com.jalgoarena.domain.JudgeRequest
import com.jalgoarena.domain.JudgeResult
import com.jalgoarena.domain.JudgeResult.*
import com.jalgoarena.domain.Problem
import org.springframework.stereotype.Component
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.*
import javax.inject.Inject

@Component
open class JvmJudgeEngine(
        @Inject private val objectMapper: ObjectMapper,
        @Inject private val compilers: List<JvmCompiler>
) : JudgeEngine {

    private val NUMBER_OF_ITERATIONS = 5

    override fun judge(problem: Problem, judgeRequest: JudgeRequest): JudgeResult {

        val (sourceCode, userId, language) = judgeRequest

        val className = findClassName(language, sourceCode)

        if (!className.isPresent) {
            return CompileError("ClassNotFoundException: No public class found")
        }

        val compiler = compilers.first { it.programmingLanguage() == language }

        return compileAndJudge(className, compiler, problem.function!!, problem, judgeRequest.sourceCode)
    }

    private fun judge(clazz: Any, method: Method, problem: Problem): JudgeResult {

        val executorService = Executors.newSingleThreadExecutor()
        val testCases = readInternalTestCases(problem)
        val judge = executorService.submit(JudgeTask(clazz, method, testCases))

        return handleFutureRunExceptions {
            val results: List<Boolean> = coldRun(judge, problem)
            val failedTestCases = results.filter({ !it }).count()

            when {
                failedTestCases > 0 -> JudgeResult.WrongAnswer(results)
                else -> hotRun(clazz, method, problem, testCases)
            }
        }
    }

    private fun hotRun(clazz: Any, method: Method, problem: Problem, testCases: Array<InternalTestCase>) =
            runPerformanceEvaluation(clazz, method, problem, testCases)

    private fun coldRun(judge: Future<List<Boolean>>, problem: Problem) =
            judge.get(problem.timeLimit, TimeUnit.SECONDS)

    private fun runPerformanceEvaluation(
            clazz: Any, method: Method, problem: Problem, testCases: Array<InternalTestCase>
    ): JudgeResult {

        val executorService = Executors.newSingleThreadExecutor()
        var performanceResultFuture = evaluatePerformance(clazz, method, problem, executorService)
        var performanceResult = performanceResultFuture.get(problem.timeLimit, TimeUnit.SECONDS)

        for (i in 0..NUMBER_OF_ITERATIONS - 1) {
            performanceResultFuture = evaluatePerformance(clazz, method, problem, executorService)
            val nextPerformanceResult = performanceResultFuture.get(problem.timeLimit, TimeUnit.SECONDS)
            performanceResult = performanceResult chooseBetterComparingWith nextPerformanceResult
        }

        if (performanceResult.usedMemoryInBytes / 1024 > problem.memoryLimit) {
            return MemoryLimitExceeded(performanceResult.usedMemoryInBytes)
        }

        return Accepted(testCases.size, performanceResult.usedTimeInMs, performanceResult.usedMemoryInBytes)
    }

    private fun handleFutureRunExceptions(call: () -> JudgeResult) = try {
        call()
    } catch(e: Throwable) {
        when (e) {
            is InterruptedException, is ExecutionException -> RuntimeError(e.message)
            is TimeoutException -> TimeLimitExceeded()
            else -> RuntimeError(e.message)
        }
    }

    private fun evaluatePerformance(clazz: Any, method: Method, problem: Problem, executorService: ExecutorService) =
            executorService.submit(JudgePerformanceTask(clazz, method, readInternalTestCases(problem)))

    private fun compileAndJudge(
            className: Optional<String>,
            compiler: JvmCompiler,
            function: Function,
            problem: Problem,
            userCode: String
    ) = try {
        val (instance, method) = compiler.compileMethod(className.get(), function.name, function.parameters.size, userCode)
        judge(instance, method, problem)
    } catch (e: Throwable) {
        when (e) {
            is ClassNotFoundException -> CompileError("${e.javaClass} : ${e.message}")
            is CompileErrorException -> CompileError(CreateFriendlyMessage().from(e.message!!))
            is NoSuchMethodError -> CompileError("No such method: ${e.message}")
            else -> RuntimeError(e.message)
        }
    }

    private fun findClassName(language: String, sourceCode: String) = when (language) {
        "kotlin" -> sourceCode.findKotlinClassName()
        else -> sourceCode.findJavaClassName()
    }

    private fun readInternalTestCases(problem: Problem): Array<InternalTestCase> {
        val testCases = problem.testCases
        val function = problem.function

        val parser = InternalTestCaseParser(function!!, objectMapper)

        val internalTestCases = testCases!!.indices
                .map { parser.parse(testCases[it]) }
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

        infix fun chooseBetterComparingWith(performanceResult: PerformanceResult): PerformanceResult {
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

    private class JudgePerformanceTask(
            val clazz: Any, val method: Method, val testCases: Array<InternalTestCase>
    ) : Callable<PerformanceResult> {

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

