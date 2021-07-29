package com.katyrin.dictionaryapp.viewmodel.interactor

import io.reactivex.Observable

interface Interactor<T> {
    fun getData(word: String, fromRemoteSource: Boolean): Observable<T>
}