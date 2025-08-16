package com.flixflash.spamdetection

/**
 * Local heuristic spam classifier for SMS text
 * Returns Pair<isSpam, score>
 */
fun classifyMessageTextLocal(text: String): Pair<Boolean, Float> {
	val keywords = listOf(
		"win","prize","click","free","loan","credit","bitcoin","crypto",
		"ربحت","جائزة","اضغط","مجاني","قرض","بطاقة","بتكوين","عملات"
	)
	val lower = text.lowercase()
	var score = 0f
	for (kw in keywords) if (lower.contains(kw)) score += 0.15f
	score = score.coerceAtMost(1f)
	val isSpam = score >= 0.3f
	return isSpam to score
}