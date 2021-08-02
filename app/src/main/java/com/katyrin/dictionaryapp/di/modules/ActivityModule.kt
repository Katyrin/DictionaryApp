package com.katyrin.dictionaryapp.di.modules

import com.katyrin.dictionaryapp.view.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}