package com.katyrin.dictionaryapp.data.datasource

import com.katyrin.dictionaryapp.data.model.DataModel
import io.reactivex.Observable

class RoomDataBaseImplementation : DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> {
        TODO("not implemented")
    }
}