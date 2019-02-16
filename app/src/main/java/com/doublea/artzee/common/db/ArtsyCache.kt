package com.doublea.artzee.common.db

import androidx.paging.DataSource
import com.doublea.artzee.common.db.room.ArtDao
import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import io.reactivex.Completable

class ArtsyCache(
        private val artDao: ArtDao,
        private val mapper: Mapper<ArtEntity, Art>
) {

    fun insert(artList: List<Art>): Completable = artDao.insert(artList
            .map { mapper.toDomain(it) })

    fun allArt(): DataSource.Factory<Int, Art> = artDao
            .getAllArt()
            .map { mapper.toModel(it) }
}

