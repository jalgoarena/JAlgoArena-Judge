package org.algohub.engine.codegeneration

import org.algohub.engine.judge.Function
import java.util.*

interface JvmCodeGenerator {
    fun functionComment(function: Function): String {
        val result = StringBuilder()
        result.append("/**\n")

        for (parameter in function.parameters) {
            result.append("     * @param ${parameter.name} ${parameter.comment}\n")
        }

        result.append("     * @return ${function.returnStatement.comment}\n")
        result.append("     */")

        return result.toString()
    }

    fun generateParameterDeclaration(type: String, parameterName: String) : String

    fun functionParameters(function: Function): StringJoiner {
        val parameters = StringJoiner(", ")

        function.parameters.forEach { parameter ->
            parameters.add(generateParameterDeclaration(parameter.type, parameter.name))
        }

        return parameters
    }
}