package org.algohub.engine.codegeneration

import org.algohub.engine.judge.Function
import kotlin.reflect.KClass

internal object JavaCodeGenerator : JvmCodeGenerator {

    fun generateEmptyFunction(function: Function) = """import java.util.*;
import org.algohub.engine.type.*;

public class Solution {
    ${functionComment(function)}
    ${functionDeclaration(function)} {
        // Write your code here
    }
}
"""

    private fun generateJavaTypeDeclaration(type: String) =
            if ("void" == type) type else classOrPrimitiveName(type)

    private fun classOrPrimitiveName(type: String): String {

        fun typeNameOf(clazz: KClass<*>): String =
                clazz.javaPrimitiveType!!.simpleName!!

        val typeName = Class.forName(type).simpleName

        return when (typeName) {
            "Boolean" -> typeNameOf(Boolean::class)
            "Long" -> typeNameOf(Long::class)
            "Integer" -> typeNameOf(Int::class)
            "Short" -> typeNameOf(Short::class)
            "Double" -> typeNameOf(Double::class)
            "Float" -> typeNameOf(Float::class)
            else -> typeName
        }
    }

    private fun generateParameterDeclaration(type: String, parameterName: String) =
            "${generateJavaTypeDeclaration(type)} $parameterName"

    private fun functionDeclaration(function: Function): String {
        val result = StringBuilder()

        result.append("public ${generateJavaTypeDeclaration(function.returnStatement.type)} ${function.name}(")

        function.parameters.forEach { p ->
            result.append(generateParameterDeclaration(p.type, p.name))
                    .append(", ")
        }

        deleteLastUnnecessaryComma(result)
        result.append(")")

        return result.toString()
    }
}
