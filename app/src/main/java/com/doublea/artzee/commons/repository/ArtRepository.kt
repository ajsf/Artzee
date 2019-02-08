package com.doublea.artzee.commons.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.models.Artist
import com.doublea.artzee.commons.data.network.ArtsyService
import com.doublea.artzee.commons.data.network.ArtworkDataSourceFactory
import com.doublea.artzee.commons.data.toArtist
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface ArtRepository {
    fun getArtistForArtwork(artworkId: String): Single<Artist>
    fun getArtFeed(): LiveData<PagedList<Art>>
}

class ArtRepositoryImpl : ArtRepository {

    private val artsyService = ArtsyService.getService()

    private val pageSize = 10

    override fun getArtistForArtwork(artworkId: String): Single<Artist> = artsyService
            .getArtistsByArtworkId(artworkId)
            .map { it._embedded.artists.map { response -> response.toArtist() } }
            .map { it.first() }
            .subscribeOn(Schedulers.io())

    override fun getArtFeed(): LiveData<PagedList<Art>> {
        val sourceFactory = ArtworkDataSourceFactory(CompositeDisposable(), artsyService)
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 3)
                .setEnablePlaceholders(false)
                .build()
        return LivePagedListBuilder<String, Art>(sourceFactory, config).build()
    }
}