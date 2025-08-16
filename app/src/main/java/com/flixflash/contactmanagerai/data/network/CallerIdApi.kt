package com.flixflash.contactmanagerai.data.network

import retrofit2.http.*

data class NumberInfo(
	val number: String,
	val name: String?,
	val type: String?,
	val location: String?,
	val spam_score: Double
)

data class SpamReportReq(val number: String, val reason: String)

interface CallerIdApi {
	@GET("/health")
	suspend fun health(): Map<String, Any>

	@GET("/lookup/{number}")
	suspend fun lookup(@Path("number") number: String): NumberInfo

	@POST("/spam/report")
	suspend fun reportSpam(@Body body: SpamReportReq): Map<String, Any>
}