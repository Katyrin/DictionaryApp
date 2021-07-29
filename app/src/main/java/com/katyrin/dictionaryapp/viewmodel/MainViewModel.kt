package com.katyrin.dictionaryapp.viewmodel

import androidx.lifecycle.LiveData
import com.katyrin.dictionaryapp.data.datasource.DataSourceLocal
import com.katyrin.dictionaryapp.data.datasource.DataSourceRemote
import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.data.repository.RepositoryImplementation
import com.katyrin.dictionaryapp.viewmodel.interactor.MainInteractor
import io.reactivex.observers.DisposableObserver

class MainViewModel(
    private val interactor: MainInteractor = MainInteractor(
        RepositoryImplementation(DataSourceRemote()),
        RepositoryImplementation(DataSourceLocal())
    )
) : BaseViewModel<AppState>() {
    private var appState: AppState? = null

    override fun getData(word: String, isOnline: Boolean): LiveData<AppState> {
        compositeDisposable.add(
            interactor.getData(word, isOnline)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }

                .subscribeWith(getObserver())
        )
        return super.getData(word, isOnline)
    }

    private fun getObserver(): DisposableObserver<AppState> =
        object : DisposableObserver<AppState>() {

            override fun onNext(state: AppState) {
                appState = state
                liveDataForViewToObserve.value = state
            }

            override fun onError(e: Throwable) {
                liveDataForViewToObserve.value = AppState.Error(e)
            }

            override fun onComplete() {}
        }
}