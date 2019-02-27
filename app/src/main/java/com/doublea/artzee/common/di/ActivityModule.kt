package com.doublea.artzee.common.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.doublea.artzee.artdetail.utils.WallpaperHelper
import com.doublea.artzee.artdetail.viewmodel.ArtDetailViewModel
import com.doublea.artzee.artdetail.viewmodel.ArtDetailViewState
import com.doublea.artzee.artdetail.viewmodel.ArtToViewStateMapper
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.navigator.Navigator
import com.doublea.artzee.common.navigator.NavigatorImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun activityModule(activity: FragmentActivity) = Kodein.Module("activityModule") {
    bind<Navigator>() with provider { NavigatorImpl(activity) }
    bind<ViewModel>(tag = ArtDetailViewModel::class.java.simpleName) with singleton {
        ArtDetailViewModel(instance(), instance(), instance())
    }
    bind<WallpaperHelper>() with provider { WallpaperHelper(instance()) }
    bind<Mapper<Art, ArtDetailViewState>>() with provider { ArtToViewStateMapper() }
}