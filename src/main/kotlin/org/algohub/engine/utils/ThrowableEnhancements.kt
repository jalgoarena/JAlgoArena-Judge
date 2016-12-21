package org.algohub.engine.utils

interface ThrowableEnhancements {
    fun getCause(e: Throwable): Throwable {
        var cause: Throwable? = e.cause
        var result = e

        while (null != cause && result !== cause) {
            result = cause
            cause = result.cause
        }
        return result
    }
}