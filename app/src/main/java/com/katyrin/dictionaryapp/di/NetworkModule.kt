package com.katyrin.dictionaryapp.di

import com.google.gson.GsonBuilder
import com.katyrin.dictionaryapp.data.network.ApiService
import com.katyrin.dictionaryapp.data.network.BaseInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL_LOCATIONS = "https://dictionary.skyeng.ru/api/public/v1/"

val network = module {
    single { provideInterceptor() }
    factory { provideClient(interceptor = get()) }
    factory { provideApiPost(client = get()) }
}

fun provideApiPost(client: OkHttpClient): ApiService = Retrofit.Builder()
    .baseUrl(BASE_URL_LOCATIONS)
    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
    .client(client)
    .build().create(ApiService::class.java)

fun provideClient(interceptor: Interceptor): OkHttpClient =
    OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }.build()

fun provideInterceptor(): Interceptor = BaseInterceptor.interceptor

