package com.jalgoarena.codegeneration

import com.jalgoarena.domain.Function

interface JvmCodeGeneration : JvmCodeGenerator {
    fun functionComment(function: Function): String {
        return """/**
${parametersComment(function)}
     * @return ${function.returnStatement.comment}
     */"""
    }

    fun parametersComment(function: Function): String {
        return function.parameters.joinToString(System.lineSeparator()) { parameter ->
            "     * @param ${parameter.name} ${parameter.comment}"
        }
    }

    fun generateParameterDeclaration(type: String, parameterName: String, generic: String?) : String

    fun parametersOf(function: Function): String {
        return function.parameters.joinToString(", ") { parameter ->
            generateParameterDeclaration(parameter.type, parameter.name, parameter.generic)
        }
    }

    fun typeGenericDeclaration(generic: String?) = when(generic) {
        null -> ""
        else -> "<$generic>"
    }
}
