package com.katyrin.dictionaryapp.utils

import com.katyrin.model.data.AppState
import com.katyrin.model.data.dto.SearchResultDto
import com.katyrin.model.data.userdata.DataModel
import com.katyrin.model.data.userdata.Meaning
import com.katyrin.model.data.userdata.TranslatedMeaning

fun mapSearchResultToResult(searchResults: List<SearchResultDto>): List<DataModel> =
    searchResults.map { searchResult ->
        var meanings: List<Meaning> = listOf()
        searchResult.meanings?.let {
            meanings = it.map { meaningsDto ->
                Meaning(
                    TranslatedMeaning(meaningsDto?.translation?.translation ?: ""),
                    meaningsDto?.imageUrl ?: ""
                )
            }
        }
        DataModel(searchResult.text ?: "", meanings)
    }

fun parseSearchResults(data: AppState): AppState {
    val newSearchResults = arrayListOf<DataModel>()
    when (data) {
        is AppState.Success -> {
            val searchResults = data.data
            if (!searchResults.isNullOrEmpty()) {
                for (searchResult in searchResults) {
                    parseResult(searchResult, newSearchResults)
                }
            }
        }
    }

    return AppState.Success(newSearchResults)
}

private fun parseResult(dataModel: DataModel, newDataModels: ArrayList<DataModel>) {
    if (dataModel.text.isNotBlank() && dataModel.meanings.isNotEmpty()) {
        val newMeanings = arrayListOf<Meaning>()
        for (meaning in dataModel.meanings) {
            if (meaning.translatedMeaning.translatedMeaning.isNotBlank())
                newMeanings.add(Meaning(meaning.translatedMeaning, meaning.imageUrl))
        }
        if (newMeanings.isNotEmpty()) newDataModels.add(DataModel(dataModel.text, newMeanings))
    }
}

fun convertMeaningsToString(meanings: List<Meaning>): String {
    var meaningsSeparatedByComma = String()
    for ((index, meaning) in meanings.withIndex()) {
        meaningsSeparatedByComma += if (index + 1 != meanings.size) {
            String.format("%s%s", meaning.translatedMeaning.translatedMeaning, ", ")
        } else {
            meaning.translatedMeaning.translatedMeaning
        }
    }
    return meaningsSeparatedByComma
}