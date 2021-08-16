package com.katyrin.historyscreen

import com.katyrin.model.data.AppState
import com.katyrin.model.data.userdata.DataModel
import com.katyrin.model.data.userdata.Meaning

fun parseLocalSearchResults(appState: AppState): AppState =
    AppState.Success(mapResult(appState, false))

private fun mapResult(
    state: AppState,
    isOnline: Boolean
): List<DataModel> {
    val newSearchResults = arrayListOf<DataModel>()
    when (state) {
        is AppState.Success -> {
            getSuccessResultData(state, isOnline, newSearchResults)
        }
    }
    return newSearchResults
}

private fun getSuccessResultData(
    state: AppState.Success,
    isOnline: Boolean,
    newSearchResults: ArrayList<DataModel>
) {
    val searchResults: List<DataModel> = state.data as List<DataModel>
    if (searchResults.isNotEmpty()) {
        if (isOnline) {
            for (searchResult in searchResults) {
                parseOnlineResult(searchResult, newSearchResults)
            }
        } else {
            for (searchResult in searchResults) {
                newSearchResults.add(DataModel(searchResult.text, arrayListOf()))
            }
        }
    }
}

private fun parseOnlineResult(searchDataModel: DataModel, newSearchResults: ArrayList<DataModel>) {
    if (searchDataModel.text.isNotBlank() && searchDataModel.meanings.isNotEmpty()) {
        val newMeanings = arrayListOf<Meaning>()
        for (meaning in searchDataModel.meanings) {
            if (meaning.translatedMeaning.translatedMeaning.isBlank())
                newMeanings.add(Meaning(meaning.translatedMeaning, meaning.imageUrl))
        }
        if (newMeanings.isNotEmpty())
            newSearchResults.add(DataModel(searchDataModel.text, newMeanings))
    }
}
