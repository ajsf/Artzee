package com.doublea.artzee.common.db.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(artist: ArtistEntity)

    @Query("SELECT * FROM artist")
    fun getAllArtists(): DataSource.Factory<Int, ArtistEntity>

    @Query("SELECT * FROM artist WHERE id = :idString")
    fun getArtistById(idString: String): Single<ArtistEntity>
}