package org.algohub.engine.codegeneration

import org.algohub.engine.judge.Function

interface JvmCodeGenerator {

    fun functionComment(function: Function): String {
        return """/**
${parametersComment(function)}
     * @return ${function.returnStatement.comment}
     */"""
    }

    fun parametersComment(function: Function): String {
        return function.parameters.map { parameter ->
            "     * @param ${parameter.name} ${parameter.comment}"
        }.joinToString(System.lineSeparator())
    }

    fun generateParameterDeclaration(type: String, parameterName: String) : String

    fun parametersOf(function: Function): String {
        return function.parameters.map { parameter ->
            generateParameterDeclaration(parameter.type, parameter.name)
        }.joinToString(", ")
    }
}
