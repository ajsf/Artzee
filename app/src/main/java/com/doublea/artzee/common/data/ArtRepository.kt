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

data class ArtRepositoryResponse<T>(val data: T? = null, val error: String? = null)

interface ArtRepository {
    fun getArtistForArtwork(art: Art): Flowable<ArtRepositoryResponse<Artist>>
    fun getArtFeed(disposable: CompositeDisposable): Flowable<ArtPagedList>
    fun getArtById(artId: String): Single<Art>
}

class ArtRepositoryImpl(
    private val artApi: ArtApi,
    private val artsyCache: ArtsyCache,
    private val scheduler: Scheduler
) : ArtRepository {

    override fun getArtistForArtwork(art: Art): Flowable<ArtRepositoryResponse<Artist>> {

        fun saveArtist(artist: Artist) = artsyCache.insertArtist(artist, art.id)

        return if (art.artistId != null) artsyCache
            .getArtistById(art.artistId)
            .map { ArtRepositoryResponse(it) }
            .subscribeOn(scheduler).toFlowable()
        else artApi
            .getArtistForArtwork(art.id)
            .map { ArtRepositoryResponse(it.artist, it.error) }
            .doOnNext { it.data?.let(::saveArtist) }
            .subscribeOn(scheduler)
    }

    override fun getArtFeed(disposable: CompositeDisposable): Flowable<ArtPagedList> = artsyCache
        .getArtFeed(disposable)
        .subscribeOn(scheduler)

    override fun getArtById(artId: String): Single<Art> = artsyCache
        .getArtById(artId)
        .subscribeOn(scheduler)
}