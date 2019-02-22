package com.doublea.artzee.browse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.doublea.artzee.common.data.ArtRepository
import com.doublea.artzee.common.model.ArtPagedList
import io.reactivex.disposables.CompositeDisposable

class BrowseArtViewModel(repository: ArtRepository) :
    ViewModel() {

    private val disposable = CompositeDisposable()

    val artList: LiveData<ArtPagedList> = repository
        .getArtFeed(disposable)
        .toLiveData()

    var currentPosition: Int = 0

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}