package com.doublea.artzee.commons.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


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
}