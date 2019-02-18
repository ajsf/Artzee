package com.doublea.artzee.common.data

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.doublea.artzee.common.db.ArtsyCache
import com.doublea.artzee.common.model.ArtPagedList
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable

class ArtPagedListBuilder(
        private val boundaryCallback: ArtBoundaryCallback,
        private val cache: ArtsyCache
) {

    private val pageSize = 5

    fun getPagedList(disposable: CompositeDisposable): Flowable<ArtPagedList> {

        boundaryCallback.compositeDisposable = disposable

        val sourceFactory = cache.allArt()

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 3)
                .setEnablePlaceholders(true)
                .build()

        return RxPagedListBuilder(sourceFactory, config)
                .setBoundaryCallback(boundaryCallback)
                .buildFlowable(BackpressureStrategy.DROP)
                .map { ArtPagedList(it) }
    }
}