package com.katyrin.repository

import com.katyrin.model.data.AppState
import com.katyrin.model.data.DataModel
import com.katyrin.repository.storage.HistoryEntity

fun mapHistoryEntityToSearchResult(list: List<HistoryEntity>): List<DataModel> =
    ArrayList<DataModel>().apply {
        if (!list.isNullOrEmpty()) {
            for (entity in list) {
                add(DataModel(entity.word, null))
            }
        }
    }

fun convertDataModelSuccessToEntity(appState: AppState): HistoryEntity? {
    return when (appState) {
        is AppState.Success -> {
            val searchResult = appState.data
            if (searchResult.isNullOrEmpty() || searchResult[0].text.isNullOrEmpty()) null
            else HistoryEntity(
                searchResult[0].text!!,
                searchResult[0].meanings?.get(0)?.translation?.translation,
                searchResult[0].meanings?.get(0)?.imageUrl
            )
        }
        else -> null
    }
}

fun mapHistoryEntityToDataModel(historyEntity: HistoryEntity): DataModel =
    DataModel(
        historyEntity.word,
        listOf(
            com.katyrin.model.data.Meanings(
                com.katyrin.model.data.Translation(historyEntity.description),
                historyEntity.imageUrl
            )
        )
    )
