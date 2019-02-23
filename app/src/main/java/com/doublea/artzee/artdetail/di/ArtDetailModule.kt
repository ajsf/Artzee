package com.doublea.artzee.artdetail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doublea.artzee.artdetail.utils.WallpaperHelper
import com.doublea.artzee.artdetail.viewmodel.ArtDetailViewModel
import com.doublea.artzee.common.di.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun artDetailModule() = Kodein.Module("artDetailModule") {
    bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(kodein.direct) }
    bind<ViewModel>(tag = ArtDetailViewModel::class.java.simpleName) with singleton {
        ArtDetailViewModel(instance(), instance())
    }
    bind<WallpaperHelper>() with provider { WallpaperHelper(instance()) }
}