package com.katyrin.dictionaryapp.di

import androidx.room.Room
import com.katyrin.dictionaryapp.data.interactor.MainInteractor
import com.katyrin.dictionaryapp.view.main.MainActivity
import com.katyrin.dictionaryapp.viewmodel.MainViewModel
import com.katyrin.model.data.dto.SearchResultDto
import com.katyrin.repository.datasource.RetrofitImplementation
import com.katyrin.repository.datasource.RoomDataBaseImplementation
import com.katyrin.repository.repository.Repository
import com.katyrin.repository.repository.RepositoryImplementation
import com.katyrin.repository.repository.RepositoryImplementationLocal
import com.katyrin.repository.repository.RepositoryLocal
import com.katyrin.repository.storage.HistoryDataBase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun injectDependencies() = loadModules

private val loadModules by lazy {
    loadKoinModules(listOf(application, mainScreen, network, networkState))
}

val application = module {
    single { Room.databaseBuilder(get(), HistoryDataBase::class.java, "HistoryDB").build() }
    single { get<HistoryDataBase>().historyDao() }
    single<Repository<List<SearchResultDto>>> { RepositoryImplementation(RetrofitImplementation(get())) }
    single<RepositoryLocal<List<SearchResultDto>>> {
        RepositoryImplementationLocal(RoomDataBaseImplementation(get()))
    }
}

val mainScreen = module {
    scope(named<MainActivity>()) {
        scoped { MainInteractor(get(), get()) }
        viewModel { MainViewModel(get()) }
    }
}