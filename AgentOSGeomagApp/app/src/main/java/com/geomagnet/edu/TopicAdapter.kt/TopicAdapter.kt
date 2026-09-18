package com.geomagnet.edu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geomagnet.edu.databinding.ItemTopicBinding

/** 主题卡片列表适配器 */
class TopicAdapter(
    private val topics: List<GeomagTopic>,
    private val onClick: (GeomagTopic) -> Unit
) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    inner class TopicViewHolder(val binding: ItemTopicBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val binding = ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopicViewHolder(binding)
    }

    override fun getItemCount(): Int = topics.size

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = topics[position]
        holder.binding.itemTitle.text = topic.title
        holder.binding.itemDesc.text = topic.subtitle
        holder.binding.root.setOnClickListener { onClick(topic) }
    }
}
