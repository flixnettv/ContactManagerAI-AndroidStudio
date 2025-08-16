package com.flixflash.contactmanagerai.data.repository

import com.flixflash.contactmanagerai.data.network.CallerIdApi
import com.flixflash.contactmanagerai.data.network.NumberInfo
import com.flixflash.contactmanagerai.data.network.SpamReportReq
import com.flixflash.contactmanagerai.data.network.BlockEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallerIdRepository @Inject constructor(
	private val api: CallerIdApi
) {
	suspend fun lookup(number: String): NumberInfo = api.lookup(number)
	suspend fun reportSpam(number: String, reason: String) { api.reportSpam(SpamReportReq(number, reason)) }
	suspend fun getBlocklist(): List<BlockEntry> = api.getBlocklist()
	suspend fun addBlock(number: String, note: String?) { api.addBlock(BlockEntry(number, note)) }
	suspend fun removeBlock(number: String) { api.removeBlock(number) }
}