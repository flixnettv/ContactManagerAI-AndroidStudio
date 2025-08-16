package com.flixflash.contactmanagerai

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class CallerIdFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun navigate_to_caller_id_and_verify_ui() {
        // Go to settings
        composeRule.onNodeWithText("الإعدادات").performClick()
        // Open Caller ID screen
        composeRule.onNodeWithText("Caller ID").performClick()
        // Verify text field and buttons exist
        composeRule.onNodeWithText("رقم الهاتف").assertExists()
        composeRule.onNodeWithText("استعلام").assertExists()
        composeRule.onNodeWithText("تبليغ Spam").assertExists()
    }
}