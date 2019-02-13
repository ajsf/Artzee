package com.doublea.artzee.artdetail.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.models.Artist
import com.doublea.artzee.commons.repository.ArtRepository
import com.doublea.artzee.commons.repository.ArtRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class ArtDetailViewModel(private val repository: ArtRepository = ArtRepositoryImpl()) : ViewModel() {

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

    companion object {
        fun createViewModel(fragment: Fragment, art: Art): ArtDetailViewModel {
            val viewModel = ViewModelProviders.of(fragment).get(ArtDetailViewModel::class.java)
            viewModel.selectArt(art)
            return viewModel
        }
    }
}