package com.manraap.permanentlearning.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manraap.permanentlearning.data.TopicRepository
import com.manraap.permanentlearning.ui.detail.TopicDetailViewModel
import com.manraap.permanentlearning.ui.home.HomeViewModel
import com.manraap.permanentlearning.ui.progress.ProgressViewModel
import com.manraap.permanentlearning.ui.recall.RecallViewModel
import com.manraap.permanentlearning.ui.topic.AddEditTopicViewModel

class AppViewModelFactory(private val repository: TopicRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository) as T
            modelClass.isAssignableFrom(AddEditTopicViewModel::class.java) -> AddEditTopicViewModel(repository) as T
            modelClass.isAssignableFrom(RecallViewModel::class.java) -> RecallViewModel(repository) as T
            modelClass.isAssignableFrom(ProgressViewModel::class.java) -> ProgressViewModel(repository) as T
            modelClass.isAssignableFrom(TopicDetailViewModel::class.java) -> TopicDetailViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
