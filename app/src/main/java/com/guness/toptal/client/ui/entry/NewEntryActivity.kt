package com.guness.toptal.client.ui.entry

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseActivity
import com.guness.toptal.client.utils.extensions.configureDropDownMenu
import com.guness.toptal.protocol.dto.name
import com.guness.toptal.protocol.dto.timeZoneCity
import com.guness.toptal.protocol.dto.validTimeZoneIDs
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_new_entry.*
import kotlinx.android.synthetic.main.content_new_entry.*
import org.joda.time.DateTimeUtils
import org.joda.time.DateTimeZone
import java.util.*

class NewEntryActivity : BaseActivity<NewEntryViewModel>(NewEntryViewModel::class, R.layout.activity_new_entry) {

    override fun initView() {
        toolbar.setNavigationOnClickListener { onBackPressed() }
        input_edit_text.configureDropDownMenu(validTimeZoneIDs) { it.timeZoneCity() }
            .map(DateTimeZone::forID)
            .map { Optional.of(it) }
            .subscribe(viewModel.timeZone)

        disposables += viewModel.timeZone.subscribe {
            if (it.isPresent) {
                name_text.setText(it.get().name)
                offset_text.setText(it.get().gmt())
                id_text.setText(it.get().id)
                clock.timeZone = it.get().id
                clock.visibility = View.VISIBLE
                save.isEnabled = true
                hideKeyboard()
            } else {
                name_text.setText("")
                offset_text.setText("")
                id_text.setText("")
                clock.visibility = View.INVISIBLE
                save.isEnabled = false
            }
        }

        disposables += input_edit_text.textChanges().subscribe {
            viewModel.timeZone.onNext(Optional.empty())
        }

        disposables += save.clicks()
            .flatMapMaybe {
                viewModel.save()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                finish()
            }

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
}

