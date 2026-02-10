package com.manraap.permanentlearning.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manraap.permanentlearning.data.RevisionHistoryEntity
import com.manraap.permanentlearning.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private val items = mutableListOf<RevisionHistoryEntity>()
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun submitList(history: List<RevisionHistoryEntity>) {
        items.clear()
        items.addAll(history.sortedByDescending { it.reviewedAt })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RevisionHistoryEntity) {
            binding.historyDate.text = formatter.format(Date(item.reviewedAt))
            binding.historyQuality.text = item.recallQuality.name.replace("_", " ")
            binding.historyStageChange.text = "Stage ${item.previousStage + 1} → ${item.newStage + 1}"
            binding.historyError.text = if (item.errorNote.isBlank()) "No error note" else item.errorNote
        }
    }
}
