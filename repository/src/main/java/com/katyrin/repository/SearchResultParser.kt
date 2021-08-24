package com.katyrin.repository

import com.katyrin.model.data.AppState
import com.katyrin.model.data.dto.SearchResultDto
import com.katyrin.model.data.userdata.DataModel
import com.katyrin.model.data.userdata.Meaning
import com.katyrin.model.data.userdata.TranslatedMeaning
import com.katyrin.repository.storage.HistoryEntity

fun mapHistoryEntityToSearchResult(list: List<HistoryEntity>): List<SearchResultDto> =
    ArrayList<SearchResultDto>().apply {
        if (!list.isNullOrEmpty()) {
            for (entity in list) {
                add(SearchResultDto(entity.word, null))
            }
        }
    }

fun convertDataModelSuccessToEntity(appState: AppState): HistoryEntity? {
    return when (appState) {
        is AppState.Success -> {
            val searchResult = appState.data
            if (searchResult.isNullOrEmpty() || searchResult[0].text.isEmpty()) null
            else HistoryEntity(
                searchResult[0].text,
                searchResult[0].meanings[0].translatedMeaning.translatedMeaning,
                searchResult[0].meanings[0].imageUrl
            )
        }
        else -> null
    }
}

fun mapHistoryEntityToDataModel(historyEntity: HistoryEntity): DataModel =
    DataModel(
        historyEntity.word,
        listOf(
            Meaning(
                TranslatedMeaning(historyEntity.description ?: ""),
                historyEntity.imageUrl ?: ""
            )
        )
    )
