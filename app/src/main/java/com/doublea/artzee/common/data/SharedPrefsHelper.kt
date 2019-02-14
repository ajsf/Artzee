package com.doublea.artzee.common.data

import android.content.SharedPreferences

interface PreferencesHelper {
    fun getCursor(): String
    fun saveCursor(cursor: String)
}

class SharedPrefsHelper(private val prefs: SharedPreferences) : PreferencesHelper {

    private val cursorKey = "CURSOR"

    override fun getCursor(): String = prefs
            .getString(cursorKey, "") ?: ""

    override fun saveCursor(cursor: String) = prefs.edit()
            .putString(cursorKey, cursor)
            .apply()

}