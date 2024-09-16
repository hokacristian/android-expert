package com.hoka.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hoka.core.R
import com.hoka.core.databinding.ItemListStoryBinding
import com.hoka.core.domain.model.Story

class StoryAdapter: RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    private var listData = ArrayList<Story>()
    var onItemClick: ((Story) -> Unit)? = null

    fun setData(newListData: List<Story>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_story, parent, false))

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListStoryBinding.bind(itemView)
        fun bind(data: Story) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(ivItemImage)
                tvItemTitle.text = data.name
                tvItemDescription.text = data.description
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}