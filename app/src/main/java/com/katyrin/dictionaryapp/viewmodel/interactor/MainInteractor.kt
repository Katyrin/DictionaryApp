package com.katyrin.dictionaryapp.viewmodel.interactor

import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.data.repository.Repository
import io.reactivex.Observable

class MainInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: Repository<List<DataModel>>
) : Interactor<AppState> {

    override fun getData(word: String, fromRemoteSource: Boolean): Observable<AppState> =
        if (fromRemoteSource) repositoryRemote.getData(word).map { AppState.Success(it) }
        else repositoryLocal.getData(word).map { AppState.Success(it) }
}