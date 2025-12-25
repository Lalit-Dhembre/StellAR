package com.cosmic_struck.stellar.common.di

import com.cosmic_struck.stellar.data.stellar.remote.StellARAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Provides
    @Singleton
    fun provideStellARAPI() : StellARAPI{
        return Retrofit
            .Builder()
            .baseUrl("http://192.168.1.3:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StellARAPI::class.java)
    }
}