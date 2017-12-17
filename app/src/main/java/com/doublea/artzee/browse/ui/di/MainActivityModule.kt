package com.doublea.artzee.browse.ui.di

import com.doublea.artzee.MainActivity
import com.doublea.artzee.browse.ui.MainActivityViewModel
import com.doublea.artzee.commons.data.ArtRepository
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun provideViewModel(activity: MainActivity, repository: ArtRepository) = MainActivityViewModel.create(activity, repository)
}