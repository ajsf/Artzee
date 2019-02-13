package com.doublea.artzee.commons.data.network

import androidx.paging.PageKeyedDataSource
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.toArt
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class ArtworkDataSource(
        private val artsyService: ArtsyService,
        private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<String, Art>() {

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Art>) {
        compositeDisposable.add(artsyService.getArt().subscribeBy(
                onError = { e -> onNetworkError(e) },
                onSuccess = { response ->
                    val (artList, cursor) = extractResponseData(response)
                    callback.onResult(artList, "", cursor)
                }))
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Art>) {
        compositeDisposable.add(artsyService.getArtByCursor(params.key).subscribeBy(
                onError = { e -> onNetworkError(e) },
                onSuccess = { response ->
                    val (artList, cursor) = extractResponseData(response)
                    callback.onResult(artList, cursor)
                }))
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Art>) {}

    private fun onNetworkError(e: Throwable) {
        println("ERROR GETTING ART")
        e.printStackTrace()
    }

    private fun extractResponseData(response: ArtsyArtworkWrapper): Pair<List<Art>, String> {
        val artList = response._embedded.artworks.map {
            it.toArt()
        }.toList()
        val cursor = getCursor(response._links.next.href)
        return Pair(artList, cursor)
    }

    private fun getCursor(urlString: String): String {
        val cursorIndex = urlString.indexOf("cursor=") + 7
        val endCursorIndex = urlString.indexOf("&size=")
        return urlString.slice(cursorIndex until endCursorIndex)
    }
}