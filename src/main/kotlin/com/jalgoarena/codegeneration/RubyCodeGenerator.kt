package com.jalgoarena.codegeneration

import com.jalgoarena.domain.Function

class RubyCodeGenerator : JvmCodeGeneration {
    override fun programmingLanguage() = "ruby"

    override fun generateEmptyFunction(function: Function) = """class Solution
${functionComment(function)}
  ${functionDeclaration(function)}
    # Write your code here
  end
end
"""

    override fun functionComment(function: Function): String {
        return """${parametersComment(function)}
  # @return ${function.returnStatement.comment}"""
    }

    override fun parametersComment(function: Function): String {
        return function.parameters.map { parameter ->
            "  # @param ${parameter.name} ${parameter.comment}"
        }.joinToString(System.lineSeparator())
    }

    override fun generateParameterDeclaration(type: String, parameterName: String, generic: String?) =
            "$parameterName"

    private fun functionDeclaration(function: Function): String =
            "def ${function.name}(${parametersOf(function)})"
}
