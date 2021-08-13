package com.katyrin.dictionaryapp.di

import androidx.room.Room
import com.katyrin.repository.datasource.RetrofitImplementation
import com.katyrin.repository.datasource.RoomDataBaseImplementation
import com.katyrin.historyscreen.interactor.HistoryInteractor
import com.katyrin.model.data.DataModel
import com.katyrin.repository.repository.Repository
import com.katyrin.repository.repository.RepositoryImplementation
import com.katyrin.repository.repository.RepositoryImplementationLocal
import com.katyrin.repository.storage.HistoryDataBase
import com.katyrin.dictionaryapp.viewmodel.MainViewModel
import com.katyrin.dictionaryapp.data.interactor.MainInteractor
import com.katyrin.repository.repository.RepositoryLocal
import com.katyrin.historyscreen.viewmodel.HistoryViewModel
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