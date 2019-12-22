package com.guness.toptal.client.ui.main

import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class.java, R.layout.activity_main) {

    override fun initView() {
        fab.setOnClickListener { view ->

            Snackbar.make(rootView, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(fab)
                .setAction("Action", null)
                .show()
        }

        bar.setNavigationOnClickListener {
            // Handle the navigation click by showing a BottomDrawer etc.
            val bottomNavDrawerFragment = BottomSheetFragment()
            bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.bottom_app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            //R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
