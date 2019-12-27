package com.guness.toptal.client.ui.main

import com.guness.toptal.client.R
import com.guness.toptal.client.utils.Title
import com.guness.toptal.client.utils.listView.AdapterModel
import com.guness.toptal.client.utils.listView.HeaderItemViewModel
import com.guness.toptal.protocol.dto.TimeEntry
import com.guness.toptal.protocol.dto.User

class TimeEntryAdapterModel(list: List<TimeEntry>, users: List<User>, manager: Boolean, val onClick: (TimeEntry) -> Unit) : AdapterModel<EntryItemLayout> {

    override val items: Array<EntryItemLayout>

    private val userMap = users.associateBy { it.id }

    init {
        val layouts = mutableListOf<EntryItemLayout>()
        if (manager) {
            val map = list.groupBy { it.userId ?: 0 }
            map.keys.forEach {
                layouts += EntryItemLayout.Header(HeaderItemViewModel(Title.Text(userMap[it]?.name ?: "-")))
                map[it]?.forEach { entry ->
                    layouts += EntryItemLayout.TimeEntryLayout(TimeEntryItemViewModel(entry) {
                        onClick(entry)
                    })
                }
            }
        } else {
            if (list.isNotEmpty()) {
                layouts += EntryItemLayout.Header(HeaderItemViewModel(Title.Resource(R.string.saved_clocks)))
                layouts += list.map {
                    EntryItemLayout.TimeEntryLayout(TimeEntryItemViewModel(it) {
                        onClick(it)
                    })
                }
            }
        }
        items = layouts.toTypedArray()
    }
}