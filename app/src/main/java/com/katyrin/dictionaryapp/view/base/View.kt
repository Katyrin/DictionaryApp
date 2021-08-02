package com.katyrin.dictionaryapp.view.base

import com.katyrin.dictionaryapp.data.model.AppState

interface View {
    fun renderData(appState: AppState)
}