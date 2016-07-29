package org.algohub.engine.judge;

/**
 * Compile error exception - raised when source code is run against java compiler
 */
class CompileErrorException extends Exception {
    CompileErrorException(final String message) {
        super(message);
    }
}
