package com.guness.toptal.client.utils.listView

interface AdapterModel<L : Layout> {

    val items: Array<L>

    fun viewModelAt(position: Int): Any? {
        return items.getOrNull(position)?.let { viewModelFor(it) }
    }

    fun viewModelFor(item: L?): Any? {
        return item?.let { it as? HasViewModel }?.viewModel
    }
}