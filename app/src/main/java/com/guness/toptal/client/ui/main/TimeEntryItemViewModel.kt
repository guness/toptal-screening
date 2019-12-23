package com.guness.toptal.client.ui.main

import android.view.View
import com.guness.toptal.client.utils.listView.IdentifableViewModel
import com.guness.toptal.client.utils.listView.ViewBinder
import com.guness.toptal.protocol.dto.TimeEntry
import kotlinx.android.synthetic.main.item_row_time_entry.view.*

class TimeEntryItemViewModel(private val timeEntry: TimeEntry) : IdentifableViewModel, ViewBinder {

    override val identity = timeEntry.id
    override val hashCode = timeEntry.hashCode()

    override fun bind(view: View) {
        view.name.text = timeEntry.timeZone.getName(0)
        view.city.text = timeEntry.timeZone.id.split("/")[1]
        view.offset.text = timeEntry.timeZone.getOffsetFromLocal(0).toString()
    }
}