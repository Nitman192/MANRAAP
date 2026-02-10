package com.manraap.permanentlearning.ui.progress

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.manraap.permanentlearning.data.TopicRepository

class ProgressViewModel(repository: TopicRepository) : ViewModel() {
    val shortTerm: LiveData<Int> = repository.getShortTermCount()
    val longTerm: LiveData<Int> = repository.getLongTermCount()
    val weakTopics: LiveData<Int> = repository.getWeakTopicsCount()
}
