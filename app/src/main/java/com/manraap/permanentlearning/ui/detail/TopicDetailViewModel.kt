package com.manraap.permanentlearning.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.manraap.permanentlearning.data.TopicRepository
import com.manraap.permanentlearning.data.TopicWithHistory

class TopicDetailViewModel(private val repository: TopicRepository) : ViewModel() {
    fun getTopicWithHistory(topicId: Long): LiveData<TopicWithHistory?> = repository.getTopicWithHistory(topicId)
}
