package com.jalgoarena.type

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Interval(@JvmField var start: Int, @JvmField var end: Int)
