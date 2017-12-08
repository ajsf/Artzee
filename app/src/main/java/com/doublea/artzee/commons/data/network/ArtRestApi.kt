package com.doublea.artzee.commons.data.network

class ArtRestApi : ArtApi {

    private var authToken = getAuthenticationToken()
    private val artsyClient = makeRetrofitAuthenticated(authToken).create(ArtsyClient::class.java)

    override fun getArt() = artsyClient.getArt()
}
