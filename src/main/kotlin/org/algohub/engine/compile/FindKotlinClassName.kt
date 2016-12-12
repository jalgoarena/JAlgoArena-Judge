package org.algohub.engine.compile

import java.util.*
import java.util.regex.Pattern

internal class FindKotlinClassName {

    fun findIn(javaCode: String): Optional<String> {

        val matcher = Companion.PATTERN_FOR_FINDING_CLASS_NAME.matcher(javaCode)
        if (matcher.find()) {
            return Optional.of(matcher.group(1))
        }

        return Optional.empty<String>()
    }

    companion object {
        private val PATTERN_FOR_FINDING_CLASS_NAME = Pattern.compile("class\\s+(\\w+)\\s+")
    }
}
