package com.doublea.artzee.commons.data.network

import com.doublea.artzee.BuildConfig
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private val apiBaseUrl = "https://api.artsy.net/api/"

val retrofit by lazy { makeRetrofitUnauthenticated() }
val clientBuilder by lazy { OkHttpClient.Builder() }

private fun makeRetrofitUnauthenticated() = makeRetrofit(makeHttpClient())

fun makeRetrofitAuthenticated(token: String) = makeRetrofit(makeAuthenticatedHttpClient(token))

private fun makeRetrofit(client: OkHttpClient) = Retrofit.Builder()
        .client(client)
        .baseUrl(apiBaseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

private fun makeHttpClient() = clientBuilder.build()

private fun makeAuthenticatedHttpClient(token: String) = clientBuilder
        .addInterceptor { chain ->
            val builder = chain.request()
                    .newBuilder()
                    .header("X-Xapp-Token", token)
            val request = builder.build()
            chain.proceed(request)
        }
        .authenticator { route, response ->
            val newToken = getAuthenticationToken()
            response
                    .request()
                    .newBuilder()
                    .header("X-Xapp-Token", newToken)
                    .build()
        }.build()

fun getAuthenticationToken(): String {
    val client = retrofit.create(ArtsyClient::class.java)
    var token = ""
    client.getToken(BuildConfig.ARTSY_CLIENT_ID, BuildConfig.ARTSY_CLIENT_SECRET)
            .subscribeBy { token = it.token }
    return token
}

