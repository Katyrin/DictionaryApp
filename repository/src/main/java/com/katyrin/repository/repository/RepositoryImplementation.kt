package com.katyrin.repository.repository

import com.katyrin.model.data.DataModel
import com.katyrin.repository.datasource.DataSource

class RepositoryImplementation(
    private val dataSource: DataSource<List<DataModel>>
) : Repository<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> = dataSource.getData(word)
}