package com.katyrin.dictionaryapp.view.base

import androidx.appcompat.app.AppCompatActivity
import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.viewmodel.interactor.Interactor
import com.katyrin.dictionaryapp.viewmodel.BaseViewModel

abstract class BaseActivity<T : AppState, I : Interactor<T>> : AppCompatActivity() {
    abstract val viewModel: BaseViewModel<T>
    abstract fun renderData(dataModel: T)
}