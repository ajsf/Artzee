package com.doublea.artzee.common.db.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable

@Dao
interface ArtDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(artworks: List<ArtEntity>): Completable

    @Query("SELECT * FROM art")
    fun getAllArt(): DataSource.Factory<Int, ArtEntity>
}