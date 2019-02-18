package com.doublea.artzee.common.data

import com.doublea.artzee.common.db.ArtsyCache
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.ArtPagedList
import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.common.network.ArtApi
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

interface ArtRepository {
    fun getArtistForArtwork(artworkId: String): Single<Artist>
    fun getArtFeed(disposable: CompositeDisposable): Flowable<ArtPagedList>
    fun getArtById(artId: String): Single<Art>
}

class ArtRepositoryImpl(
    private val artApi: ArtApi,
    private val artsyCache: ArtsyCache,
    private val scheduler: Scheduler
) : ArtRepository {

    override fun getArtistForArtwork(artworkId: String): Single<Artist> = artApi
        .getArtistForArtwork(artworkId)
        .subscribeOn(scheduler)

    override fun getArtFeed(disposable: CompositeDisposable): Flowable<ArtPagedList> = artsyCache
        .getArtFeed(disposable)
        .subscribeOn(scheduler)

    override fun getArtById(artId: String): Single<Art> = artsyCache
        .getArtById(artId)
        .subscribeOn(scheduler)
}