package com.katyrin.repository.datasource

interface DataSource<T> {
    suspend fun getData(word: String): T
}