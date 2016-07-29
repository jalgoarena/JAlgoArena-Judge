package org.algohub.engine.compiler.java;

/**
 * Compile error exception - raised when source code is run against java compiler
 */
public class CompileErrorException extends Exception {
    CompileErrorException(final String message) {
        super(message);
    }
}
