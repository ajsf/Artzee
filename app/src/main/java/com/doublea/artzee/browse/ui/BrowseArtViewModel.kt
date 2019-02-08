package com.doublea.artzee.browse.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.network.ArtsyService
import com.doublea.artzee.commons.data.network.ArtworkDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel : ViewModel() {

    var artList: LiveData<PagedList<Art>>

    private val pageSize = 10

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val sourceFactory: ArtworkDataSourceFactory

    init {
        sourceFactory = ArtworkDataSourceFactory(compositeDisposable, ArtsyService.getService())
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 3)
                .setEnablePlaceholders(false)
                .build()
        artList = LivePagedListBuilder<String, Art>(sourceFactory, config).build()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}