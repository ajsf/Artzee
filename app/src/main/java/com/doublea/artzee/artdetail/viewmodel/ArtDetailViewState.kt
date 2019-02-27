package com.doublea.artzee.artdetail.viewmodel

data class ArtDetailViewState(
    val imageUrl: String? = null,
    val wallpaperImage: String? = null,
    val title: String = "",
    val details: String = "",
    val details2: String? = null,
    val artistName: String? = null,
    val settingWallpaper: Boolean = false
)