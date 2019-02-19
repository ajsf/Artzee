package com.doublea.artzee.artdetail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.doublea.artzee.artdetail.utils.WallpaperHelper
import com.doublea.artzee.common.data.ArtRepository
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.test.data.ArtDataFactory.randomArt
import com.doublea.artzee.test.data.ArtDataFactory.randomArtist
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
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

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var artDetailViewModel: ArtDetailViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        artDetailViewModel = ArtDetailViewModel(mockRepository, mockWallpaperHelper)
    }

    @Test
    fun `when selectArt is called, it calls getArtById on the repository with the art ID`() {
        val randomArt = stubRandomArt()
        stubRandomArtist(randomArt.id)

        artDetailViewModel.selectArt(randomArt.id)

        verify(mockRepository).getArtById(randomArt.id)
    }

    @Test
    fun `when selectArt is called, it calls getArtistForArtwork on the repository with the art ID`() {
        val randomArt = stubRandomArt()
        stubRandomArtist(randomArt.id)

        artDetailViewModel.selectArt(randomArt.id)

        verify(mockRepository).getArtistForArtwork(randomArt.id)
    }

    @Test
    fun `when selectArt is called, it updates the artLiveData with the art from the repository`() {
        val randomArt = stubRandomArt()
        stubRandomArtist(randomArt.id)

        artDetailViewModel.selectArt(randomArt.id)

        var art: Art? = null

        artDetailViewModel.artLiveData.observeForever { art = it }

        assertEquals(randomArt, art)
    }

    @Test
    fun `when selectArt is called, it updates the artistLiveData with the artist from the repository`() {
        val randomArt = stubRandomArt()
        val randomArtist = stubRandomArtist(randomArt.id)

        artDetailViewModel.selectArt(randomArt.id)

        var artist: Artist? = null

        artDetailViewModel.artistLiveData.observeForever { artist = it }

        assertEquals(randomArtist, artist)
    }

    @Test
    fun `when art has been selected and setWallpaper is called, it calls setWallpaper on the WallpaperHelper with the art's image`() {
        val randomArt = stubRandomArt()
        stubRandomArtist(randomArt.id)
        stubSetWallpaper(randomArt.id)

        artDetailViewModel.setWallpaper()

        verify(mockWallpaperHelper).setWallpaper(eq(randomArt.image), any())
    }

    @Test
    fun `settingWallpaper LiveData is set to false when it is created`() {
        val randomArt = stubRandomArt()
        stubRandomArtist(randomArt.id)

        val settingWallpaper = artDetailViewModel.settingWallpaper.value

        assertEquals(false, settingWallpaper)
    }

    @Test
    fun `when art has been selected and setWallpaper is called, it sets settingWallpaper LiveData to true`() {
        val randomArt = stubRandomArt()
        stubRandomArtist(randomArt.id)
        stubSetWallpaper(randomArt.id) {
            val settingWallpaper = artDetailViewModel.settingWallpaper.value
            assertEquals(true, settingWallpaper)
        }

        artDetailViewModel.setWallpaper()
    }

    @Test
    fun `settingWallpaper LiveData is set back to false when setting wallpaper is complete`() {
        val randomArt = stubRandomArt()
        stubRandomArtist(randomArt.id)

        var settingWallpaper: Boolean?

        stubSetWallpaper(randomArt.id) {
            settingWallpaper = artDetailViewModel.settingWallpaper.value
            assertEquals(true, settingWallpaper)
        }

        settingWallpaper = artDetailViewModel.settingWallpaper.value
        assertEquals(false, settingWallpaper)

        artDetailViewModel.setWallpaper()

        settingWallpaper = artDetailViewModel.settingWallpaper.value
        assertEquals(false, settingWallpaper)
    }

    @Test
    fun `when setWallpaper is called before art has been selected, it does not call setWallpaper on the WallpaperHelper`() {
        val randomArt = stubRandomArt()
        stubRandomArtist(randomArt.id)

        artDetailViewModel.setWallpaper()

        verify(mockWallpaperHelper, never()).setWallpaper(any(), any())
    }

    private fun stubRandomArt(): Art {
        val randomArt = randomArt()
        whenever(mockRepository.getArtById(randomArt.id))
            .thenReturn(Single.just(randomArt))
        return randomArt
    }

    private fun stubRandomArtist(artId: String): Artist {
        val randomArtist = randomArtist()
        whenever(mockRepository.getArtistForArtwork(artId))
            .thenReturn(Single.just(randomArtist))
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
        artDetailViewModel.artLiveData.observeForever {}
    }

}