package com.doublea.artzee.artdetail.di

import androidx.lifecycle.ViewModelProvider
import com.doublea.artzee.common.di.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

fun artDetailModule() = Kodein.Module("artDetailModule") {
    bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(kodein.direct) }
}