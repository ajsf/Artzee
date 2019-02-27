package com.doublea.artzee.common.data

import android.content.SharedPreferences

interface PreferencesHelper {
    fun getCursor(): String
    fun saveCursor(cursor: String)
    fun firstRunTime(currentTime: Long): Long
}

class SharedPrefsHelper(private val prefs: SharedPreferences) : PreferencesHelper {

    private val cursorKey = "CURSOR"
    private val firstRunKey = "FIRST_RUN"

    override fun getCursor(): String = prefs
        .getString(cursorKey, "") ?: ""

    override fun saveCursor(cursor: String) = prefs.edit()
        .putString(cursorKey, cursor)
        .apply()

    override fun firstRunTime(currentTime: Long): Long {
        val time = prefs.getLong(firstRunKey, -1L)
        if (time == -1L) prefs.edit()
            .putLong(firstRunKey, currentTime)
            .apply()
        return time
    }

}