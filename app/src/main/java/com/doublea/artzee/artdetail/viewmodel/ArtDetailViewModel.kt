package com.doublea.artzee.artdetail.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.models.Artist
import com.doublea.artzee.commons.repository.ArtRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class ArtDetailViewModel : ViewModel() {

    val artLiveData: MutableLiveData<Art> = MutableLiveData()
    val artistLiveData: MediatorLiveData<Artist> = MediatorLiveData()

    private val repository = ArtRepositoryImpl()

    init {
        artistLiveData.addSource(artLiveData) { artEvent ->
            artEvent?.let { art ->
                repository
                        .getArtistForArtwork(art.id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                { Log.e("ERROR", it.message) },
                                { artistLiveData.value = it }
                        )
            }
        }
    }

    companion object {
        fun createViewModel(fragment: Fragment, art: Art): ArtDetailViewModel {
            val viewModel = ViewModelProviders.of(fragment).get(ArtDetailViewModel::class.java)
            viewModel.artLiveData.value = art
            return viewModel
        }
    }
}