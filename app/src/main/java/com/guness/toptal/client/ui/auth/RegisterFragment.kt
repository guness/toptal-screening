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
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.progress_circular.*

class RegisterFragment : BaseFragment<RegisterViewModel>(RegisterViewModel::class, R.layout.fragment_register) {
    override fun initView() {
        disposables += login.clicks()
            .subscribe {
                (activity as AuthenticationActivity).viewModel.authMode.onNext(AuthMode.LOGIN)
            }

        disposables += button.clicks()
            .flatMapCompletable {
                val name = name_text.text.toString().trim()
                val username = username_text.text.toString().trim()
                val password = password_text.text.toString().trim()
                viewModel.register(name, username, password)
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
                name_layout.isEnabled = !it
                username_layout.isEnabled = !it
                password_layout.isEnabled = !it
                login.isEnabled = !it
                button.isEnabled = !it
            }

        disposables += Observables.combineLatest(
            viewModel.activityIndicator.map { !it },
            name_text.textChanges().map { it.trim().isNotEmpty() },
            username_text.textChanges().map { it.trim().isNotEmpty() },
            password_text.textChanges().map { it.trim().isNotEmpty() }) { a, b, c, d -> a && b && c && d }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { button.isEnabled = it }

        disposables += viewModel.activityIndicator.subscribe(progress_circular.visibility())
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }
}