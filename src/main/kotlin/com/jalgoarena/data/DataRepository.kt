package com.jalgoarena.data

interface DataRepository<T> {
    fun find(id: String): T?
    fun findAll(): Array<T>
}
