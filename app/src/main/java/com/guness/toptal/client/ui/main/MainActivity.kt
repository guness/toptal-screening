package com.guness.toptal.client.ui.main

import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.core.view.isVisible
import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseActivity
import com.guness.toptal.client.ui.auth.AuthMode
import com.guness.toptal.client.ui.auth.AuthenticationActivity
import com.guness.toptal.client.ui.entry.EntryActivity
import com.guness.toptal.client.ui.profile.ProfileActivity
import com.guness.toptal.client.ui.users.UsersActivity
import com.guness.toptal.client.utils.extensions.startActivity
import com.guness.toptal.client.utils.listView.ListAdapter
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.queryTextChanges
import com.petarmarijanovic.rxactivityresult.RxActivityResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Flowables
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class, R.layout.activity_main) {

    private val adapter = ListAdapter<EntryItemLayout>()

    private val activityResult: RxActivityResult by lazy {
        RxActivityResult(this)
    }

    override fun initView() {
        listView.adapter = adapter

        disposables += fab.clicks().subscribe { startActivity(EntryActivity::class) }

        toolbar.setNavigationOnClickListener {
            val bottomNavDrawerFragment = BottomSheetFragment()
            bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
        }
        disposables += Flowables.combineLatest(viewModel.entries, viewModel.users, viewModel.manager)
            .distinctUntilChanged()
            .map { triple ->
                TimeEntryAdapterModel(triple.first, triple.second, triple.third) {
                    startActivity(EntryActivity.newIntent(this, it))
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                val empty = it.items.isEmpty()
                empty_image.isVisible = empty
                empty_message.isVisible = empty
                listView.isVisible = !empty
            }
            .subscribe(adapter::update)

        disposables += viewModel.filter.skip(1).subscribe { toolbar.performShow() }

        viewModel.fetchEntries()
        viewModel.fetchUsers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.search_app_bar_menu, menu)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottom_nav_logout -> handleLogoutMenu()
            R.id.bottom_nav_users -> handleUsersMenu()
            R.id.bottom_nav_register -> handleRegisterMenu()
            R.id.bottom_nav_login -> handleLoginMenu()
            R.id.bottom_nav_account -> handleAccountMenu()
            else -> return super.onOptionsItemSelected(item)
        }
        return false
    }

    private fun handleAccountMenu() {
        startActivity(ProfileActivity.newIntent(this))
    }

    private fun handleLoginMenu() {
        handleAuth(AuthMode.LOGIN)
    }

    private fun handleRegisterMenu() {
        handleAuth(AuthMode.REGISTER)
    }

    private fun handleAuth(authMode: AuthMode) {
        disposables += activityResult.start(AuthenticationActivity.newIntent(this, authMode))
            .filter { it.isOk }
            .doOnSuccess {
                //TODO: we may ask for merge items
            }
            .subscribe()
    }

    private fun handleUsersMenu() {
        startActivity(UsersActivity.newIntent(this))
    }

    private fun handleLogoutMenu() {
        viewModel.logout()
    }
}
