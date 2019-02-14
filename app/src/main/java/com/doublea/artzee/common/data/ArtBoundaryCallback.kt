package com.doublea.artzee.common.data

import androidx.paging.PagedList
import com.doublea.artzee.common.db.ArtsyCache
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.network.ArtApi
import com.doublea.artzee.common.network.ArtApiResponse
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ArtBoundaryCallback(
        private val artApi: ArtApi,
        private val cache: ArtsyCache,
        private val prefs: PreferencesHelper,
        private val compositeDisposable: CompositeDisposable)
    : PagedList.BoundaryCallback<Art>() {

    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() = artApi
            .getArt()
            .requestAndSave()

    override fun onItemAtEndLoaded(itemAtEnd: Art) = artApi
            .getArt(prefs.getCursor())
            .requestAndSave()

    private fun Single<ArtApiResponse>.requestAndSave() {
        if (isRequestInProgress) return
        isRequestInProgress = true
        compositeDisposable.add(subscribeOn(Schedulers.io())
                .subscribeBy(
                        onError = this@ArtBoundaryCallback::onNetworkError,
                        onSuccess = this@ArtBoundaryCallback::onSuccess))
    }

    private fun onSuccess(response: ArtApiResponse) {
        cache.insert(response.artList) {
            prefs.saveCursor(response.cursor)
            isRequestInProgress = false
        }
    }

    private fun onNetworkError(e: Throwable) {
        println("ERROR GETTING ART")
        e.printStackTrace()
    }

}