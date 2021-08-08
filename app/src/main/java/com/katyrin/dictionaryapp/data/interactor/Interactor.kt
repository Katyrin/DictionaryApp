package com.katyrin.dictionaryapp.data.interactor

interface Interactor<T> {
    suspend fun getData(word: String, fromRemoteSource: Boolean): T
}