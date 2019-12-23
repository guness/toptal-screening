package com.guness.toptal.client.ui.main

import android.view.Menu
import android.view.MenuItem
import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseActivity
import com.guness.toptal.client.ui.entry.NewEntryActivity
import com.guness.toptal.client.utils.extensions.startActivity
import com.guness.toptal.client.utils.listView.ListAdapter
import com.guness.toptal.client.utils.listView.SingleTypeItemLayout
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class.java, R.layout.activity_main) {

    private val adapter = ListAdapter<SingleTypeItemLayout>()

    override fun initView() {
        listView.adapter = adapter

        disposables += fab.clicks().subscribe { startActivity(NewEntryActivity::class) }

        toolbar.setNavigationOnClickListener {
            // Handle the navigation click by showing a BottomDrawer etc.
            val bottomNavDrawerFragment = BottomSheetFragment()
            bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)

            viewModel.login()
        }

        disposables += viewModel.entries
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(adapter::update)
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
