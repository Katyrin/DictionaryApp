package com.katyrin.utils.network

interface NetworkState {
    suspend fun isOnline(): Boolean
}