package com.doublea.artzee.commons.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
        entities = [ArtEntity::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(StringListTypeConverter::class)
abstract class ArtDatabase : RoomDatabase() {

    abstract fun artDao(): ArtDao

    companion object {
        @Volatile
        private var INSTANCE: ArtDatabase? = null

        fun getInstance(context: Context): ArtDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext,
                ArtDatabase::class.java, "Artzee.db").build()
    }
}