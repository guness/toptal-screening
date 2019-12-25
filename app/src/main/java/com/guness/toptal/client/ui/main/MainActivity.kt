package com.guness.toptal.client.ui.main

import android.view.Menu
import android.widget.SearchView
import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseActivity
import com.guness.toptal.client.ui.entry.NewEntryActivity
import com.guness.toptal.client.utils.extensions.startActivity
import com.guness.toptal.client.utils.listView.ListAdapter
import com.guness.toptal.client.utils.listView.SingleTypeItemLayout
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.queryTextChanges
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

        disposables += viewModel.filter.skip(1).subscribe { toolbar.performShow() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.bottom_app_bar_menu, menu)
        val searchView = menu.findItem(R.id.bottom_app_bar_menu_search).actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnSearchClickListener {
            fab.hide()
            toolbar.navigationIcon = null
        }
        searchView.setOnCloseListener {
            fab.show()
            toolbar.setNavigationIcon(R.drawable.ic_menu_on_surface_24dp)
            false
        }
        searchView.queryTextChanges().subscribe(viewModel.filter)
        return true
    }
}
