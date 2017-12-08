package com.doublea.artzee.commons.data.network

import io.reactivex.rxkotlin.subscribeBy
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by aaron on 12/7/17.
 */
class ArtRestApiTest {

    @Test
    fun `it returns art`() {
        val artRestApi = ArtRestApi()
        artRestApi.getArt()
                .subscribeBy { println(it) }
        //val response = artRestApi.getToken().execute()
        //val artsyToken = response.body()
        //assertNotNull(artsyToken?.token)
    }
}