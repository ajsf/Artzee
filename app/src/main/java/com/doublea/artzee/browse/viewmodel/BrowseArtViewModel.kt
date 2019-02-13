package com.doublea.artzee.browse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import androidx.paging.PagedList
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.navigator.Navigator
import com.doublea.artzee.commons.repository.ArtRepository
import com.doublea.artzee.commons.repository.ArtRepositoryImpl
import io.reactivex.disposables.CompositeDisposable

class BrowseArtViewModel(repository: ArtRepository = ArtRepositoryImpl()) : ViewModel() {

    private val disposable = CompositeDisposable()

    val artList: LiveData<PagedList<Art>> = repository.getArtFeed(disposable).toLiveData()

    lateinit var navigator: Navigator

    fun selectArtItem(art: Art) = navigator.viewArtDetail(art)

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}