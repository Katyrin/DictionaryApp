package com.katyrin.dictionaryapp.data.datasource

import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.data.network.ApiService

class RetrofitImplementation(
    private val apiService: ApiService
) : DataSource<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> = apiService.search(word)
}
