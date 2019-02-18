package com.doublea.artzee.common.db

import androidx.paging.DataSource
import com.doublea.artzee.common.data.ArtPagedListBuilder
import com.doublea.artzee.common.db.room.ArtDao
import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class ArtsyCache(
    private val artDao: ArtDao,
    private val mapper: Mapper<ArtEntity, Art>,
    private val pagedListBuilder: ArtPagedListBuilder
) {

    fun insert(artList: List<Art>): Completable = artDao.insert(artList
        .map { mapper.toDomain(it) })

    fun allArt(): DataSource.Factory<Int, Art> = artDao.getAllArt()
        .map { mapper.toModel(it) }

    fun getArtById(id: String): Single<Art> = artDao.getArtById(id)
        .map { mapper.toModel(it) }

    fun getArtFeed(disposable: CompositeDisposable) =
        pagedListBuilder.getPagedList(this, disposable)

}

