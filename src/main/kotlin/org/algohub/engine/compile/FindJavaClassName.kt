package org.algohub.engine.compile

import java.util.*
import java.util.regex.Pattern

internal class FindJavaClassName {

    fun findIn(javaCode: String): Optional<String> {
        PATTERNS_FOR_FINDING_CLASS_NAME
                .map { it.matcher(javaCode) }
                .filter { it.find() }
                .forEach { return Optional.of(it.group(1)) }

        return Optional.empty()
    }

    companion object {

        private val PATTERNS_FOR_FINDING_CLASS_NAME = arrayOf(
                Pattern.compile("public\\s+class\\s+(\\w+)\\s+"),
                Pattern.compile("final\\s+public\\s+class\\s+(\\w+)\\s+"),
                Pattern.compile("public\\s+final\\s+class\\s+(\\w+)\\s+")
        )
    }
}
