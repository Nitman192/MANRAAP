package com.manraap.permanentlearning.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manraap.permanentlearning.data.TopicEntity
import com.manraap.permanentlearning.databinding.ItemTopicBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TopicAdapter(
    private val onRecallClick: (TopicEntity) -> Unit,
    private val onDetailClick: (TopicEntity) -> Unit
) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    private val topics = mutableListOf<TopicEntity>()
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    fun submitList(newItems: List<TopicEntity>) {
        topics.clear()
        topics.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val binding = ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) = holder.bind(topics[position])

    override fun getItemCount(): Int = topics.size

    inner class TopicViewHolder(private val binding: ItemTopicBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(topic: TopicEntity) {
            binding.topicTitle.text = topic.title
            binding.topicStage.text = "Stage ${topic.currentStage + 1}/6"
            binding.nextRevision.text = "Next: ${formatter.format(Date(topic.nextRevisionAt))}"
            binding.buttonRecall.setOnClickListener { onRecallClick(topic) }
            binding.root.setOnClickListener { onDetailClick(topic) }
        }
    }
}
