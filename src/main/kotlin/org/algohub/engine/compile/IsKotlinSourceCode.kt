package org.algohub.engine.compile

import org.algohub.engine.judge.Function
import java.util.regex.Pattern

internal class IsKotlinSourceCode {
    fun findIn(sourceCode: String, function: Function): Boolean {
        val methodName = function.name
        val pattern = Pattern.compile("fun\\s+$methodName\\s*")
        return pattern.matcher(sourceCode).find()
    }
}