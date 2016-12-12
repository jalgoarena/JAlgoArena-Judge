package org.algohub.engine.compile

/**
 * Compile error exception - raised when source code is run against java compiler
 */
internal class CompileErrorException(message: String) : Exception(message)
