package org.d3if0021.reminderpartner.ui.screen.main

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.d3if0021.reminderpartner.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testDeleteActivity() {
        // Gulir ke aktivitas pertama jika tidak terlihat
        composeTestRule
            .onNodeWithText("Nama Aktivitas Pertama")
            .performScrollTo()

        // Geser aktivitas pertama ke kiri untuk menghapus
        composeTestRule
            .onNodeWithText("Nama Aktivitas Pertama")
            .performTouchInput {
                swipeLeft()
            }

        // Verifikasi aktivitas tidak ada lagi
        composeTestRule
            .onNodeWithText("Nama Aktivitas Pertama")
            .assertDoesNotExist()
    }
    @Test
    fun testAddActivity() {
        // Klik tombol FAB
        composeTestRule
            .onNodeWithContentDescription("Tambah Aktivitas")
            .performClick()

        // Masukkan nama aktivitas baru
        composeTestRule
            .onNodeWithContentDescription("Input Nama Aktivitas")
            .performTextInput("Aktivitas Baru")

        // Klik tombol simpan
        composeTestRule
            .onNodeWithContentDescription("Simpan Aktivitas")
            .performClick()

        // Verifikasi aktivitas baru muncul di daftar
        composeTestRule
            .onNodeWithText("Aktivitas Baru")
            .assertIsDisplayed()
    }
}
