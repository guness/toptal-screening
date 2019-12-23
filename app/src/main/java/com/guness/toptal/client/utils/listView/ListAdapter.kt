package com.guness.toptal.client.utils.listView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

open class ListAdapter<L : IdentifableLayout>(private var model: AdapterModel<L>? = null) : RecyclerView.Adapter<ViewHolder>() {

    private val differ by lazy { AsyncListDiffer<L>(this, createDiffCallback()) }

    fun update(adapterModel: AdapterModel<L>) {
        model = adapterModel
        differ.submitList(adapterModel.items.asList())
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return getItemAt(position)?.layoutId ?: -1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = getItemAt(position)) {
            is ViewBinder -> holder.bind(item)
            is DisposableViewBinder -> holder.bind(item)
            else -> {
                model?.viewModelFor(item)?.let {
                    when (it) {
                        is ViewBinder -> holder.bind(it)
                        is DisposableViewBinder -> holder.bind(it)
                        else -> throw UnsupportedOperationException("Can't bind view model: $it")
                    }
                }
            }
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    fun getLayoutAt(position: Int): L? {
        return getItemAt(position)
    }

    protected fun getItemAt(position: Int): L? {
        return differ.currentList.getOrNull(position)
    }

    companion object {
        private fun <L : IdentifableLayout> createDiffCallback() = object : DiffUtil.ItemCallback<L>() {
            override fun areItemsTheSame(oldItem: L, newItem: L) = oldItem.identity == newItem.identity
            override fun areContentsTheSame(oldItem: L, newItem: L) = oldItem.hashCode == newItem.hashCode
        }
    }
}