package com.doublea.artzee.browse.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import com.doublea.artzee.commons.data.ArtRepository
import com.doublea.artzee.commons.data.models.Art
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel : ViewModel() {

    lateinit var repository: ArtRepository

    val art: LiveData<List<Art>> by lazy {
        LiveDataReactiveStreams
                .fromPublisher<List<Art>>(repository
                        .getArt()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable()) }

    companion object {
        fun create(activity: AppCompatActivity, repository: ArtRepository): MainActivityViewModel {
            val viewModel = ViewModelProviders.of(activity).get((MainActivityViewModel::class.java))
            viewModel.repository = repository
            return viewModel
        }
    }

}