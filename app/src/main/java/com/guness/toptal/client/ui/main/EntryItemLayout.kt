package com.guness.toptal.client.ui.main

import com.guness.toptal.client.R
import com.guness.toptal.client.utils.listView.HasViewModel
import com.guness.toptal.client.utils.listView.HeaderItemViewModel
import com.guness.toptal.client.utils.listView.IdentifableLayout

sealed class EntryItemLayout(override val layoutId: Int) : IdentifableLayout, HasViewModel {
    class Header(override val viewModel: HeaderItemViewModel) : EntryItemLayout(R.layout.item_row_time_entry_header)
    class TimeEntryLayout(override val viewModel: TimeEntryItemViewModel) : EntryItemLayout(R.layout.item_row_time_entry)

    override val identity: Long
        get() = when (this) {
            is Header -> viewModel.title.hashCode.toLong()
            is TimeEntryLayout -> viewModel.identity
        }
    override val hashCode: Int
        get() = when (this) {
            is Header -> viewModel.title.hashCode
            is TimeEntryLayout -> viewModel.hashCode
        }
}