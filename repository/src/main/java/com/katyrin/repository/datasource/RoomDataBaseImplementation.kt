package com.katyrin.repository.datasource

import com.katyrin.model.data.dto.SearchResultDto
import com.katyrin.model.data.userdata.DataModel
import com.katyrin.repository.convertDataModelSuccessToEntity
import com.katyrin.repository.mapHistoryEntityToDataModel
import com.katyrin.repository.mapHistoryEntityToSearchResult
import com.katyrin.repository.storage.HistoryDao

class RoomDataBaseImplementation(
    private val historyDao: HistoryDao
) : DataSourceLocal<List<SearchResultDto>> {

    override suspend fun getData(word: String): List<SearchResultDto> =
        mapHistoryEntityToSearchResult(historyDao.all())

    override suspend fun saveToDB(appState: com.katyrin.model.data.AppState) {
        convertDataModelSuccessToEntity(appState)?.let {
            historyDao.insert(it)
        }
    }

    override suspend fun getDataByWord(word: String): DataModel =
        mapHistoryEntityToDataModel(historyDao.getDataByWord(word))
}