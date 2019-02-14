package com.doublea.artzee

import android.app.Application
import com.doublea.artzee.common.di.appModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class ArtzeeApp : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(appModule(this@ArtzeeApp))
    }
}