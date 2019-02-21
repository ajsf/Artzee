package com.doublea.artzee.common.di

import androidx.fragment.app.FragmentActivity
import com.doublea.artzee.common.navigator.Navigator
import com.doublea.artzee.common.navigator.NavigatorImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

fun activityModule(activity: FragmentActivity) = Kodein.Module("activityModule") {
    bind<Navigator>() with provider { NavigatorImpl(activity) }
}