package com.doublea.artzee.artdetail.utils

import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.IOException

fun setWallpaper(context: Context?, imageUrl: String) {
    context.doAsync {
        val image = Picasso.with(context)
                .load(imageUrl)
                .get()
        uiThread {
            try {
                val wallpaperManager = WallpaperManager.getInstance(context)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap(
                            image,
                            null,
                            true,
                            WallpaperManager.FLAG_LOCK)
                    wallpaperManager.setBitmap(
                            image,
                            null,
                            true,
                            WallpaperManager.FLAG_SYSTEM)
                } else {
                    wallpaperManager.setBitmap(image)
                }
                context?.toast("Wallpaper Set")
            } catch (e: IOException) {
                context?.toast("Unable to set wallpaper. Please check internet connection and permissions")
            }
        }
    }
}

