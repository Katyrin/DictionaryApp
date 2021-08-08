package com.katyrin.dictionaryapp.data.repository

import com.katyrin.dictionaryapp.data.datasource.DataSourceLocal
import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.data.model.DataModel

class RepositoryImplementationLocal(
    private val dataSource: DataSourceLocal<List<DataModel>>
) : RepositoryLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> = dataSource.getData(word)

    override suspend fun saveToDB(appState: AppState) {
        dataSource.saveToDB(appState)
    }
}