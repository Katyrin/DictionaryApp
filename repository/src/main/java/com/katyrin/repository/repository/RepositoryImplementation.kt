package com.katyrin.repository.repository

import com.katyrin.model.data.dto.SearchResultDto
import com.katyrin.repository.datasource.DataSource

class RepositoryImplementation(
    private val dataSource: DataSource<List<SearchResultDto>>
) : Repository<List<SearchResultDto>> {

    override suspend fun getData(word: String): List<SearchResultDto> = dataSource.getData(word)
}