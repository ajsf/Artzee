package com.doublea.artzee.common.data

import androidx.paging.PagedList
import com.doublea.artzee.common.db.ArtsyCache
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.network.ArtApi
import com.doublea.artzee.common.network.ArtApiResponse
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class ArtBoundaryCallback(
    private val artApi: ArtApi,
    private val prefs: PreferencesHelper,
    private val scheduler: Scheduler
) : PagedList.BoundaryCallback<Art>() {

    lateinit var compositeDisposable: CompositeDisposable
    lateinit var cache: ArtsyCache

    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() = requestAndSave { artApi.getArt() }

    override fun onItemAtEndLoaded(itemAtEnd: Art) =
        requestAndSave { artApi.getArt(prefs.getCursor()) }

    private fun requestAndSave(flowableProvider: () -> Flowable<ArtApiResponse>) {
        if (isRequestInProgress) return
        isRequestInProgress = true
        compositeDisposable.add(
            flowableProvider()
                .subscribeOn(scheduler)
                .filter { it.artList != null }
                .take(1)
                .doOnNext(this@ArtBoundaryCallback::onSuccess)
                .doOnError(this@ArtBoundaryCallback::onNetworkError)
                .subscribe()
        )
    }

    private fun onSuccess(response: ArtApiResponse) {
        response.artList?.let {
            compositeDisposable.add(
                cache.insertArtworks(it)
                    .subscribeOn(scheduler)
                    .subscribeBy(
                        onError = { println("Cache error") },
                        onComplete = {
                            response.cursor?.let { cursor -> prefs.saveCursor(cursor) }
                            isRequestInProgress = false
                            compositeDisposable.clear()
                        }
                    ))
        }
    }

    private fun onNetworkError(e: Throwable) {
        println("ERROR GETTING ART")
        e.printStackTrace()
    }

}