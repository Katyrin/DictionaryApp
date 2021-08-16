package com.katyrin.repository.datasource

import com.katyrin.model.data.dto.SearchResultDto
import com.katyrin.repository.network.ApiService

class RetrofitImplementation(
    private val apiService: ApiService
) : DataSource<List<SearchResultDto>> {

    override suspend fun getData(word: String): List<SearchResultDto> = apiService.search(word)
}
