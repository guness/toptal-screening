package com.guness.toptal.client.utils.listView

interface IdentifableLayout : Layout {
    val identity: Long
    val hashCode: Int
}