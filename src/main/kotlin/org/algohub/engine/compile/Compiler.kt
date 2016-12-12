package org.algohub.engine.compile

interface Compiler {
    fun run(className: String, source: String): MutableMap<String, ByteArray?>?
}