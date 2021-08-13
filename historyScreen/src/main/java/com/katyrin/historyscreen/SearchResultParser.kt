package com.katyrin.historyscreen

import com.katyrin.model.data.AppState
import com.katyrin.model.data.DataModel
import com.katyrin.model.data.Meanings

fun parseLocalSearchResults(appState: AppState): AppState {
    return AppState.Success(mapResult(appState, false))
}

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

private fun parseOnlineResult(searchResult: DataModel, newSearchResults: ArrayList<DataModel>) {
    if (!searchResult.text.isNullOrBlank() && !searchResult.meanings.isNullOrEmpty()) {
        val newMeanings = arrayListOf<Meanings>()
        for (meaning in searchResult.meanings!!) {
            if (meaning.translation != null && !meaning.translation!!.translation.isNullOrBlank())
                newMeanings.add(Meanings(meaning.translation, meaning.imageUrl))
        }
        if (newMeanings.isNotEmpty())
            newSearchResults.add(DataModel(searchResult.text, newMeanings))
    }
}
