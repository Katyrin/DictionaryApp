package com.katyrin.dictionaryapp

import android.app.Application
import com.katyrin.dictionaryapp.di.application
import com.katyrin.dictionaryapp.di.mainScreen
import com.katyrin.dictionaryapp.di.network
import org.koin.core.context.startKoin

class DictionaryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(application, mainScreen, network))
        }
    }
}