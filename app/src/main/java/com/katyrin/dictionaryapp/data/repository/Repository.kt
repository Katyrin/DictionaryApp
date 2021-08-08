package com.katyrin.dictionaryapp.data.repository

interface Repository<T> {
    suspend fun getData(word: String): T
}