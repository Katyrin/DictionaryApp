package com.katyrin.historyscreen.interactor

import com.katyrin.core.interactor.Interactor
import com.katyrin.dictionaryapp.utils.mapSearchResultToResult
import com.katyrin.model.data.AppState
import com.katyrin.model.data.dto.SearchResultDto
import com.katyrin.repository.repository.Repository
import com.katyrin.repository.repository.RepositoryLocal

class HistoryInteractor(
    private val repositoryRemote: Repository<List<SearchResultDto>>,
    private val repositoryLocal: RepositoryLocal<List<SearchResultDto>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState =
        AppState.Success(
            mapSearchResultToResult(
                if (fromRemoteSource) {
                    repositoryRemote
                } else {
                    repositoryLocal
                }.getData(word)
            )
        )

    override suspend fun getDataByWord(word: String): AppState {
        TODO("Not yet implemented")
    }
}