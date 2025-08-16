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

data class BlockEntry(val number: String, val note: String?)

interface CallerIdApi {
	@GET("/health")
	suspend fun health(): Map<String, Any>

	@GET("/lookup/{number}")
	suspend fun lookup(@Path("number") number: String): NumberInfo

	@POST("/spam/report")
	suspend fun reportSpam(@Body body: SpamReportReq): Map<String, Any>

	@GET("/blocklist")
	suspend fun getBlocklist(): List<BlockEntry>

	@POST("/blocklist")
	suspend fun addBlock(@Body entry: BlockEntry): Map<String, Any>

	@DELETE("/blocklist/{number}")
	suspend fun removeBlock(@Path("number") number: String): Map<String, Any>
}