package com.doublea.artzee.artdetail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.common.data.ArtRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class ArtDetailViewModel(private val repository: ArtRepository) : ViewModel() {

    val artLiveData: LiveData<Art>
        get() = _artLiveData

    val artistLiveData: LiveData<Artist>
        get() = _artistLiveData

    private val _artLiveData: MutableLiveData<Art> = MutableLiveData()

    private val _artistLiveData: MediatorLiveData<Artist> = MediatorLiveData()

    init {
        _artistLiveData.addSource(artLiveData) { artEvent ->
            artEvent?.let { art ->
                repository
                        .getArtistForArtwork(art.id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                { Log.e("ERROR", it.message) },
                                { _artistLiveData.value = it }
                        )
            }
        }
    }

    fun selectArt(art: Art) {
        _artLiveData.value = art
    }
}