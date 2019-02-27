package com.doublea.artzee

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.doublea.artzee.browse.view.BrowseArtFragment
import com.doublea.artzee.common.data.PreferencesHelper
import com.doublea.artzee.common.di.activityModule
import com.doublea.artzee.common.extensions.launchFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

    private val _parentKodein: Kodein by closestKodein()

    override val kodein = Kodein.lazy {
        extend(_parentKodein)
        import(activityModule(this@MainActivity))
    }

    private val prefs: PreferencesHelper by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        launchFragments(savedInstanceState)
    }

    private fun launchFragments(savedInstanceState: Bundle?) {
        checkFirstRun()
        if (savedInstanceState == null) {
            val browseFragment = BrowseArtFragment()
            browseFragment.launchFragment(supportFragmentManager, false, BrowseArtFragment.TAG)
        }
    }

    private fun checkFirstRun() {
        if (prefs.firstRunTime(System.currentTimeMillis()) == -1L) {
            WelcomeFragment().show(supportFragmentManager, "Welcome")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_browse_art, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> true
        android.R.id.home -> with(supportFragmentManager) {
            popBackStack()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
