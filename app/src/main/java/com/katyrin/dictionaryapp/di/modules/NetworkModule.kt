package com.katyrin.dictionaryapp.di.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.katyrin.dictionaryapp.data.datasource.ApiService
import com.katyrin.dictionaryapp.data.datasource.BaseInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideApiPost(gson: Gson, client: OkHttpClient): ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL_LOCATIONS)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client)
        .build().create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }.build()

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor = BaseInterceptor.interceptor

    private companion object {
        const val BASE_URL_LOCATIONS = "https://dictionary.skyeng.ru/api/public/v1/"
    }
}