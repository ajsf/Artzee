package com.doublea.artzee.common.db

import androidx.paging.DataSource
import com.doublea.artzee.common.db.room.ArtDao
import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import java.util.concurrent.Executor

class ArtsyCache(
        private val artDao: ArtDao,
        private val ioExecutor: Executor,
        private val mapper: Mapper<ArtEntity, Art>
) {

    fun insert(artList: List<Art>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            artDao.insert(artList.map { mapper.toDomain(it) })
            insertFinished()
        }
    }

    fun allArt(): DataSource.Factory<Int, Art> = artDao
            .getAllArt()
            .map { mapper.toModel(it) }
}

