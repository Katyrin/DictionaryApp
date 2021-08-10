package com.katyrin.dictionaryapp.data.datasource

import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.data.storage.HistoryDao
import com.katyrin.dictionaryapp.utils.convertDataModelSuccessToEntity
import com.katyrin.dictionaryapp.utils.mapHistoryEntityToDataModel
import com.katyrin.dictionaryapp.utils.mapHistoryEntityToSearchResult

class RoomDataBaseImplementation(
    private val historyDao: HistoryDao
) : DataSourceLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> =
        mapHistoryEntityToSearchResult(historyDao.all())

    override suspend fun saveToDB(appState: AppState) {
        convertDataModelSuccessToEntity(appState)?.let {
            historyDao.insert(it)
        }
    }

    override suspend fun getDataByWord(word: String): DataModel =
        mapHistoryEntityToDataModel(historyDao.getDataByWord(word))
}