package com.katyrin.dictionaryapp.presenter

import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.view.base.View

interface Presenter<T : AppState, V : View> {
    fun attachView(view: V)
    fun detachView(view: V)
    fun getData(word: String, isOnline: Boolean)
}