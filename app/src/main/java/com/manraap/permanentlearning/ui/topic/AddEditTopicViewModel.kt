package com.manraap.permanentlearning.ui.topic

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manraap.permanentlearning.data.TopicEntity
import com.manraap.permanentlearning.data.TopicRepository
import kotlinx.coroutines.launch

class AddEditTopicViewModel(private val repository: TopicRepository) : ViewModel() {
    fun getTopic(topicId: Long): LiveData<TopicEntity?> = repository.getTopicById(topicId)

    fun saveTopic(
        topicId: Long?,
        title: String,
        explanation: String,
        keywords: String,
        questions: List<String>,
        errorLog: String,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            if (topicId == null) {
                repository.addTopic(title, explanation, keywords, questions, errorLog)
            } else {
                repository.updateTopic(topicId, title, explanation, keywords, questions, errorLog)
            }
            onComplete()
        }
    }
}
