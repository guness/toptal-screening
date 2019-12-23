package com.guness.toptal.client.ui.entry

import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseActivity
import com.guness.toptal.protocol.dto.validTimeZoneIDs
import org.joda.time.DateTimeZone
import timber.log.Timber

class NewEntryActivity : BaseActivity<NewEntryViewModel>(NewEntryViewModel::class.java, R.layout.activity_new_entry) {

    override fun initView() {
        validTimeZoneIDs.forEach {
            val zone = DateTimeZone.forID(it)
            Timber.e("TimeZone $it + ${zone.getName(0)} + ${zone.getShortName(0)}")
        }
    }
}