package com.katyrin.historyscreen.interactor

import com.katyrin.model.data.AppState
import com.katyrin.model.data.DataModel
import com.katyrin.repository.repository.Repository
import com.katyrin.repository.repository.RepositoryLocal

class HistoryInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : com.katyrin.core.interactor.Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState =
        AppState.Success(
            if (fromRemoteSource) {
                repositoryRemote
            } else {
                repositoryLocal
            }.getData(word)
        )

    override suspend fun getDataByWord(word: String): AppState {
        TODO("Not yet implemented")
    }
}