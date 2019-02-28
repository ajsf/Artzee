package com.doublea.artzee.common.db

import androidx.paging.DataSource
import com.doublea.artzee.common.data.ArtPagedListBuilder
import com.doublea.artzee.common.db.room.ArtDao
import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.db.room.ArtistDao
import com.doublea.artzee.common.db.room.ArtistEntity
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class ArtsyCache(
    private val artDao: ArtDao,
    private val artMapper: Mapper<ArtEntity, Art>,
    private val pagedListBuilder: ArtPagedListBuilder,
    private val artistDao: ArtistDao,
    private val artistMapper: Mapper<ArtistEntity, Artist>
) {

    fun insertArtworks(artList: List<Art>): Completable = artDao
        .insert(artList.map { artMapper.toDomain(it) })

    fun allArt(): DataSource.Factory<Int, Art> = artDao.getAllArt()
        .map { artMapper.toModel(it) }

    fun getArtById(id: String): Single<Art> = artDao.getArtById(id)
        .map { artMapper.toModel(it) }

    fun getArtFeed(disposable: CompositeDisposable) = pagedListBuilder
        .getPagedList(this, disposable)

    fun getArtistById(id: String): Single<Artist> = artistDao.getArtistById(id)
        .map { artistMapper.toModel(it) }

    fun insertArtist(artist: Artist, artId: String) {
        artistDao.insert(artistMapper.toDomain(artist))
        artDao.updateArtistIdForArt(artId, artist.id)
    }
}

