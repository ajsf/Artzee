package com.doublea.artzee.artdetail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.doublea.artzee.artdetail.utils.WallpaperHelper
import com.doublea.artzee.common.data.ArtRepository
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.test.data.ArtDataFactory.randomArt
import com.doublea.artzee.test.data.ArtDataFactory.randomArtist
import com.doublea.artzee.test.data.ArtDataFactory.randomViewState
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ArtDetailViewModelTest {

    @Mock
    lateinit var mockRepository: ArtRepository

    @Mock
    lateinit var mockWallpaperHelper: WallpaperHelper

    @Mock
    lateinit var mockMapper: Mapper<Art, ArtDetailViewState>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var artDetailViewModel: ArtDetailViewModel

    private lateinit var scheduler: TestScheduler

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        scheduler = TestScheduler()
        artDetailViewModel =
            ArtDetailViewModel(mockRepository, mockWallpaperHelper, mockMapper, scheduler)
    }

    @Test
    fun `when selectArt is called, it calls getArtById on the repository with the art ID`() {
        val randomArt = stubRandomArt()
        stubRandomArtist(randomArt)

        artDetailViewModel.selectArt(randomArt.id)

        verify(mockRepository).getArtById(randomArt.id)
    }

    @Test
    fun `when selectArt is called, it calls getArtistForArtwork on the repository with the returned art object`() {
        val randomArt = stubRandomArt()
        stubRandomArtist(randomArt)
        stubRandomViewState(randomArt)

        artDetailViewModel.selectArt(randomArt.id)
        scheduler.triggerActions()

        verify(mockRepository).getArtistForArtwork(randomArt)
    }

    @Test
    fun `when selectArt is called, it calls toModel on the mapper with the art from the repository`() {
        whenever(mockRepository.getArtistForArtwork(any()))
            .thenReturn(Maybe.never())
        val randomArt = stubRandomArt()
        stubRandomViewState(randomArt)

        artDetailViewModel.selectArt(randomArt.id)
        scheduler.triggerActions()

        verify(mockMapper).toModel(randomArt)
    }

    @Test
    fun `when selectArt is called, and the repository has returned art, but not an artist, it updates the viewState with the output from the mapper`() {
        whenever(mockRepository.getArtistForArtwork(any()))
            .thenReturn(Maybe.never())
        val randomArt = stubRandomArt()
        val randomViewState = stubRandomViewState(randomArt)

        artDetailViewModel.selectArt(randomArt.id)

        var viewState: ArtDetailViewState? = null

        artDetailViewModel.viewState.observeForever { viewState = it }
        scheduler.triggerActions()

        assertEquals(randomViewState, viewState)
    }

    @Test
    fun `when selectArt is called, it updates the viewState with the artist from the repository`() {
        val randomArt = stubRandomArt()
        val randomArtist = stubRandomArtist(randomArt)
        val randomViewState = stubRandomViewState(randomArt)
            .copy(artistName = randomArtist.name)

        artDetailViewModel.selectArt(randomArt.id)

        var viewState: ArtDetailViewState? = null

        artDetailViewModel.viewState.observeForever { viewState = it }
        scheduler.triggerActions()

        assertEquals(randomViewState, viewState)
    }

    @Test
    fun `when art has been selected and setWallpaper is called, it calls setWallpaper on the WallpaperHelper with the wallpaperImage from the view state`() {
        val randomArt = stubRandomArt()
        stubRandomArtist(randomArt)
        val viewState = stubRandomViewState(randomArt)
        stubSetWallpaper(randomArt.id)

        artDetailViewModel.setWallpaper()

        verify(mockWallpaperHelper).setWallpaper(eq(viewState.wallpaperImage!!), any())
    }

    @Test
    fun `settingWallpaper is set to false in the viewState when it is created`() {
        val settingWallpaper = getViewState().settingWallpaper

        assertEquals(false, settingWallpaper)
    }

    @Test
    fun `when art has been selected and setWallpaper is called, it sets settingWallpaper in the viewState to true`() {
        val randomArt = stubRandomArt()
        stubRandomArtist(randomArt)
        stubRandomViewState(randomArt)

        stubSetWallpaper(randomArt.id) {
            val settingWallpaper = getViewState().settingWallpaper
            assertEquals(true, settingWallpaper)
        }

        artDetailViewModel.setWallpaper()
    }

    @Test
    fun `settingWallpaper is set back to false when setting wallpaper is complete`() {
        val randomArt = stubRandomArt()
        stubRandomViewState(randomArt)
        stubRandomArtist(randomArt)

        var settingWallpaper: Boolean

        stubSetWallpaper(randomArt.id) {
            settingWallpaper = getViewState().settingWallpaper
            assertEquals(true, settingWallpaper)
        }

        settingWallpaper = getViewState().settingWallpaper
        assertEquals(false, settingWallpaper)

        artDetailViewModel.setWallpaper()

        settingWallpaper = getViewState().settingWallpaper
        assertEquals(false, settingWallpaper)
    }

    @Test
    fun `when setWallpaper is called before art has been selected, it does not call setWallpaper on the WallpaperHelper`() {
        val randomArt = stubRandomArt()
        stubRandomArtist(randomArt)

        artDetailViewModel.setWallpaper()
        scheduler.triggerActions()

        verify(mockWallpaperHelper, never()).setWallpaper(any(), any())
    }

    private fun stubRandomArt(): Art {
        val randomArt = randomArt()
        whenever(mockRepository.getArtById(randomArt.id))
            .thenReturn(Single.just(randomArt))
        return randomArt
    }

    private fun stubRandomViewState(art: Art): ArtDetailViewState {
        val randomViewState = randomViewState()
        whenever(mockMapper.toModel(art))
            .thenReturn(randomViewState)
        return randomViewState
    }

    private fun stubRandomArtist(art: Art): Artist {
        val randomArtist = randomArtist()
        whenever(mockRepository.getArtistForArtwork(art))
            .thenReturn(Maybe.just(randomArtist))
        return randomArtist
    }

    private fun stubSetWallpaper(artId: String, callback: () -> Unit = {}) {
        whenever(mockWallpaperHelper.setWallpaper(any(), any()))
            .thenAnswer { invocation ->
                callback.invoke()
                val functionCallback = invocation.getArgument(1) as () -> Unit
                functionCallback.invoke()
            }
        artDetailViewModel.selectArt(artId)
        scheduler.triggerActions()
    }

    private fun getViewState(): ArtDetailViewState = artDetailViewModel.viewState.value!!

}