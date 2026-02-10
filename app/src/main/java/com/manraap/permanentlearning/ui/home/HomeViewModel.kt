package com.manraap.permanentlearning.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manraap.permanentlearning.data.TopicEntity
import com.manraap.permanentlearning.data.TopicRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: TopicRepository) : ViewModel() {
    val topics: LiveData<List<TopicEntity>> = repository.getAllTopics()

    fun getNextDueTopic(onResult: (TopicEntity?) -> Unit) {
        viewModelScope.launch {
            onResult(repository.getNextDueTopic(System.currentTimeMillis()))
        }
    }
}
