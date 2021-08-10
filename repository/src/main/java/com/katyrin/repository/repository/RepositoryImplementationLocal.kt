package com.katyrin.repository.repository

import com.katyrin.model.data.AppState
import com.katyrin.model.data.DataModel
import com.katyrin.repository.datasource.DataSourceLocal

class RepositoryImplementationLocal(
    private val dataSource: DataSourceLocal<List<DataModel>>
) : RepositoryLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> = dataSource.getData(word)

    override suspend fun saveToDB(appState: AppState) {
        dataSource.saveToDB(appState)
    }

    override suspend fun getDataByWord(word: String): DataModel = dataSource.getDataByWord(word)
}