package org.algohub.engine.judge

import java.util.*
import java.util.regex.Pattern

fun String.findJavaClassName(): Optional<String> {
    PATTERNS_FOR_FINDING_JAVA_CLASS_NAME
            .map { it.matcher(this) }
            .filter { it.find() }
            .forEach { return Optional.of(it.group(1)) }

    return Optional.empty()
}

fun String.findKotlinClassName(): Optional<String> {

    val matcher = PATTERN_FOR_FINDING_KOTLIN_CLASS_NAME.matcher(this)
    return if (matcher.find()) Optional.of(matcher.group(1)) else Optional.empty<String>()
}


private val PATTERNS_FOR_FINDING_JAVA_CLASS_NAME = arrayOf(
        Pattern.compile("public\\s+class\\s+(\\w+)\\s+"),
        Pattern.compile("final\\s+public\\s+class\\s+(\\w+)\\s+"),
        Pattern.compile("public\\s+final\\s+class\\s+(\\w+)\\s+")
)

private val PATTERN_FOR_FINDING_KOTLIN_CLASS_NAME =
        Pattern.compile("class\\s+(\\w+)\\s+")

