package com.guness.toptal.client.ui.users

import android.view.View
import com.guness.toptal.client.data.room.UserWithEntries
import com.guness.toptal.client.utils.listView.IdentifableViewModel
import com.guness.toptal.client.utils.listView.ViewBinder
import kotlinx.android.synthetic.main.item_row_user.view.*

class UserItemViewModel(private val user: UserWithEntries, val onClick: () -> Unit) : IdentifableViewModel, ViewBinder {

    override val identity = user.user.id
    override val hashCode = user.hashCode()

    override fun bind(view: View) {
        view.setOnClickListener { onClick() }
        view.name.text = user.user.name
        view.role.text = user.user.role.name
        view.entries.text = user.entries.toString()
    }
}