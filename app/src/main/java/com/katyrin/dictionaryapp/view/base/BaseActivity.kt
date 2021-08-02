package com.katyrin.dictionaryapp.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.viewmodel.BaseViewModel
import com.katyrin.dictionaryapp.viewmodel.interactor.Interactor
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseActivity<T : AppState, I : Interactor<T>> : AppCompatActivity(),
    HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    abstract val viewModel: BaseViewModel<T>
    abstract fun renderData(dataModel: T)

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}