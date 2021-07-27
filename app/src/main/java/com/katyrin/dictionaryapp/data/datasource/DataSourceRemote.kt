package com.katyrin.dictionaryapp.data.datasource

import com.katyrin.dictionaryapp.data.model.DataModel
import io.reactivex.Observable

class DataSourceRemote(
    private val remoteProvider: RetrofitImplementation = RetrofitImplementation()
) : DataSource<List<DataModel>> {
    override fun getData(word: String): Observable<List<DataModel>> = remoteProvider.getData(word)
}