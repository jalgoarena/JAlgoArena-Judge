package com.jalgoarena.data

import com.jalgoarena.domain.Problem

interface ProblemsRepository {
    fun find(id: String): Problem?
    fun findAll(): List<Problem>
}
