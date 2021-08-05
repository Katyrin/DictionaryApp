package com.katyrin.dictionaryapp.data.repository

import com.katyrin.dictionaryapp.data.datasource.DataSource
import com.katyrin.dictionaryapp.data.model.DataModel

class RepositoryImplementation(
    private val dataSource: DataSource<List<DataModel>>
) : Repository<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> = dataSource.getData(word)
}