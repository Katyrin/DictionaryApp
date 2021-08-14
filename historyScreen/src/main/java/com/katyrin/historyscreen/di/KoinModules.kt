package com.katyrin.historyscreen.di

import com.katyrin.historyscreen.interactor.HistoryInteractor
import com.katyrin.historyscreen.view.HistoryActivity
import com.katyrin.historyscreen.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun injectDependencies() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(listOf(historyScreen))
}

val historyScreen = module {
    scope(named<HistoryActivity>()) {
        scoped { HistoryInteractor(get(), get()) }
        viewModel { HistoryViewModel(get()) }
    }
}
