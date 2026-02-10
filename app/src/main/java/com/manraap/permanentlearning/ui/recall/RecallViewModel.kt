package com.manraap.permanentlearning.ui.recall

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manraap.permanentlearning.data.RecallQuality
import com.manraap.permanentlearning.data.TopicEntity
import com.manraap.permanentlearning.data.TopicRepository
import kotlinx.coroutines.launch

class RecallViewModel(private val repository: TopicRepository) : ViewModel() {
    fun getTopic(topicId: Long): LiveData<TopicEntity?> = repository.getTopicById(topicId)

    fun submitFeedback(topicId: Long, quality: RecallQuality, errorNote: String, onSaved: () -> Unit) {
        viewModelScope.launch {
            repository.applyRecallFeedback(topicId, quality, errorNote)
            onSaved()
        }
    }
}
