package com.doublea.artzee.artdetail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.doublea.artzee.common.data.ArtRepository
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist

class ArtDetailViewModel(private val repository: ArtRepository) : ViewModel() {

    val artLiveData: LiveData<Art>
        get() = _artLiveData

    val artistLiveData: LiveData<Artist>
        get() = _artistLiveData

    private var _artLiveData: LiveData<Art> = MutableLiveData()

    private var _artistLiveData: LiveData<Artist> = MutableLiveData()

    fun selectArt(artId: String) {
        val art = repository.getArtById(artId)
        _artLiveData = art.toFlowable()
            .doOnEach { }
            .toLiveData()

        _artistLiveData = repository
            .getArtistForArtwork(artId).toFlowable().toLiveData()
    }
}