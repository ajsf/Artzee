package com.doublea.artzee.artdetail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doublea.artzee.artdetail.viewmodel.ArtDetailViewModel
import com.doublea.artzee.common.di.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun artDetailModule() = Kodein.Module("artDetailModule") {
    bind<ViewModel>(tag = ArtDetailViewModel::class.java.simpleName) with provider {
        ArtDetailViewModel(instance())
    }
    bind<ViewModelProvider.Factory>() with provider { ViewModelFactory(kodein.direct) }
}