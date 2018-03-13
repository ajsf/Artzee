package com.doublea.artzee.commons.data.network

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.doublea.artzee.commons.data.models.Art
import io.reactivex.disposables.CompositeDisposable

class ArtworkDataSourceFactory(private val compositeDisposable: CompositeDisposable,
                               private val artsyService: ArtsyService)
    : DataSource.Factory<String, Art> {

    val artworkDataSourceLiveData = MutableLiveData<ArtworkDataSource>()

    override fun create(): DataSource<String, Art> {
        val artworkDataSource = ArtworkDataSource(artsyService, compositeDisposable)
        artworkDataSourceLiveData.postValue(artworkDataSource)
        return artworkDataSource
    }
}