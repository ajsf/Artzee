package com.doublea.artzee.commons.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ArtsyClient {

    @POST("tokens/xapp_token")
    fun getToken(
            @Query("client_id") clientId: String,
            @Query("client_secret") clientSecret: String
    ): Single<ArtsyToken>

    @GET("artworks")
    fun getArt(): Single<ArtsyResponse>
}