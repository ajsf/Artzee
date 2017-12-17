package com.doublea.artzee.commons.di

import android.app.Application
import com.doublea.artzee.commons.data.network.ArtsyService
import com.doublea.artzee.commons.data.network.AuthenticationInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    private val apiBaseUrl = "https:/api.artsy.net/api/"

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthenticationInterceptor {
        return AuthenticationInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(application: Application, authInterceptor: AuthenticationInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        val cacheDir = File(application.cacheDir, UUID.randomUUID().toString())
        val cache = Cache(cacheDir, 10 * 1024 * 1024)

        return OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(authInterceptor)
                .authenticator(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
    }

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient, authInterceptor: AuthenticationInterceptor): ArtsyService {
        val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(apiBaseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(ArtsyService::class.java)
        authInterceptor.service = retrofit
        return retrofit
    }
}