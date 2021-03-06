package com.doublea.artzee.browse.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doublea.artzee.browse.viewmodel.BrowseArtViewModel
import com.doublea.artzee.common.di.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun browseArtModule() = Kodein.Module("browseArtModule") {
    bind<ViewModel>(tag = BrowseArtViewModel::class.java.simpleName) with provider {
        BrowseArtViewModel(instance())
    }
    bind<ViewModelProvider.Factory>() with provider { ViewModelFactory(kodein.direct) }
}