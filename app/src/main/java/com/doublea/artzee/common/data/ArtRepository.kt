package com.doublea.artzee.common.data

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.doublea.artzee.common.db.ArtsyCache
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.common.network.ArtApi
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
        private val artApi: ArtApi,
        private val cache: ArtsyCache,
        private val prefs: PreferencesHelper
) : ArtRepository {

    private val pageSize = 5

    override fun getArtistForArtwork(artworkId: String): Single<Artist> = artApi
            .getArtistForArtwork(artworkId)
            .subscribeOn(Schedulers.io())

    override fun getArtFeed(disposable: CompositeDisposable): Flowable<PagedList<Art>> {
        val sourceFactory = cache.allArt()

        val boundaryCallback = ArtBoundaryCallback(artApi, cache, prefs, disposable)

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