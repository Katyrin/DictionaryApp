package com.katyrin.dictionaryapp.data.datasource

import com.katyrin.dictionaryapp.data.model.DataModel
import io.reactivex.Observable

class RetrofitImplementation(
    private val apiService: ApiService
) : DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> = apiService.search(word)
}
