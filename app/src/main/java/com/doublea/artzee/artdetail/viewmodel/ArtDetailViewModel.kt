package com.doublea.artzee.artdetail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.doublea.artzee.artdetail.utils.WallpaperHelper
import com.doublea.artzee.common.data.ArtRepository
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist

class ArtDetailViewModel(
    private val repository: ArtRepository,
    private val wallpaperHelper: WallpaperHelper
) : ViewModel() {

    val artLiveData: LiveData<Art>
        get() = _artLiveData

    val artistLiveData: LiveData<Artist>
        get() = _artistLiveData

    val settingWallpaper: LiveData<Boolean>
        get() = _settingWallpaper

    private var _artLiveData: MutableLiveData<Art> = MutableLiveData()

    private var _artistLiveData: LiveData<Artist> = MutableLiveData()

    private var _settingWallpaper: MutableLiveData<Boolean> = MutableLiveData(false)

    fun selectArt(artId: String) {
        val art = repository.getArtById(artId).blockingGet()
        _artLiveData.postValue(art)
        selectArtist(art)
    }

    private fun selectArtist(art: Art) {
        _artistLiveData = repository
            .getArtistForArtwork(art).toFlowable().toLiveData()
    }

    fun setWallpaper() {
        artLiveData.value?.let {
            _settingWallpaper.postValue(true)
            wallpaperHelper.setWallpaper(it.image) {
                _settingWallpaper.postValue(false)
            }
        }
    }
}