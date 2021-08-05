package com.katyrin.dictionaryapp.di.modules

import com.katyrin.dictionaryapp.data.datasource.*
import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.data.repository.Repository
import com.katyrin.dictionaryapp.data.repository.RepositoryImplementation
import com.katyrin.dictionaryapp.di.NAME_LOCAL
import com.katyrin.dictionaryapp.di.NAME_REMOTE
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    @Named(NAME_REMOTE)
    internal fun provideRepositoryRemote(@Named(NAME_REMOTE) dataSourceRemote: DataSource<List<DataModel>>): Repository<List<DataModel>> =
        RepositoryImplementation(dataSourceRemote)

    @Provides
    @Singleton
    @Named(NAME_LOCAL)
    internal fun provideRepositoryLocal(@Named(NAME_LOCAL) dataSourceLocal: DataSource<List<DataModel>>): Repository<List<DataModel>> =
        RepositoryImplementation(dataSourceLocal)

    @Provides
    @Singleton
    @Named(NAME_REMOTE)
    internal fun provideDataSourceRemote(apiService: ApiService): DataSource<List<DataModel>> =
        RetrofitImplementation(apiService)

    @Provides
    @Singleton
    @Named(NAME_LOCAL)
    internal fun provideDataSourceLocal(): DataSource<List<DataModel>> = RoomDataBaseImplementation()
}