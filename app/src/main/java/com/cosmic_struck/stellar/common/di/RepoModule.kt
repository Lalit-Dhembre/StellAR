package com.cosmic_struck.stellar.common.di

import com.cosmic_struck.stellar.data.remote.StellARAPI
import com.cosmic_struck.stellar.data.repository.modelsRepo.ModelRepoImpl
import com.cosmic_struck.stellar.data.repository.modelsRepo.ModelsScreenRepo
import com.cosmic_struck.stellar.data.stellar.repository.modelsRepo.ScanImageRepo
import com.cosmic_struck.stellar.data.stellar.repository.modelsRepo.ScanImageRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideModelsScreenRepo(api: StellARAPI): ModelsScreenRepo {
        return ModelRepoImpl(api)
    }

    @Provides
    @Singleton
    fun provideImageScanRepo(api: StellARAPI): ScanImageRepo {
        return ScanImageRepoImpl(api)
    }

}