package com.katyrin.dictionaryapp.data.datasource

import com.katyrin.dictionaryapp.data.model.AppState

interface DataSourceLocal<T> : DataSource<T> {
    suspend fun saveToDB(appState: AppState)
}