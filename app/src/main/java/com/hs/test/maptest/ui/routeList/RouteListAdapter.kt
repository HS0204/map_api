package com.hs.test.maptest.ui.routeList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hs.test.maptest.data.RouteInfo
import com.hs.test.maptest.databinding.ItemPathBinding
import com.hs.test.maptest.util.formatTime

class RouteListAdapter(private val interaction: OnRouteClickListener) : ListAdapter<RouteInfo, RouteListAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteListAdapter.ViewHolder {
        return ViewHolder(
            ItemPathBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RouteListAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(private val binding: ItemPathBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(routeInfo: RouteInfo, position: Int) {
            with(binding) {
                route = routeInfo.copy(date = routeInfo.date.formatTime())
                tvPathIndex.text = position.plus(1).toString()      // todo : 데이터 클래스에 index 넣기
                root.setOnClickListener { interaction.onRouteClick(routeInfo) }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<RouteInfo>() {
            override fun areItemsTheSame(oldItem: RouteInfo, newItem: RouteInfo): Boolean {
                return oldItem.userId == newItem.userId && oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: RouteInfo, newItem: RouteInfo): Boolean {
                return oldItem == newItem
            }
        }
    }
}