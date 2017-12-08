package com.doublea.artzee.commons.data.network

import io.reactivex.Single

interface ArtApi {
    fun getArt(): Single<ArtsyResponse>
}