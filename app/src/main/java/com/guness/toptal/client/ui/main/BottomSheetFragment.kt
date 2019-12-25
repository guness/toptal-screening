package com.guness.toptal.client.ui.main

import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseBottomSheetDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*

class BottomSheetFragment : BaseBottomSheetDialogFragment<BottomSheetViewModel>(BottomSheetViewModel::class, R.layout.fragment_bottom_sheet) {

    override fun initView() {

        disposables += Observables.combineLatest(viewModel.hasSession, viewModel.isManager)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                menuItem(R.id.bottom_nav_account)?.isVisible = it.first
                menuItem(R.id.bottom_nav_logout)?.isVisible = it.first
                menuItem(R.id.bottom_nav_login)?.isVisible = !it.first
                menuItem(R.id.bottom_nav_register)?.isVisible = !it.first
                menuItem(R.id.bottom_nav_users)?.isVisible = it.second
            }

        navigationView.setNavigationItemSelectedListener {
            activity?.onOptionsItemSelected(it).also { dismiss() } == true
        }
    }

    private fun menuItem(id: Int) = navigationView.menu.findItem(id)
}
