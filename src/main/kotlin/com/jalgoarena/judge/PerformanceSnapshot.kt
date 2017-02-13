package com.jalgoarena.judge

class PerformanceSnapshot(val currentNanoTime: Long, val usedMemoryInBytes: Long) {
    companion object {
        private val runtime = Runtime.getRuntime()

        fun takePerformanceSnapshot(): PerformanceSnapshot {

            val usedMemoryInBytes = runtime.totalMemory() - runtime.freeMemory()

            return PerformanceSnapshot(
                    System.nanoTime(),
                    usedMemoryInBytes
            )
        }
    }
}
