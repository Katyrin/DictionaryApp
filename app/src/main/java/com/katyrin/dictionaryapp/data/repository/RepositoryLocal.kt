package com.katyrin.dictionaryapp.data.repository

import com.katyrin.dictionaryapp.data.model.AppState

interface RepositoryLocal<T> : Repository<T> {
    suspend fun saveToDB(appState: AppState)
}