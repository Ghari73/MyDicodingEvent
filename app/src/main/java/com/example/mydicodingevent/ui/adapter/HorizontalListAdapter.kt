package com.example.mydicodingevent.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mydicodingevent.data.response.ListEventsItem
import com.example.mydicodingevent.databinding.ItemEventBinding
import com.example.mydicodingevent.databinding.ItemHomeHorizontalBinding
import com.example.mydicodingevent.ui.DetailActivity

class HorizontalListAdapter(): ListAdapter<ListEventsItem, HorizontalListAdapter.MyViewHorizontalHolder>(
    DIFF_CALLBACK
) {
    class MyViewHorizontalHolder(val binding: ItemHomeHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(event: ListEventsItem){
            binding.tvItemName.text = event.name
            Glide.with(binding.root)
                .load(event.imageLogo)
                .into(binding.imgItemPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHorizontalHolder {
        val binding = ItemHomeHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHorizontalHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHorizontalHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.KEY_ID, event)
            intentDetail.putExtra(DetailActivity.INSTANCE_FRAGMENT, 1)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}