package com.doublea.artzee.commons.repository

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.doublea.artzee.commons.data.PreferencesHelper
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.models.Artist
import com.doublea.artzee.commons.data.network.ArtsyService
import com.doublea.artzee.commons.data.toArtist
import com.doublea.artzee.commons.db.ArtsyCache
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface ArtRepository {
    fun getArtistForArtwork(artworkId: String): Single<Artist>
    fun getArtFeed(disposable: CompositeDisposable): Flowable<PagedList<Art>>
}

class ArtRepositoryImpl(
        private val artsyService: ArtsyService,
        private val cache: ArtsyCache,
        private val prefs: PreferencesHelper
) : ArtRepository {

    private val pageSize = 5

    override fun getArtistForArtwork(artworkId: String): Single<Artist> = artsyService
            .getArtistsByArtworkId(artworkId)
            .map { it._embedded.artists.map { response -> response.toArtist() } }
            .map { it.first() }
            .subscribeOn(Schedulers.io())

    override fun getArtFeed(disposable: CompositeDisposable): Flowable<PagedList<Art>> {
        val sourceFactory = cache.allArt()

        val boundaryCallback = ArtBoundaryCallback(artsyService, cache, prefs, disposable)

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 3)
                .setEnablePlaceholders(true)
                .build()

        return RxPagedListBuilder(sourceFactory, config)
                .setBoundaryCallback(boundaryCallback)
                .buildFlowable(BackpressureStrategy.DROP)
    }
}