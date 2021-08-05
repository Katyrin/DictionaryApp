package com.katyrin.dictionaryapp.di

import android.content.Context
import com.katyrin.dictionaryapp.DictionaryApp
import com.katyrin.dictionaryapp.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        InteractorModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
        ActivityModule::class,
        AndroidSupportInjectionModule::class,
        NetworkModule::class
    ]
)
@Singleton
interface AppComponent : AndroidInjector<DictionaryApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withContext(context: Context): Builder

        fun build(): AppComponent
    }
}