package com.katyrin.dictionaryapp.presenter.interactor

import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.data.repository.Repository
import io.reactivex.Observable

class MainInteractor(
    private val remoteRepository: Repository<List<DataModel>>,
    private val localRepository: Repository<List<DataModel>>
) : Interactor<AppState> {

    override fun getData(word: String, fromRemoteSource: Boolean): Observable<AppState> =
        if (fromRemoteSource) remoteRepository.getData(word).map { AppState.Success(it) }
        else localRepository.getData(word).map { AppState.Success(it) }
}