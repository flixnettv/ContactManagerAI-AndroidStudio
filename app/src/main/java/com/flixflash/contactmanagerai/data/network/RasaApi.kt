package com.flixflash.contactmanagerai.data.network

import retrofit2.http.Body
import retrofit2.http.POST

interface RasaApi {
    @POST("/webhooks/rest/webhook")
    suspend fun sendMessage(@Body body: RasaMessageRequest): List<RasaMessageResponse>
}

data class RasaMessageRequest(
    val sender: String,
    val message: String
)

data class RasaMessageResponse(
    val text: String?
)