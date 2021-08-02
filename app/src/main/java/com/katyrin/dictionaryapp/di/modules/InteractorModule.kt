package com.katyrin.dictionaryapp.di.modules

import com.katyrin.dictionaryapp.data.model.AppState
import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.data.repository.Repository
import com.katyrin.dictionaryapp.di.NAME_LOCAL
import com.katyrin.dictionaryapp.di.NAME_REMOTE
import com.katyrin.dictionaryapp.viewmodel.interactor.Interactor
import com.katyrin.dictionaryapp.viewmodel.interactor.MainInteractor
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class InteractorModule {

    @Provides
    internal fun provideInteractor(
        @Named(NAME_REMOTE) repositoryRemote: Repository<List<DataModel>>,
        @Named(NAME_LOCAL) repositoryLocal: Repository<List<DataModel>>
    ) = MainInteractor(repositoryRemote, repositoryLocal)
}