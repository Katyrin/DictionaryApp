package com.katyrin.historyscreen.di

import com.katyrin.historyscreen.interactor.HistoryInteractor
import com.katyrin.historyscreen.viewmodel.HistoryViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun injectDependencies() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(listOf(historyScreen))
}

val historyScreen = module {
    factory { HistoryViewModel(get()) }
    factory { HistoryInteractor(get(), get()) }
}
