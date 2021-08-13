package com.katyrin.repository.datasource

import com.katyrin.repository.convertDataModelSuccessToEntity
import com.katyrin.repository.mapHistoryEntityToDataModel
import com.katyrin.repository.mapHistoryEntityToSearchResult
import com.katyrin.repository.storage.HistoryDao

class RoomDataBaseImplementation(
    private val historyDao: HistoryDao
) : DataSourceLocal<List<com.katyrin.model.data.DataModel>> {

    override suspend fun getData(word: String): List<com.katyrin.model.data.DataModel> =
        mapHistoryEntityToSearchResult(historyDao.all())

    override suspend fun saveToDB(appState: com.katyrin.model.data.AppState) {
        convertDataModelSuccessToEntity(appState)?.let {
            historyDao.insert(it)
        }
    }

    override suspend fun getDataByWord(word: String): com.katyrin.model.data.DataModel =
        mapHistoryEntityToDataModel(historyDao.getDataByWord(word))
}