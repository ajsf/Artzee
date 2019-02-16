package com.doublea.artzee.common.network.retrofit

import com.doublea.artzee.BuildConfig
import com.doublea.artzee.common.network.ArtsyToken
import com.doublea.artzee.test.data.TestDataFactory.randomString
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

private const val TOKEN_KEY = "X-Xapp-Token"

internal class AuthenticationInterceptorTest {

    @Mock
    lateinit var mockService: ArtsyService

    private lateinit var interceptor: AuthenticationInterceptor

    private lateinit var mockServer: MockWebServer

    private lateinit var client: OkHttpClient

    private lateinit var requestBuilder: Request.Builder

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        interceptor = AuthenticationInterceptor()
        interceptor.service = mockService

        mockServer = MockWebServer()
        mockServer.start()
        mockServer.enqueue(MockResponse())

        requestBuilder = Request.Builder().url(mockServer.url("/"))

        client = OkHttpClient().newBuilder()
                .addInterceptor(interceptor).build()
    }

    @After
    fun shutdown() {
        mockServer.shutdown()
    }

    @Test
    fun `it calls getToken on the service with the correct Artsy client ID and secret when a request is made`() {
        stubTokenSuccess()
        val clientId = BuildConfig.ARTSY_CLIENT_ID
        val clientSecret = BuildConfig.ARTSY_CLIENT_SECRET
        client.newCall(requestBuilder.build()).execute()
        verify(mockService).getToken(clientId, clientSecret)
    }

    @Test
    fun `when the service returns a token, it adds the token to the header with key TOKEN_KEY`() {
        val mockToken = stubTokenSuccess()

        client.newCall(requestBuilder.build()).execute()

        val request = mockServer.takeRequest()

        assertEquals(mockToken, request.getHeader(TOKEN_KEY))
    }

    @Test
    fun `when the service returns an error, the TOKEN_KEY is not present`() {
        stubTokenFailure()

        client.newCall(requestBuilder.build()).execute()

        val request = mockServer.takeRequest()

        assertEquals("", request.getHeader(TOKEN_KEY))
    }

    @Test
    fun `when multiple requests are made, it caches the token and only calls getToken on the service once`() {
        val mockToken = stubTokenSuccess()

        client.newCall(requestBuilder.build()).execute()
        val requestOne = mockServer.takeRequest()

        assertEquals(mockToken, requestOne.getHeader(TOKEN_KEY))
        verify(mockService, times(1)).getToken(any(), any())

        mockServer.enqueue(MockResponse())
        client.newCall(requestBuilder.build()).execute()

        val requestTwo = mockServer.takeRequest()

        assertEquals(mockToken, requestTwo.getHeader(TOKEN_KEY))
        verify(mockService, times(1)).getToken(any(), any())
    }


    private fun stubTokenSuccess(): String {
        val mockToken = ArtsyToken(randomString(), randomString(), randomString())
        whenever(mockService.getToken(any(), any()))
                .thenReturn(Single.just(mockToken))
        return mockToken.token
    }

    private fun stubTokenFailure() {
        whenever(mockService.getToken(any(), any()))
                .thenReturn(Single.error(Throwable()))
    }
}