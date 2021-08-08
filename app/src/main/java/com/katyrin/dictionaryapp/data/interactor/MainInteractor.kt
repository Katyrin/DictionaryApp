package com.katyrin.dictionaryapp.data.interactor

import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.data.repository.Repository
import com.katyrin.dictionaryapp.data.repository.RepositoryLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState =
        AppState.Success(repositoryRemote.getData(word)).also { appState ->
            if (fromRemoteSource) repositoryLocal.saveToDB(appState)
        }

    override suspend fun getDataByWord(word: String): AppState =
        withContext(Dispatchers.IO) {
            AppState.Success(listOf(repositoryLocal.getDataByWord(word)))
        }
}