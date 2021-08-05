package com.katyrin.dictionaryapp.data.datasource

interface DataSource<T> {
    suspend fun getData(word: String): T
}