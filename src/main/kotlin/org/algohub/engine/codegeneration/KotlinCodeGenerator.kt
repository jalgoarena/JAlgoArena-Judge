package org.algohub.engine.codegeneration

import org.algohub.engine.judge.Function

internal object KotlinCodeGenerator : JvmCodeGenerator {

    private val CUSTOM_IMPORT = """import java.util.*
import org.algohub.engine.type.*

"""

    fun generateEmptyFunction(function: Function)=  "${CUSTOM_IMPORT}class Solution {\n${generateFunction(function)}}\n"

    private fun generateFunction(function: Function): String {
        val result = StringBuilder()

        functionComment(function, result)
        functionBody(function, result)

        implementationPlaceHolder(function, result)

        return result.toString()
    }

    private fun functionBody(function: Function, result: StringBuilder) {
        appendIndentation(result, "fun")
        result.append(" ").append(function.name).append("(")

        for (p in function.parameters) {
            result.append(generateParameterDeclaration(p.type, p.name))
                    .append(", ")
        }
        deleteLastUnnecessaryComma(result)
        result.append(")")
    }

    private fun generateParameterDeclaration(type: String, parameterName: String) =
            "$parameterName: ${generateKotlinTypeDeclaration(type)}"

    private fun implementationPlaceHolder(function: Function, result: StringBuilder) {
        result.append(": ${generateKotlinTypeDeclaration(function.returnStatement.type)}")
        result.append(" {\n")
        appendIndentation(result, "    // Write your code here\n")
        appendIndentation(result, "}\n")
    }

    private fun generateKotlinTypeDeclaration(type: String)=  if ("void" == type) "kotlin.Unit" else typeName(type)

    private fun typeName(type: String) = Class.forName(type).kotlin.simpleName!!
}