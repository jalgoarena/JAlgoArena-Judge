package com.jalgoarena.judge

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.io.Resources
import java.util.*

object BoggleDictionary {
    val INSTANCE: ArrayList<String> = ArrayList(jacksonObjectMapper().readValue(
            Resources.toString(Resources.getResource("dictionary.json"), Charsets.UTF_8),
            Array<String>::class.java
    ).asList())

    val KEY = "\"<ENGLISH-DICTIONARY>\""
}
