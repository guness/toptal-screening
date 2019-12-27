package com.guness.toptal.client.ui.users

import android.content.Context
import android.content.Intent
import android.view.Menu
import android.widget.SearchView
import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseActivity
import com.guness.toptal.client.ui.profile.ProfileActivity
import com.guness.toptal.client.ui.user.CreateUserActivity
import com.guness.toptal.client.utils.extensions.mapList
import com.guness.toptal.client.utils.extensions.startActivity
import com.guness.toptal.client.utils.listView.DefaultAdapterModel
import com.guness.toptal.client.utils.listView.ListAdapter
import com.guness.toptal.client.utils.listView.SingleTypeItemLayout
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.queryTextChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.content_users.*

class UsersActivity : BaseActivity<UsersViewModel>(UsersViewModel::class, R.layout.activity_users) {

    private val adapter = ListAdapter<SingleTypeItemLayout>()

    override fun initView() {
        listView.adapter = adapter

        disposables += fab.clicks().subscribe { startActivity(CreateUserActivity::class) }

        disposables += viewModel.users
            .mapList { user ->
                SingleTypeItemLayout(R.layout.item_row_user, UserItemViewModel(user) {
                    startActivity(ProfileActivity.newIntent(this, user.user))
                })
            }
            .map { DefaultAdapterModel(it.toTypedArray()) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(adapter::update)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.search_app_bar_menu, menu)
        val searchView = menu.findItem(R.id.bottom_app_bar_menu_search).actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryTextChanges().subscribe(viewModel.filter)
        return true
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, UsersActivity::class.java)
    }
}