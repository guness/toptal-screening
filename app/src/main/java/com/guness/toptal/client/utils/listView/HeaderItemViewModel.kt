package com.guness.toptal.client.utils.listView

import android.view.View
import com.guness.toptal.client.utils.Title
import com.guness.toptal.client.utils.bindTo
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_row_time_entry_header.view.*

class HeaderItemViewModel(val title: Title) : DisposableViewBinder {
    override fun bind(view: View): Disposable {
        return title.bindTo(view.text1)
    }
}
