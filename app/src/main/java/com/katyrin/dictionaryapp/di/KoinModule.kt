package com.katyrin.dictionaryapp.di

import androidx.room.Room
import com.katyrin.dictionaryapp.data.datasource.RetrofitImplementation
import com.katyrin.dictionaryapp.data.datasource.RoomDataBaseImplementation
import com.katyrin.dictionaryapp.data.interactor.HistoryInteractor
import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.data.repository.Repository
import com.katyrin.dictionaryapp.data.repository.RepositoryImplementation
import com.katyrin.dictionaryapp.data.repository.RepositoryImplementationLocal
import com.katyrin.dictionaryapp.data.storage.HistoryDataBase
import com.katyrin.dictionaryapp.viewmodel.MainViewModel
import com.katyrin.dictionaryapp.data.interactor.MainInteractor
import com.katyrin.dictionaryapp.data.repository.RepositoryLocal
import com.katyrin.dictionaryapp.viewmodel.HistoryViewModel
import org.koin.dsl.module

val application = module {
    single { Room.databaseBuilder(get(), HistoryDataBase::class.java, "HistoryDB").build() }
    single { get<HistoryDataBase>().historyDao() }
    single<Repository<List<DataModel>>> { RepositoryImplementation(RetrofitImplementation(get())) }
    single<RepositoryLocal<List<DataModel>>> {
        RepositoryImplementationLocal(RoomDataBaseImplementation(get()))
    }
}

val mainScreen = module {
    factory { MainViewModel(get()) }
    factory { MainInteractor(get(), get()) }
}

val historyScreen = module {
    factory { HistoryViewModel(get()) }
    factory { HistoryInteractor(get(), get()) }
}