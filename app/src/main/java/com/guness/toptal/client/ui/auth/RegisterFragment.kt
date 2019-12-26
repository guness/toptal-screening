package com.guness.toptal.client.ui.auth

import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseFragment
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseFragment<RegisterViewModel>(RegisterViewModel::class, R.layout.fragment_register) {
    override fun initView() {
        disposables += login.clicks()
            .subscribe {
                (activity as AuthenticationActivity).viewModel.authMode.onNext(AuthMode.LOGIN)
            }
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }
}