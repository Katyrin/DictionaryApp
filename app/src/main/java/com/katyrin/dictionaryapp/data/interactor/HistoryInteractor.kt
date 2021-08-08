package com.katyrin.dictionaryapp.data.interactor

import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.data.repository.Repository
import com.katyrin.dictionaryapp.data.repository.RepositoryLocal

class HistoryInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState =
        AppState.Success(
            if (fromRemoteSource) {
                repositoryRemote
            } else {
                repositoryLocal
            }.getData(word)
        )
}