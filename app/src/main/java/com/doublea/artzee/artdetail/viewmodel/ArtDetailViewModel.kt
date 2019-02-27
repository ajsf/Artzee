package com.doublea.artzee.artdetail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doublea.artzee.artdetail.utils.WallpaperHelper
import com.doublea.artzee.common.data.ArtRepository
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class ArtDetailViewModel(
    private val repository: ArtRepository,
    private val wallpaperHelper: WallpaperHelper,
    private val artToViewStateMapper: Mapper<Art, ArtDetailViewState>,
    private val uiScheduler: Scheduler = AndroidSchedulers.mainThread()
) : ViewModel() {

    val viewState: LiveData<ArtDetailViewState>
        get() = _viewState

    private val _viewState: MutableLiveData<ArtDetailViewState> = MutableLiveData()

    private fun currentViewState(): ArtDetailViewState = _viewState.value!!

    private val disposable = CompositeDisposable()

    init {
        _viewState.value = ArtDetailViewState()
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable.isDisposed.not()) disposable.dispose()
    }

    fun selectArt(artId: String) {
        repository.getArtById(artId)
            .observeOn(uiScheduler)
            .subscribeBy {
                _viewState.value = artToViewStateMapper.toModel(it)
                selectArtist(it)
            }
    }

    private fun selectArtist(art: Art) {
        repository.getArtistForArtwork(art)
            .observeOn(uiScheduler)
            .subscribeBy {
                _viewState.value = currentViewState().copy(artistName = it.name)
            }
    }

    fun setWallpaper() {
        currentViewState().wallpaperImage?.let {
            _viewState.value = currentViewState().copy(settingWallpaper = true)
            wallpaperHelper.setWallpaper(it) {
                _viewState.value = currentViewState().copy(settingWallpaper = false)
            }
        }
    }
}