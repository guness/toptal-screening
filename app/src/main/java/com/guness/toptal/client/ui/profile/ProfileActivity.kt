package com.guness.toptal.client.ui.profile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseActivity
import com.guness.toptal.client.utils.extensions.configureDropDownMenu
import com.guness.toptal.client.utils.extensions.put
import com.guness.toptal.client.utils.extensions.serializable
import com.guness.toptal.protocol.dto.User
import com.guness.toptal.protocol.dto.UserRole
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.content_profile.*
import kotlinx.android.synthetic.main.layout_new_password.view.*

class ProfileActivity : BaseActivity<ProfileViewModel>(ProfileViewModel::class, R.layout.activity_profile) {

    override fun initView() {

        role_text.configureDropDownMenu(UserRole.values().toList()) { it.name }
            .subscribe(viewModel.role)

        val user = intent.serializable<User>()
        if (user == null) {
            disposables += viewModel.initSelf().subscribe()
        } else {
            viewModel.user.onNext(user)
        }

        disposables += viewModel.user
            .doOnNext { viewModel.role.onNext(it.role) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                username.text = it.username
                name_text.setText(it.name)
                role_text.setText(it.role.name, false)
            }

        disposables += viewModel.entries.map { it.size }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                entries.text = it.toString()
            }

        disposables += Observables.combineLatest(viewModel.admin, viewModel.selfProfile)
            .map { it.first && !it.second }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                role_layout.isVisible = it
                delete.isVisible = it
            }

        disposables += Observables.combineLatest(viewModel.admin, viewModel.selfProfile)
            .map { it.first || it.second }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                name_text.isFocusable = it
                name_text.isFocusableInTouchMode = it
                name_text.isCursorVisible = it
            }

        disposables += Observables
            .combineLatest(
                viewModel.user, viewModel.role, name_text.textChanges(),
                viewModel.activityIndicator
            ) { a, b, c, d ->
                !d && (a.role != b || a.name != c.toString())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { button.isEnabled = it }

        disposables += button.clicks()
            .flatMapSingle {
                viewModel.save(name_text.text!!.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { finish() }
            }
            .subscribe()

        disposables += viewModel.activityIndicator
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                name_layout.isEnabled = !it
                role_layout.isEnabled = !it
                change_password.isEnabled = !it
            }

        disposables += change_password.clicks()
            .subscribe {

                val view = LayoutInflater.from(this).inflate(R.layout.layout_new_password, null)

                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.change_password)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.save) { _, _ ->
                        changePassword(view.input_text.text.toString())
                    }
                    .setView(view)
                    .show()
            }
    }

    private fun changePassword(password: String) {
        if (password.isNotBlank()) {
            disposables += viewModel.changePassword(password).subscribe()
        }
    }

    companion object {
        fun newIntent(context: Context, user: User? = null) = Intent(context, ProfileActivity::class.java)
            .also { intent -> user?.let(intent::put) }
    }
}