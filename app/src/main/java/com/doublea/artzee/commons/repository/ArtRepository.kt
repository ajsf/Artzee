package com.doublea.artzee.commons.repository

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.models.Artist
import com.doublea.artzee.commons.data.network.ArtsyService
import com.doublea.artzee.commons.data.network.ArtworkDataSourceFactory
import com.doublea.artzee.commons.data.toArtist
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface ArtRepository {
    fun getArtistForArtwork(artworkId: String): Single<Artist>
    fun getArtFeed(disposable: CompositeDisposable): Flowable<PagedList<Art>>
}

class ArtRepositoryImpl(private val artsyService: ArtsyService = ArtsyService.getService()) : ArtRepository {

    private val pageSize = 10

    override fun getArtistForArtwork(artworkId: String): Single<Artist> = artsyService
            .getArtistsByArtworkId(artworkId)
            .map { it._embedded.artists.map { response -> response.toArtist() } }
            .map { it.first() }
            .subscribeOn(Schedulers.io())

    override fun getArtFeed(disposable: CompositeDisposable): Flowable<PagedList<Art>> {
        val sourceFactory = ArtworkDataSourceFactory(disposable, artsyService)

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 3)
                .setEnablePlaceholders(false)
                .build()

        return RxPagedListBuilder<String, Art>(sourceFactory, config)
                .buildFlowable(BackpressureStrategy.DROP)
    }
}