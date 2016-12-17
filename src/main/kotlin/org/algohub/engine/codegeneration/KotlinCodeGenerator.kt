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

    private fun functionDeclaration(function: Function): String {
        val result = StringBuilder()
        result.append("fun ${function.name}(")

        function.parameters.forEach { parameter ->
            result.append(generateParameterDeclaration(parameter.type, parameter.name))
                    .append(", ")
        }

        deleteLastUnnecessaryComma(result)
        result.append(")${generateKotlinTypeDeclaration(function.returnStatement.type)}")

        return result.toString()
    }

    private fun generateParameterDeclaration(type: String, parameterName: String) =
            "$parameterName${generateKotlinTypeDeclaration(type)}"

    private fun generateKotlinTypeDeclaration(type: String) =
            if ("void" == type) "" else ": ${typeName(type)}"

    private fun typeName(type: String) = Class.forName(type).kotlin.simpleName!!
}