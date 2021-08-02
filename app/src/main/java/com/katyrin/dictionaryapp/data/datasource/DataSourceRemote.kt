package com.katyrin.dictionaryapp.data.datasource

import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.di.NAME_REMOTE
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class DataSourceRemote @Inject constructor(
    @Named(NAME_REMOTE) private val retrofitImplementation : DataSource<List<DataModel>>
) : DataSource<List<DataModel>> {
    override fun getData(word: String): Observable<List<DataModel>> =
        retrofitImplementation.getData(word)
}