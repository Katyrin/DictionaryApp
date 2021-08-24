package com.katyrin.repository.repository

import com.katyrin.model.data.AppState
import com.katyrin.model.data.dto.SearchResultDto
import com.katyrin.model.data.userdata.DataModel
import com.katyrin.repository.datasource.DataSourceLocal

class RepositoryImplementationLocal(
    private val dataSource: DataSourceLocal<List<SearchResultDto>>
) : RepositoryLocal<List<SearchResultDto>> {

    override suspend fun getData(word: String): List<SearchResultDto> = dataSource.getData(word)

    override suspend fun saveToDB(appState: AppState) {
        dataSource.saveToDB(appState)
    }

    override suspend fun getDataByWord(word: String): DataModel = dataSource.getDataByWord(word)
}