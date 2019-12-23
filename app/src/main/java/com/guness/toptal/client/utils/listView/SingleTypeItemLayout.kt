package com.guness.toptal.client.utils.listView

class SingleTypeItemLayout(override val layoutId: Int, override val viewModel: IdentifableViewModel) : IdentifableLayout, HasViewModel {

    override val identity: Long
        get() = viewModel.identity

    override val hashCode: Int
        get() = viewModel.hashCode

}