package org.algohub.engine.compile

import java.util.*
import java.util.regex.Pattern

internal class FindKotlinClassName {

    private val PATTERN_FOR_FINDING_CLASS_NAME = Pattern.compile("class\\s+(\\w+)\\s+")

    fun findIn(javaCode: String): Optional<String> {

        val matcher = PATTERN_FOR_FINDING_CLASS_NAME.matcher(javaCode)
        return if (matcher.find()) Optional.of(matcher.group(1)) else Optional.empty<String>()
    }
}
