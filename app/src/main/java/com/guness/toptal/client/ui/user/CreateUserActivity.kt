package com.guness.toptal.client.ui.user

import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseActivity
import com.guness.toptal.client.utils.extensions.configureDropDownMenu
import com.guness.toptal.protocol.dto.UserRole
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.content_create_user.*
import java.util.*

class CreateUserActivity : BaseActivity<CreateUserViewModel>(CreateUserViewModel::class, R.layout.activity_create_user) {

    override fun initView() {

        role_text.configureDropDownMenu(UserRole.values().toList()) { it.name }
            .map { Optional.of(it) }
            .subscribe(viewModel.role)

        disposables += Observables
            .combineLatest(
                viewModel.role,
                name_text.textChanges(),
                username_text.textChanges(),
                password_text.textChanges(),
                viewModel.activityIndicator
            ) { a, b, c, d, e ->
                !e && a.isPresent && b.isNotBlank() && c.isNotBlank() && d.isNotBlank()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { button.isEnabled = it }

        disposables += button.clicks()
            .flatMapSingle {
                viewModel.createUser(name_text.text!!.toString(), username_text.text!!.toString(), password_text.text!!.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { finish() }
            }
            .subscribe()

        disposables += role_text.textChanges().subscribe {
            viewModel.role.onNext(Optional.empty())
        }

        disposables += viewModel.activityIndicator
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                role_layout.isEnabled = !it
                name_layout.isEnabled = !it
                username_layout.isEnabled = !it
                password_layout.isEnabled = !it
                button.isEnabled = !it
            }
    }
}