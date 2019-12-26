package com.guness.toptal.client.ui.auth

import android.content.Context
import android.content.Intent
import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseActivity
import com.guness.toptal.client.utils.extensions.put
import com.guness.toptal.client.utils.extensions.serializable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_auth.*

class AuthenticationActivity : BaseActivity<AuthenticationViewModel>(AuthenticationViewModel::class, R.layout.activity_auth) {

    override fun initView() {
        intent.serializable<AuthMode>()?.let(viewModel.authMode::onNext)

        disposables += viewModel.authMode.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container, when (it) {
                        AuthMode.LOGIN -> LoginFragment.newInstance()
                        AuthMode.REGISTER -> RegisterFragment.newInstance()
                        null -> throw IllegalArgumentException()
                    }
                ).commit()

                when (it) {
                    AuthMode.LOGIN -> toolbar.setTitle(R.string.login)
                    AuthMode.REGISTER -> toolbar.setTitle(R.string.create_account)
                }
            }
    }

    companion object {
        fun newIntent(context: Context, mode: AuthMode) = Intent(context, AuthenticationActivity::class.java).put(mode)
    }
}