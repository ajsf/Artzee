package com.doublea.artzee.common.db.room

import com.doublea.artzee.test.data.TestDataFactory.randomString
import junit.framework.Assert.assertEquals
import org.junit.Test

class StringListTypeConverterTest {

    @Test
    fun `it converts a string into a list of strings, with commas as the delimiter`() {
        val converter = StringListTypeConverter()

        val string1 = randomString()
        val string2 = randomString()
        val string3 = randomString()
        val string = "$string1,$string2,$string3"

        val list = converter.stringToStringList(string)

        val expectedList = listOf(string1, string2, string3)

        assertEquals(expectedList, list)
    }

    @Test
    fun `it converts a list of strings into one comma delimited string`() {
        val converter = StringListTypeConverter()

        val string1 = randomString()
        val string2 = randomString()
        val string3 = randomString()
        val list = listOf(string1, string2, string3)

        val string = converter.stringListToString(list)

        val expectedString = "$string1,$string2,$string3"

        assertEquals(expectedString, string)
    }
}