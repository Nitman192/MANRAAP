package com.manraap.permanentlearning.data

import androidx.room.Embedded
import androidx.room.Relation

data class TopicWithHistory(
    @Embedded val topic: TopicEntity,
    @Relation(parentColumn = "id", entityColumn = "topicId")
    val history: List<RevisionHistoryEntity>
)
