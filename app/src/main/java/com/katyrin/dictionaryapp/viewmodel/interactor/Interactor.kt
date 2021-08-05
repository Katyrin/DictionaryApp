package com.katyrin.dictionaryapp.viewmodel.interactor

interface Interactor<T> {
    suspend fun getData(word: String, fromRemoteSource: Boolean): T
}