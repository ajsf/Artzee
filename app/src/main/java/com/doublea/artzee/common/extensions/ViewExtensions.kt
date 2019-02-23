package com.doublea.artzee.common.extensions

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.doublea.artzee.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun ImageView.loadImage(
    imageUrl: String,
    progressBar: ProgressBar? = null,
    callback: () -> Unit = {}
) {
    val loadingCallback = object : Callback {
        override fun onSuccess() {
            progressBar?.visibility = View.GONE
            callback.invoke()
        }

        override fun onError() {
            callback.invoke()
        }
    }

    progressBar?.visibility = View.VISIBLE

    if (TextUtils.isEmpty(imageUrl)) {
        Picasso.with(context).load(R.mipmap.ic_launcher).into(this, loadingCallback)
    } else {
        Picasso.with(context).load(imageUrl).into(this, loadingCallback)
    }
}

inline fun <reified VM : ViewModel, T> T.buildViewModel(): Lazy<VM> where T : KodeinAware, T : FragmentActivity {
    return lazy {
        ViewModelProviders.of(this, direct.instance()).get(VM::class.java)
    }
}

inline fun <reified VM : ViewModel, T> T.buildViewModel(): Lazy<VM> where T : KodeinAware, T : Fragment {
    return lazy {
        ViewModelProviders.of(activity!!, direct.instance()).get(VM::class.java)
    }
}