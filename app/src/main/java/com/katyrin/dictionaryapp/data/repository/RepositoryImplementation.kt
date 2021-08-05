package com.katyrin.dictionaryapp.data.repository

import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.data.datasource.DataSource
import io.reactivex.Observable
import javax.inject.Inject

class RepositoryImplementation @Inject constructor(
    private val dataSource: DataSource<List<DataModel>>
) : Repository<List<DataModel>> {
    override fun getData(word: String): Observable<List<DataModel>> = dataSource.getData(word)
}