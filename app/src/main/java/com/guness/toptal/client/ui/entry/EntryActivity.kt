package com.guness.toptal.client.ui.entry

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseActivity
import com.guness.toptal.client.utils.extensions.configureDropDownMenu
import com.guness.toptal.client.utils.extensions.put
import com.guness.toptal.client.utils.extensions.serializable
import com.guness.toptal.protocol.dto.TimeEntry
import com.guness.toptal.protocol.dto.name
import com.guness.toptal.protocol.dto.timeZoneCity
import com.guness.toptal.protocol.dto.validTimeZoneIDs
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.content_entry.*
import org.joda.time.DateTimeUtils
import org.joda.time.DateTimeZone
import java.util.*

class EntryActivity : BaseActivity<EntryViewModel>(EntryViewModel::class, R.layout.activity_entry) {

    override fun initView() {

        viewModel.timeZoneIDs()
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapObservable {
                input_edit_text.configureDropDownMenu(validTimeZoneIDs) { it.timeZoneCity() }
            }
            .map(DateTimeZone::forID)
            .map { Optional.of(it) }
            .subscribe(viewModel.timeZone)

        val initialEntry = intent.serializable<TimeEntry>()

        initialEntry?.let {
            viewModel.initialEntry.onNext(Optional.of(it))
            viewModel.timeZone.onNext(Optional.of(it.timeZone))
            input_edit_text.setText(it.timeZone.id.timeZoneCity())
            delete.visibility = View.VISIBLE

            disposables += viewModel.manager.flatMap { viewModel.loadUser(initialEntry.userId) }.subscribe()
        }

        disposables += viewModel.manager
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                user_text.isFocusable = it
                user_text.isFocusableInTouchMode = it
                user_text.isCursorVisible = it
                user_layout.isVisible = it
            }

        disposables += viewModel.users
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .flatMap { user_text.configureDropDownMenu(it) { "${it.name}(${it.username})" } }
            .doOnNext {
                viewModel.user.onNext(Optional.of(it))
            }
            .subscribe()

        disposables += Observables.combineLatest(viewModel.user, viewModel.timeZone)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val user = it.first
                if (it.second.isPresent) {
                    val timeZone = it.second.get()
                    name_text.setText(timeZone.name)
                    offset_text.setText(timeZone.gmt())
                    id_text.setText(timeZone.id)
                    clock.timeZone = timeZone.id
                    clock.visibility = View.VISIBLE
                    button.isEnabled = timeZone != initialEntry?.timeZone || initialEntry.userId != user.map { it.id }.orElse(0)
                    hideKeyboard()
                } else {
                    name_text.setText("")
                    offset_text.setText("")
                    id_text.setText("")
                    clock.visibility = View.INVISIBLE
                    button.isEnabled = false
                }
            }

        disposables += input_edit_text.textChanges().skip(1).subscribe {
            viewModel.timeZone.onNext(Optional.empty())
        }

        disposables += viewModel.user
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                user_text.setText(it.map { "${it.name}(${it.username})" }.orElse(""), false)
            }

        disposables += button.clicks()
            .flatMapSingle {
                viewModel.save(initialEntry == null)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                finish()
            }

        disposables += delete.clicks()
            .doOnNext {
                MaterialAlertDialogBuilder(this)
                    .setMessage(R.string.sure_to_delete)
                    .setPositiveButton(R.string.yes_delete) { _, _ ->
                        deleteRecord()
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
            .subscribe()
    }

    private fun deleteRecord() {
        disposables += viewModel.delete()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { finish() }
            .subscribe()
    }

    private fun hideKeyboard() {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(input_edit_text.windowToken, 0)
    }

    private fun DateTimeZone.gmt(): String {
        val diff = getOffset(DateTimeUtils.currentTimeMillis())
        val totalSeconds = diff / 1000
        val minutes = (totalSeconds / 60) % 60
        val hours = totalSeconds / 3600

        return String.format("GMT%+03d:%02d", hours, minutes)
    }

    companion object {
        fun newIntent(context: Context, entry: TimeEntry) = Intent(context, EntryActivity::class.java).put(entry)
    }
}

