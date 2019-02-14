package com.doublea.artzee.commons.db

import androidx.paging.DataSource
import com.doublea.artzee.commons.db.room.ArtDao
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.models.toArt
import com.doublea.artzee.commons.data.models.toArtEntity
import java.util.concurrent.Executor

class ArtsyCache(
        private val artDao: ArtDao,
        private val ioExecutor: Executor
) {

    fun insert(artList: List<Art>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            artDao.insert(artList.map { it.toArtEntity() })
            insertFinished()
        }
    }

    fun allArt(): DataSource.Factory<Int, Art> = artDao.getAllArt().map { it.toArt() }

}

