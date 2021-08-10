package com.katyrin.repository.datasource

import com.katyrin.model.data.DataModel
import com.katyrin.repository.network.ApiService

class RetrofitImplementation(
    private val apiService: ApiService
) : DataSource<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> = apiService.search(word)
}
