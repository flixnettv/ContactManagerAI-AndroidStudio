package com.flixflash.contactmanagerai

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class SmsFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun navigate_to_sms_and_toggle_conversations() {
        // Go to messages tab
        composeRule.onNodeWithText("الرسائل").performClick()
        // Toggle conversations mode
        composeRule.onNodeWithText("المحادثات").assertExists()
        composeRule.onNodeWithText("المحادثات").performClick()
    }
}