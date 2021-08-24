package com.katyrin.dictionaryapp.data.interactor

import com.katyrin.core.interactor.Interactor
import com.katyrin.dictionaryapp.utils.mapSearchResultToResult
import com.katyrin.model.data.AppState
import com.katyrin.model.data.dto.SearchResultDto
import com.katyrin.repository.repository.Repository
import com.katyrin.repository.repository.RepositoryLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainInteractor(
    private val repositoryRemote: Repository<List<SearchResultDto>>,
    private val repositoryLocal: RepositoryLocal<List<SearchResultDto>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState =
        AppState.Success(
            mapSearchResultToResult(repositoryRemote.getData(word))
        ).also { appState ->
            if (fromRemoteSource) repositoryLocal.saveToDB(appState)
        }

    override suspend fun getDataByWord(word: String): AppState =
        withContext(Dispatchers.IO) {
            AppState.Success(listOf(repositoryLocal.getDataByWord(word)))
        }
}