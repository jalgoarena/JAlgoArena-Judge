package org.algohub.engine.codegeneration

import org.algohub.engine.judge.Function

internal object KotlinCodeGenerator : JvmCodeGenerator {

    fun generateEmptyFunction(function: Function)= """import java.util.*
import org.algohub.engine.type.*

class Solution {
    ${functionComment(function)}
    ${functionDeclaration(function)} {
        // Write your code here
    }
}
"""

    override fun generateParameterDeclaration(type: String, parameterName: String) =
            "$parameterName${generateKotlinTypeDeclaration(type)}"

    private fun functionDeclaration(function: Function): String {
        val result = StringBuilder()
        result.append("fun ${function.name}(")

        val parameters = functionParameters(function)

        result.append(parameters.toString())
        result.append(")${generateKotlinTypeDeclaration(function.returnStatement.type)}")

        return result.toString()
    }

    private fun generateKotlinTypeDeclaration(type: String) =
            if ("void" == type) "" else ": ${typeName(type)}"

    private fun typeName(type: String) = Class.forName(type).kotlin.simpleName!!
}