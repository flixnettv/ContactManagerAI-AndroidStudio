package com.flixflash.contactmanagerai.data.network

import retrofit2.http.Body
import retrofit2.http.POST

data class SmsSpamRequest(val text: String)

data class SmsSpamResponse(val is_spam: Boolean, val score: Double)

interface SpamApi {
	@POST("/sms/spam")
	suspend fun classify(@Body body: SmsSpamRequest): SmsSpamResponse
}