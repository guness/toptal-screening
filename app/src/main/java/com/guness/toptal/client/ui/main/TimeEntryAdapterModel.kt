package com.guness.toptal.client.ui.main

import com.guness.toptal.client.R
import com.guness.toptal.client.utils.Title
import com.guness.toptal.client.utils.listView.AdapterModel
import com.guness.toptal.client.utils.listView.HeaderItemViewModel
import com.guness.toptal.protocol.dto.TimeEntry

class TimeEntryAdapterModel(list: List<TimeEntry>, manager: Boolean, val onClick: (TimeEntry) -> Unit) : AdapterModel<EntryItemLayout> {

    override val items: Array<EntryItemLayout>

    init {
        val layouts = mutableListOf<EntryItemLayout>()
        if (manager) {
            val map = list.groupBy { it.userId ?: 0 }
            map.keys.forEach {
                layouts += EntryItemLayout.Header(HeaderItemViewModel(Title.Text("UserID: $it")))
                map[it]?.forEach {
                    layouts += EntryItemLayout.TimeEntryLayout(TimeEntryItemViewModel(it) {
                        onClick(it)
                    })
                }
            }
        } else {
            layouts += EntryItemLayout.Header(HeaderItemViewModel(Title.Resource(R.string.saved_clocks)))
            layouts += list.map {
                EntryItemLayout.TimeEntryLayout(TimeEntryItemViewModel(it) {
                    onClick(it)
                })
            }
        }
        items = layouts.toTypedArray()
    }
}