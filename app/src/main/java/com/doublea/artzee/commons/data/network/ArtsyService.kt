package com.doublea.artzee.commons.data.network

import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface ArtsyService {

    @POST("tokens/xapp_token")
    fun getToken(
            @Query("client_id") clientId: String,
            @Query("client_secret") clientSecret: String
    ): Single<ArtsyToken>

    @GET("artworks")
    fun getArt(
            @Query("size") size: Int = 10
    ): Single<ArtsyArtworkWrapper>

    @GET("artworks")
    fun getArtByCursor(
            @Query("cursor", encoded = true) cursor: String,
            @Query("size") size: Int = 10
    ): Single<ArtsyArtworkWrapper>

    @GET("artists")
    fun getArtists(
            @Query("size") size: Int = 10
    ): Single<ArtsyArtistsWrapper>

    @GET("artists")
    fun getArtistsByCursor(
            @Query("cursor", encoded = true) cursor: String,
            @Query("size") size: Int = 10
    ): Single<ArtsyArtistsWrapper>

    @GET("artists/{id}")
    fun getArtistById(
            @Path("id", encoded = true) id: String
    ): Single<ArtsyArtistResponse>

    companion object {
        private val apiBaseUrl = "https:/api.artsy.net/api/"

        private val retrofit by lazy {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            val authInterceptor = AuthenticationInterceptor()

            val client = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(authInterceptor)
                    .authenticator(authInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .build()

            val retrofit = Retrofit.Builder()
                    .client(client)
                    .baseUrl(apiBaseUrl)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(ArtsyService::class.java)
            authInterceptor.service = retrofit
            retrofit
        }

        fun getService(): ArtsyService = retrofit
    }
}