package com.jalgoarena.codegeneration

import com.jalgoarena.judge.Function
import kotlin.reflect.KClass

internal object JavaCodeGenerator : JvmCodeGenerator {

    fun generateEmptyFunction(function: Function) = """import java.util.*;
import com.jalgoarena.type.*;

public class Solution {
    ${functionComment(function)}
    ${functionDeclaration(function)} {
        // Write your code here
    }
}
"""

    override fun generateParameterDeclaration(type: String, parameterName: String) =
            "${javaTypeDeclaration(type)} $parameterName"

    private fun javaTypeDeclaration(type: String) = when (type) {
        "void" -> type
        else -> classOrPrimitiveName(type)
    }

    private fun classOrPrimitiveName(type: String): String {

        val typeName = Class.forName(type).simpleName

        return when (typeName) {
            "Boolean" -> Boolean::class.typeName()
            "Long" -> Long::class.typeName()
            "Integer" -> Int::class.typeName()
            "Short" -> Short::class.typeName()
            "Double" -> Double::class.typeName()
            "Float" -> Float::class.typeName()
            else -> typeName
        }
    }

    private fun functionDeclaration(function: Function): String =
            "public ${javaTypeDeclaration(function.returnStatement.type)} ${function.name}(${parametersOf(function)})"

    private fun KClass<*>.typeName() = this.javaPrimitiveType!!.simpleName!!
}
