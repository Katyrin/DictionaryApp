package com.katyrin.repository.repository

import com.katyrin.model.data.AppState
import com.katyrin.model.data.DataModel

interface RepositoryLocal<T> : Repository<T> {
    suspend fun saveToDB(appState: AppState)
    suspend fun getDataByWord(word: String): DataModel
}