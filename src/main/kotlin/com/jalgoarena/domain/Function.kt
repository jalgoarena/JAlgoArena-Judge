package com.jalgoarena.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Function(val name: String,
               val returnStatement: Return,
               val parameters: List<Parameter>) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Return(val type: String, val comment: String, val generic: String? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Parameter(val name: String, val type: String, val comment: String, val generic: String? = null)
}
