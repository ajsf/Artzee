package com.doublea.artzee.common.data

import android.content.SharedPreferences
import com.doublea.artzee.test.data.TestDataFactory.randomString
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SharedPrefsHelperTest {

    @Mock
    lateinit var mockPreferences: SharedPreferences

    @Mock
    lateinit var mockEditor: SharedPreferences.Editor

    private lateinit var prefsHelper: SharedPrefsHelper

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        prefsHelper = SharedPrefsHelper(mockPreferences)
    }

    @Test
    fun `when getCursor is called, it calls getString on the SharedPreferences`() {
        prefsHelper.getCursor()

        verify(mockPreferences).getString(any(), any())
    }

    @Test
    fun `when getCursor is called, it returns the string returned from SharedPreferences`() {
        val randomString = randomString()
        whenever(mockPreferences.getString(any(), any()))
                .thenReturn(randomString)

        val cursor = prefsHelper.getCursor()

        assertEquals(randomString, cursor)
    }

    @Test
    fun `when getCursor is called, if SharedPreferences returns null, a blank string is returned`() {
        whenever(mockPreferences.getString(any(), any()))
                .thenReturn(null)

        val cursor = prefsHelper.getCursor()

        assertEquals("", cursor)
    }

    @Test
    fun `when saveCursor is called, it calls edit on the SharedPreferences`() {
        val cursor = randomString()
        stubSaveString(cursor)

        prefsHelper.saveCursor(cursor)

        verify(mockPreferences).edit()
    }

    @Test
    fun `when saveCursor is called, it calls putString with the cursor on the Editor returned from SharedPreferences`() {
        val cursor = randomString()
        stubSaveString(cursor)

        prefsHelper.saveCursor(cursor)

        verify(mockEditor).putString(any(), eq(cursor))

    }

    @Test
    fun `when saveCursor is called, it calls apply on the Editor returned from SharedPreferences`() {
        val cursor = randomString()
        stubSaveString(cursor)

        prefsHelper.saveCursor(cursor)

        verify(mockEditor).apply()
    }

    private fun stubSaveString(string: String) {
        whenever(mockPreferences.edit())
                .thenReturn(mockEditor)

        whenever(mockEditor.putString(any(), eq(string)))
                .thenReturn(mockEditor)
    }
}