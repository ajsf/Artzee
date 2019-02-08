package com.doublea.artzee.artdetail.ui

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.models.Artist
import com.doublea.artzee.commons.data.network.ArtsyService
import com.doublea.artzee.commons.data.toArtist
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ArtDetailViewModel : ViewModel() {

    val artLiveData: MutableLiveData<Art> = MutableLiveData()
    val artistLiveData: MediatorLiveData<Artist> = MediatorLiveData()
    private val artsyService = ArtsyService.getService()

    init {
        artistLiveData.addSource(artLiveData) {
            it?.let {
                artsyService.getArtistsByArtworkId(it.id)
                        .map {
                            it._embedded.artists.map { it.toArtist() }
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                { Log.e("ERROR", it.message) },
                                { artistLiveData.value = it.getOrNull(0) }
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