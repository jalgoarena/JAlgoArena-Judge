package org.algohub.engine.codegeneration

import org.algohub.engine.judge.Function
import java.util.*

interface JvmCodeGenerator {
    fun functionComment(function: Function): String {
        return """/**
${parametersComment(function)}
     * @return ${function.returnStatement.comment}
     */"""
    }

    fun parametersComment(function: Function): String {

        val parametersComment = StringJoiner("\n")

        for (parameter in function.parameters) {
            parametersComment.add("     * @param ${parameter.name} ${parameter.comment}")
        }

        return parametersComment.toString()
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