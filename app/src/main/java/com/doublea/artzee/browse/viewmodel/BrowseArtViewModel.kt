package com.doublea.artzee.browse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.navigator.Navigator
import com.doublea.artzee.commons.repository.ArtRepositoryImpl

class BrowseArtViewModel : ViewModel() {

    private val repository = ArtRepositoryImpl()

    val artList: LiveData<PagedList<Art>> = repository.getArtFeed()

    lateinit var navigator: Navigator

    fun selectArtItem(art: Art) = navigator.viewArtDetail(art)
}