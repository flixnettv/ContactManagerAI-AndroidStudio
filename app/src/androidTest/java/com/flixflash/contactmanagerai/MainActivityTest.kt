package com.flixflash.contactmanagerai

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun bottomNavigation_and_settingsNavigation() {
        // Verify bottom nav items exist
        composeRule.onNodeWithText("الرئيسية").assertExists()
        composeRule.onNodeWithText("الإعدادات").assertExists()
        // Navigate to settings
        composeRule.onNodeWithText("الإعدادات").performClick()
        // Expect settings screen title text
        composeRule.onNodeWithText("الإعدادات").assertExists()
    }
}