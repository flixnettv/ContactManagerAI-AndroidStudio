package com.flixflash.spamdetection

import org.junit.Assert.assertEquals
import org.junit.Test

class SpamHeuristicsTest {
	@Test
	fun `non-spam message has low score`() {
		val (isSpam, score) = classifyMessageTextLocal("Hello, see you at 5")
		assertEquals(false, isSpam)
		assert(score < 0.3f)
	}

	@Test
	fun `spammy message triggers spam`() {
		val (isSpam, score) = classifyMessageTextLocal("You won a free prize, click now!")
		assertEquals(true, isSpam)
		assert(score >= 0.3f)
	}
}