package com.katyrin.dictionaryapp.data.networkstatus

interface NetworkState {
    suspend fun isOnline(): Boolean
}