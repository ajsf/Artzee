package com.doublea.artzee.commons.extensions

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.doublea.artzee.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun ImageView.loadImage(imageUrl: String, progressBar: ProgressBar) {
    val loadingCallback = object : Callback {
        override fun onSuccess() {
            progressBar.visibility = View.GONE
        }

        override fun onError() {
        }
    }
    progressBar.visibility = View.VISIBLE
    if (TextUtils.isEmpty(imageUrl)) {
        Picasso.with(context).load(R.mipmap.ic_launcher).into(this, loadingCallback)
    } else {
        Picasso.with(context).load(imageUrl).into(this, loadingCallback)
    }
}

fun Fragment.launchFragment(fm: FragmentManager, addToBackStack: Boolean = true, tag: String = "TAG") {
    val ft = fm.beginTransaction()
    ft.replace(R.id.fragment_container, this, tag)
    if (addToBackStack) {
        ft.addToBackStack(null)
    }
    ft.commit()
}