package com.doublea.artzee.commons.di

import com.doublea.artzee.MainActivity
import com.doublea.artzee.browse.ui.di.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity
}