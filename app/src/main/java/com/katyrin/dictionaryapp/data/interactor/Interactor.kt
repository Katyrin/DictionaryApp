package com.katyrin.dictionaryapp.data.interactor

interface Interactor<T> {
    suspend fun getData(word: String, fromRemoteSource: Boolean): T
    suspend fun getDataByWord(word: String): T
}