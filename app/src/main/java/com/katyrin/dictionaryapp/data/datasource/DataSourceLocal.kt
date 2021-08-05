package com.katyrin.dictionaryapp.data.datasource

import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.di.NAME_LOCAL
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class DataSourceLocal @Inject constructor(
    @Named(NAME_LOCAL) private val remoteProvider: DataSource<List<DataModel>>
) : DataSource<List<DataModel>> {
    override fun getData(word: String): Observable<List<DataModel>> = remoteProvider.getData(word)
}