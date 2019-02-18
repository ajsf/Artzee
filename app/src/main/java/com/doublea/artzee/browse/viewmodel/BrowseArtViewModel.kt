package com.doublea.artzee.browse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.doublea.artzee.common.data.ArtRepository
import com.doublea.artzee.common.model.ArtPagedList
import com.doublea.artzee.common.navigator.Navigator
import io.reactivex.disposables.CompositeDisposable

class BrowseArtViewModel(repository: ArtRepository, private val navigator: Navigator) :
    ViewModel() {

    private val disposable = CompositeDisposable()

    val artList: LiveData<ArtPagedList> = repository
        .getArtFeed(disposable)
        .toLiveData()

    fun selectArtItem(artId: String) = navigator.viewArtDetail(artId)

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}