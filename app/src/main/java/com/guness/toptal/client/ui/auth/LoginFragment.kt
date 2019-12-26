package com.guness.toptal.client.ui.auth

import android.app.Activity
import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseFragment
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.visibility
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.progress_circular.*

class LoginFragment : BaseFragment<LoginViewModel>(LoginViewModel::class, R.layout.fragment_login) {
    override fun initView() {

        disposables += register.clicks()
            .subscribe {
                (activity as AuthenticationActivity).viewModel.authMode.onNext(AuthMode.REGISTER)
            }

        disposables += button.clicks()
            .flatMapCompletable {
                val username = username_text.text.toString().trim()
                val password = password_text.text.toString().trim()
                viewModel.login(username, password)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        activity?.setResult(Activity.RESULT_OK)
                        activity?.finish()
                    }
            }
            .subscribe()

        disposables += viewModel.activityIndicator
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                username_layout.isEnabled = !it
                password_layout.isEnabled = !it
                register.isEnabled = !it
                button.isEnabled = !it
            }

        disposables += Observables.combineLatest(
            viewModel.activityIndicator.map { !it },
            username_text.textChanges().map { it.trim().isNotEmpty() },
            password_text.textChanges().map { it.trim().isNotEmpty() })
            .map { it.first && it.second && it.third }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { button.isEnabled = it }

        disposables += viewModel.activityIndicator.subscribe(progress_circular.visibility())
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}