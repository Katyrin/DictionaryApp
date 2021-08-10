package com.katyrin.repository.datasource

import com.katyrin.model.data.AppState
import com.katyrin.model.data.DataModel

interface DataSourceLocal<T> : DataSource<T> {
    suspend fun saveToDB(appState: AppState)
    suspend fun getDataByWord(word: String): DataModel
}