package com.jalgoarena.judge

class PerformanceResult(val usedMemoryInBytes: Long, val usedTimeInMs: Double) {

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
