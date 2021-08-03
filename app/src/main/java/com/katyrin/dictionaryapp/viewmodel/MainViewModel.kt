package com.katyrin.dictionaryapp.viewmodel

import androidx.lifecycle.LiveData
import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.viewmodel.interactor.MainInteractor
import io.reactivex.observers.DisposableObserver

class MainViewModel(
    private val interactor: MainInteractor
) : BaseViewModel<AppState>() {

    private var appState: AppState? = null
    private val liveDataForViewToObserve: LiveData<AppState> = _mutableLiveData

    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
    }

    override fun getData(word: String, isOnline: Boolean) {
        compositeDisposable.add(
            interactor.getData(word, isOnline)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { _mutableLiveData.value = AppState.Loading(null) }

                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableObserver<AppState> =
        object : DisposableObserver<AppState>() {

            override fun onNext(state: AppState) {
                appState = state
                _mutableLiveData.value = state
            }

            override fun onError(e: Throwable) {
                _mutableLiveData.value = AppState.Error(e)
            }

            override fun onComplete() {}
        }
}