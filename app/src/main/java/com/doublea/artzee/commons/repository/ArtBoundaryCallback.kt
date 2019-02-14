package com.doublea.artzee.commons.repository

import androidx.paging.PagedList
import com.doublea.artzee.commons.data.PreferencesHelper
import com.doublea.artzee.commons.db.ArtsyCache
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.network.ArtsyArtworkWrapper
import com.doublea.artzee.commons.data.network.ArtsyService
import com.doublea.artzee.commons.data.toArt
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ArtBoundaryCallback(
        private val artsyService: ArtsyService,
        private val cache: ArtsyCache,
        private val prefs: PreferencesHelper,
        private val compositeDisposable: CompositeDisposable)
    : PagedList.BoundaryCallback<Art>() {

    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() = artsyService
            .getArt()
            .requestAndSave()

    override fun onItemAtEndLoaded(itemAtEnd: Art) = artsyService
            .getArtByCursor(prefs.getCursor())
            .requestAndSave()

    private fun Single<ArtsyArtworkWrapper>.requestAndSave() {
        if (isRequestInProgress) return
        isRequestInProgress = true
        compositeDisposable.add(subscribeOn(Schedulers.io())
                .subscribeBy(
                        onError = this@ArtBoundaryCallback::onNetworkError,
                        onSuccess = this@ArtBoundaryCallback::onSuccess))
    }

    private fun onSuccess(response: ArtsyArtworkWrapper) {
        val (artList, cursor) = extractResponseData(response)
        cache.insert(artList) {
            prefs.saveCursor(cursor)
            isRequestInProgress = false
        }
    }

    private fun onNetworkError(e: Throwable) {
        println("ERROR GETTING ART")
        e.printStackTrace()
    }

    private fun extractResponseData(response: ArtsyArtworkWrapper): Pair<List<Art>, String> {
        val artList = response._embedded.artworks
                .map { it.toArt() }
                .toList()
        val cursor = getCursor(response._links.next.href)
        return Pair(artList, cursor)
    }

    private fun getCursor(urlString: String): String {
        val cursorIndex = urlString.indexOf("cursor=") + 7
        val endCursorIndex = urlString.indexOf("&size=")
        return urlString.slice(cursorIndex until endCursorIndex)
    }
}