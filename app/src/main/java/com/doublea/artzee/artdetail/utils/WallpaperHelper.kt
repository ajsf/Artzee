package com.doublea.artzee.artdetail.utils

import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.IOException

class WallpaperHelper(private val context: Context) {

    fun setWallpaper(imageUrl: String, callback: () -> Unit) {
        context.doAsync {
            val image = Picasso.with(context)
                .load(imageUrl)
                .get()
            try {
                val wallpaperManager = WallpaperManager.getInstance(context)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap(
                        image,
                        null,
                        true,
                        WallpaperManager.FLAG_LOCK
                    )
                    wallpaperManager.setBitmap(
                        image,
                        null,
                        true,
                        WallpaperManager.FLAG_SYSTEM
                    )
                } else {
                    wallpaperManager.setBitmap(image)
                }
                uiThread {
                    context.toast("Wallpaper Set")
                    callback()
                }
            } catch (e: IOException) {
                context.toast("Unable to set wallpaper. Please check internet connection and permissions")
            }
        }
    }
}
