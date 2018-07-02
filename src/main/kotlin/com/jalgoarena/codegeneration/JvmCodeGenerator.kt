package com.jalgoarena.codegeneration

import com.jalgoarena.domain.Function

interface JvmCodeGenerator {

    fun generateEmptyFunction(function: Function): String
}

