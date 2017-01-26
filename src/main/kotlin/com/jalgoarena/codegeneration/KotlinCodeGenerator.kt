package com.jalgoarena.codegeneration

import com.jalgoarena.domain.Function

class KotlinCodeGenerator : JvmCodeGeneration {
    override fun programmingLanguage() = "kotlin"

    override fun generateEmptyFunction(function: Function) = """import java.util.*
import com.jalgoarena.type.*

class Solution {
    ${functionComment(function)}
    ${functionDeclaration(function)} {
        // Write your code here
    }
}
"""

    override fun generateParameterDeclaration(type: String, parameterName: String, generic: String?) =
            "$parameterName${kotlinTypeDeclaration(type, generic)}"

    private fun functionDeclaration(function: Function): String =
            "fun ${function.name}(${parametersOf(function)})${kotlinTypeDeclaration(function.returnStatement.type, function.returnStatement.generic)}"

    private fun kotlinTypeDeclaration(type: String, generic: String?) = when(type) {
        "void" -> ""
        "[[I" -> ": Array<IntArray>"
        "[[C" -> ": Array<CharArray>"
        else -> ": ${Class.forName(type).kotlin.simpleName!!}${typeGenericDeclaration(generic)}"
    }
}
