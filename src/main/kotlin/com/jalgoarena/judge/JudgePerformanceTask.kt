package com.jalgoarena.judge

import com.jalgoarena.judge.PerformanceSnapshot.Companion.takePerformanceSnapshot
import java.lang.reflect.Method
import java.util.concurrent.Callable

class JudgePerformanceTask(
        private val clazz: Any, val method: Method, val testCases: Array<InternalTestCase>
) : Callable<PerformanceResult> {

    override fun call(): PerformanceResult {
        val snapshotBeforeRun = takePerformanceSnapshot()

        JudgeTask(this.clazz, this.method, this.testCases, JudgeResultVerifier()).call()

        val snapshotAfterRun = takePerformanceSnapshot()

        return PerformanceResult.create(snapshotBeforeRun, snapshotAfterRun)
    }
}
