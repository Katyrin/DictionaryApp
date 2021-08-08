package com.katyrin.dictionaryapp.di

import com.katyrin.dictionaryapp.data.datasource.RetrofitImplementation
import com.katyrin.dictionaryapp.data.datasource.RoomDataBaseImplementation
import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.data.repository.Repository
import com.katyrin.dictionaryapp.data.repository.RepositoryImplementation
import com.katyrin.dictionaryapp.viewmodel.MainViewModel
import com.katyrin.dictionaryapp.viewmodel.interactor.MainInteractor
import org.koin.core.qualifier.named
import org.koin.dsl.module

val application = module {
    single<Repository<List<DataModel>>>(named(NAME_REMOTE)) {
        RepositoryImplementation(
            RetrofitImplementation(apiService = get())
        )
    }
    single<Repository<List<DataModel>>>(named(NAME_LOCAL)) {
        RepositoryImplementation(
            RoomDataBaseImplementation()
        )
    }
}

val mainScreen = module {
    factory { MainInteractor(get(named(NAME_REMOTE)), get(named(NAME_LOCAL))) }
    factory { MainViewModel(get()) }
}