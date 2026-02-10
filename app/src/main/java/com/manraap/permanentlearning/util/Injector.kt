package com.manraap.permanentlearning.util

import android.content.Context
import com.manraap.permanentlearning.data.AppDatabase
import com.manraap.permanentlearning.data.TopicRepository

object Injector {
    fun provideRepository(context: Context): TopicRepository {
        return TopicRepository(AppDatabase.getInstance(context).topicDao())
    }
}
