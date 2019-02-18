package com.doublea.artzee.common.data

import androidx.paging.PagedList
import com.doublea.artzee.common.db.ArtsyCache
import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.network.ArtApi
import com.doublea.artzee.common.network.ArtApiResponse
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class ArtBoundaryCallback(
    private val artApi: ArtApi,
    private val prefs: PreferencesHelper,
    private val mapper: Mapper<ArtApiResponse, List<ArtEntity>>,
    private val scheduler: Scheduler
) : PagedList.BoundaryCallback<Art>() {

    lateinit var compositeDisposable: CompositeDisposable
    lateinit var cache: ArtsyCache

    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() = requestAndSave { artApi.getArt() }

    override fun onItemAtEndLoaded(itemAtEnd: Art) =
        requestAndSave { artApi.getArt(prefs.getCursor()) }

    private fun requestAndSave(singleProvider: () -> Single<ArtApiResponse>) {
        if (isRequestInProgress) return
        isRequestInProgress = true
        compositeDisposable.add(
            singleProvider()
                .subscribeOn(scheduler)
                .doOnSuccess(this@ArtBoundaryCallback::onSuccess)
                .doOnError(this@ArtBoundaryCallback::onNetworkError)
                .subscribe()
        )
    }

    private fun onSuccess(response: ArtApiResponse) {
        val artEntityList = mapper.toModel(response)

        compositeDisposable.add(
            cache.insert(artEntityList)
                .subscribeOn(scheduler)
                .subscribeBy(
                    onError = { println("Cache error") },
                    onComplete = {
                        prefs.saveCursor(response.cursor)
                        isRequestInProgress = false
                    }
                ))
    }

    private fun onNetworkError(e: Throwable) {
        println("ERROR GETTING ART")
        e.printStackTrace()
    }

}