package com.doublea.artzee.common.data

import android.content.SharedPreferences
import com.doublea.artzee.test.data.TestDataFactory
import com.doublea.artzee.test.data.TestDataFactory.randomString
import com.nhaarman.mockitokotlin2.*
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

    @Test
    fun `when firstRunTime is called, it calls getLong on shared preferences with a default value of -1`() {
        val randomTime = randomLong()

        prefsHelper.firstRunTime(randomTime)

        verify(mockPreferences).getLong(any(), eq(-1L))
    }

    @Test
    fun `when firstRunTime is called and no run time has been saved, it calls edit on shared preferences`() {
        val randomTime = randomLong()
        whenever(mockPreferences.getLong(any(), any())).thenReturn(-1L)
        stubSaveLong(randomTime)

        prefsHelper.firstRunTime(randomTime)

        verify(mockPreferences).edit()
    }

    @Test
    fun `when firstRunTime is called and no run time has been saved, it calls putLong with the time on the editor`() {
        val randomTime = randomLong()
        whenever(mockPreferences.getLong(any(), any())).thenReturn(-1L)
        stubSaveLong(randomTime)

        prefsHelper.firstRunTime(randomTime)

        verify(mockEditor).putLong(any(), eq(randomTime))
    }

    @Test
    fun `when firstRunTime is called and no run time has been saved, it calls apply on the editor`() {
        val randomTime = randomLong()
        whenever(mockPreferences.getLong(any(), any())).thenReturn(-1L)
        stubSaveLong(randomTime)

        prefsHelper.firstRunTime(randomTime)

        verify(mockEditor).apply()
    }

    @Test
    fun `when firstRunTime is called and no run time has been saved, it returns -1`() {
        val randomTime = randomLong()
        whenever(mockPreferences.getLong(any(), any())).thenReturn(-1L)
        stubSaveLong(randomTime)

        val time = prefsHelper.firstRunTime(randomTime)

        assertEquals(-1L, time)
    }

    @Test
    fun `when firstRunTime is called and a run time has been saved, it does not call edit on the shared preferences`() {
        whenever(mockPreferences.getLong(any(), any())).thenReturn(randomLong())

        prefsHelper.firstRunTime(randomLong())

        verify(mockPreferences, never()).edit()
    }

    @Test
    fun `when firstRunTime is called and a run time has been saved, it returns the saved time`() {
        val savedTime = randomLong()
        whenever(mockPreferences.getLong(any(), any())).thenReturn(savedTime)

        val time = prefsHelper.firstRunTime(randomLong())

        assertEquals(savedTime, time)
    }

    private fun stubSaveString(string: String) {
        whenever(mockPreferences.edit())
            .thenReturn(mockEditor)

        whenever(mockEditor.putString(any(), eq(string)))
            .thenReturn(mockEditor)
    }

    private fun stubSaveLong(long: Long) {
        whenever(mockPreferences.edit())
            .thenReturn(mockEditor)

        whenever(mockEditor.putLong(any(), eq(long)))
            .thenReturn(mockEditor)
    }

    private fun randomLong() = TestDataFactory.randomInt().toLong()
}