package com.doublea.artzee.artdetail.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.util.Log
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.models.Artist
import com.doublea.artzee.commons.data.network.ArtsyService
import com.doublea.artzee.commons.data.toArtist
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ArtDetailViewModel : ViewModel() {

    val artLiveData: MutableLiveData<Art> = MutableLiveData()
    val artistLiveData: MutableLiveData<Artist> = MutableLiveData()

    private val artsyService = ArtsyService.getService()

    fun setArt(art: Art) {
        artLiveData.value = art
        Log.d("TAG", art.toString())
        artsyService.getArtistsByArtworkId(art.id)
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

    companion object {
        fun createViewModel(fragment: Fragment, art: Art): ArtDetailViewModel {
            val viewModel = ViewModelProviders.of(fragment).get(ArtDetailViewModel::class.java)
            viewModel.setArt(art)
            return viewModel
        }
    }
}