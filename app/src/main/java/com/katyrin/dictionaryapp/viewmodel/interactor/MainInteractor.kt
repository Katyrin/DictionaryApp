package com.katyrin.dictionaryapp.viewmodel.interactor

import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.data.repository.Repository
import com.katyrin.dictionaryapp.di.NAME_LOCAL
import com.katyrin.dictionaryapp.di.NAME_REMOTE
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class MainInteractor @Inject constructor(
    @Named(NAME_REMOTE) val repositoryRemote: Repository<List<DataModel>>,
    @Named(NAME_LOCAL) val repositoryLocal: Repository<List<DataModel>>
) : Interactor<AppState> {

    override fun getData(word: String, fromRemoteSource: Boolean): Observable<AppState> =
        if (fromRemoteSource) repositoryRemote.getData(word).map { AppState.Success(it) }
        else repositoryLocal.getData(word).map { AppState.Success(it) }
}