package com.doublea.artzee.common.db.room

import androidx.room.TypeConverter

class StringListTypeConverter {

    @TypeConverter
    fun stringToStringList(data: String): List<String> = data.split(",")

    @TypeConverter
    fun stringListToString(list: List<String>): String = list.joinToString(",")
}