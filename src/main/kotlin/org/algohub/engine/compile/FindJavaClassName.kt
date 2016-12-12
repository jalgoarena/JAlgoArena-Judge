package org.algohub.engine.compile

import java.util.*
import java.util.regex.Pattern

internal class FindJavaClassName {

    fun findIn(javaCode: String): Optional<String> {
        for (pattern in PATTERNS_FOR_FINDING_CLASS_NAME) {
            val matcher = pattern.matcher(javaCode)
            if (matcher.find()) {
                return Optional.of(matcher.group(1))
            }
        }
        return Optional.empty<String>()
    }

    companion object {

        private val PATTERNS_FOR_FINDING_CLASS_NAME = arrayOf(
                Pattern.compile("public\\s+class\\s+(\\w+)\\s+"),
                Pattern.compile("final\\s+public\\s+class\\s+(\\w+)\\s+"),
                Pattern.compile("public\\s+final\\s+class\\s+(\\w+)\\s+")
        )
    }
}
