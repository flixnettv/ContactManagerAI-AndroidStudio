package com.flixflash.contactmanagerai

import com.flixflash.contactmanagerai.data.network.CallerIdApi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CallerIdApiTest {
    private lateinit var server: MockWebServer
    private lateinit var api: CallerIdApi

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()
        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        api = retrofit.create(CallerIdApi::class.java)
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun health_ok() {
        server.enqueue(MockResponse().setBody("{\"ok\": true}").setResponseCode(200))
        val res = kotlinx.coroutines.runBlocking { api.health() }
        assertEquals(true, res["ok"])
    }
}