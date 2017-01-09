package com.jalgoarena.compile

import com.jalgoarena.domain.Function
import java.util.regex.Pattern

internal class IsKotlinSourceCode {

    fun findIn(sourceCode: String, function: Function): Boolean {

        val pattern = Pattern.compile("fun\\s+${function.name}\\s*")
        return pattern.matcher(sourceCode).find()
    }
}
