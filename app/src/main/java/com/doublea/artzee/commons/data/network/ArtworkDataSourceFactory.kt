package com.doublea.artzee.commons.data.network

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.doublea.artzee.commons.data.models.Art
import io.reactivex.disposables.CompositeDisposable

class ArtworkDataSourceFactory(private val compositeDisposable: CompositeDisposable,
                               private val artsyService: ArtsyService)
    : DataSource.Factory<String, Art>() {

    private val artworkDataSourceLiveData = MutableLiveData<ArtworkDataSource>()

    override fun create(): DataSource<String, Art> {
        val artworkDataSource = ArtworkDataSource(artsyService, compositeDisposable)
        artworkDataSourceLiveData.postValue(artworkDataSource)
        return artworkDataSource
    }
}