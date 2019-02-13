package com.doublea.artzee.commons.data.network

import com.doublea.artzee.BuildConfig
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.*

class AuthenticationInterceptor : Interceptor, Authenticator {

    private var authToken: String = ""
    lateinit var service: ArtsyService

    override fun authenticate(route: Route, response: Response): Request {
        getAuthToken()
        return response.request().replaceAuthHeader()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (request.method() == "GET") {
            if (authToken.isBlank()) getAuthToken()
            request = request.replaceAuthHeader()
        }
        return chain.proceed(request)
    }

    private fun getAuthToken() {
        service.getToken(BuildConfig.ARTSY_CLIENT_ID, BuildConfig.ARTSY_CLIENT_SECRET)
                .subscribeBy(
                        onError = { println("ERROR GETTING AUTH TOKEN") },
                        onSuccess = {
                            authToken = it.token
                        }
                )
    }

    private fun Request.replaceAuthHeader() = this
            .newBuilder()
            .header("X-Xapp-Token", authToken).build()
}