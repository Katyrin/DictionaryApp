package com.katyrin.dictionaryapp.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.katyrin.repository.network.ApiService
import com.katyrin.repository.network.BaseInterceptor
import com.katyrin.utils.network.NetworkState
import com.katyrin.utils.network.NetworkStateImpl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL_LOCATIONS = "https://dictionary.skyeng.ru/api/public/v1/"

val networkState = module {
    single { getNetworkState(context = get()) }
}

fun getNetworkState(context: Context): NetworkState = NetworkStateImpl(context)

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

