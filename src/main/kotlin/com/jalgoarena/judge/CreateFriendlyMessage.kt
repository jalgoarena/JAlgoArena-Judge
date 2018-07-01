package com.jalgoarena.judge

import org.slf4j.LoggerFactory

internal class CreateFriendlyMessage {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val extension = ".java:"

    fun from(errorMessage: String): String {
        try {
            val lines = errorMessage
                    .split("\n")
                    .dropLastWhile(String::isEmpty)
                    .toTypedArray()

            val errorFriendlyMessage = StringBuilder()
            for (line in lines) {
                processLine(errorFriendlyMessage, line)
            }

            return errorFriendlyMessage.toString()
        } catch (e: Exception) {
            logger.warn("Cannot generate error friendly message", e)
            return errorMessage
        }
    }

    private fun processLine(sb: StringBuilder, line: String) {
        val pos = line.indexOf(extension)

        if (pos > 0) {
            appendFriendlyMessage(sb, line, pos)
        } else {
            sb.append(line).append('\n')
        }
    }

    private fun appendFriendlyMessage(errorFriendlyMessage: StringBuilder, line: String, pos: Int) {
        val pos2 = line.indexOf(':', pos + extension.length)
        val friendlyMessage = "Line:${getLineNumber(line, pos, pos2)}${line.substring(pos2)}"
        errorFriendlyMessage.append(friendlyMessage).append('\n')
    }

    private fun getLineNumber(line: String, pos: Int, pos2: Int): Int {
        val numberStr = line.substring(pos + extension.length, pos2)
        return Integer.valueOf(numberStr)
    }
}
