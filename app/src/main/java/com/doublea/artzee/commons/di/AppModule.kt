package com.doublea.artzee.commons.di

import android.content.Context
import com.doublea.artzee.AndroidApp
import com.doublea.artzee.commons.data.ArtRepository
import com.doublea.artzee.commons.data.ArtRepositoryImpl
import com.doublea.artzee.commons.data.network.ArtsyService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: AndroidApp): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideArtRepository(service: ArtsyService): ArtRepository = ArtRepositoryImpl(service)
}
