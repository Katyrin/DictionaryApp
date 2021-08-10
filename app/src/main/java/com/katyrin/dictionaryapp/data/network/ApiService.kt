package com.katyrin.dictionaryapp.data.network

import com.katyrin.dictionaryapp.data.model.DataModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("words/search")
    suspend fun search(@Query("search") wordToSearch: String): List<DataModel>
}