package org.algohub.engine.codegeneration

import org.algohub.engine.judge.Function

internal object KotlinCodeGenerator : JvmCodeGenerator {

    fun generateEmptyFunction(function: Function) = """import java.util.*
import org.algohub.engine.type.*

class Solution {
    ${functionComment(function)}
    ${functionDeclaration(function)} {
        // Write your code here
    }
}
"""

    override fun generateParameterDeclaration(type: String, parameterName: String) =
            "$parameterName${kotlinTypeDeclaration(type)}"

    private fun functionDeclaration(function: Function): String =
            "fun ${function.name}(${parametersOf(function)})${kotlinTypeDeclaration(function.returnStatement.type)}"

    private fun kotlinTypeDeclaration(type: String) = when(type) {
        "void" -> ""
        else -> ": ${Class.forName(type).kotlin.simpleName!!}"
    }
}
