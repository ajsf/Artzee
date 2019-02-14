package com.doublea.artzee

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.doublea.artzee.browse.view.BrowseArtFragment
import com.doublea.artzee.common.extensions.launchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            BrowseArtFragment().launchFragment(supportFragmentManager, addToBackStack = false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_browse_art, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> true
        android.R.id.home -> with(supportFragmentManager) {
            if (backStackEntryCount > 0) popBackStack()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
