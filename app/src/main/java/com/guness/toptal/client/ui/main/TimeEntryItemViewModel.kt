package com.guness.toptal.client.ui.main

import android.view.View
import com.guness.toptal.client.utils.listView.IdentifableViewModel
import com.guness.toptal.client.utils.listView.ViewBinder
import com.guness.toptal.protocol.dto.TimeEntry
import kotlinx.android.synthetic.main.item_row_time_entry.view.*

class TimeEntryItemViewModel(private val timeEntry: TimeEntry, val onClick: () -> Unit) : IdentifableViewModel, ViewBinder {

    override val identity = timeEntry.id
    override val hashCode = timeEntry.hashCode()

    override fun bind(view: View) {
        view.setOnClickListener { onClick() }
        view.clock.timeZone = timeEntry.timeZone.id
        view.name.text = timeEntry.name
        view.city.text = timeEntry.city
        view.offset.text = timeEntry.diff
    }
}