package com.jalgoarena.codegeneration

import com.jalgoarena.domain.Function

interface JvmCodeGenerator {

    fun programmingLanguage(): String
    fun generateEmptyFunction(function: Function): String
}

