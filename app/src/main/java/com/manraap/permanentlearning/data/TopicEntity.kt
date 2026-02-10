package com.manraap.permanentlearning.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "topics",
    indices = [Index("nextRevisionAt"), Index("currentStage"), Index("lastRecallQuality")]
)
data class TopicEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val explanation: String,
    val keywords: String,
    val questions: String,
    val errorLog: String = "",
    val createdAt: Long,
    val nextRevisionAt: Long,
    val currentStage: Int = 0,
    val lastReviewedAt: Long? = null,
    val lastRecallQuality: RecallQuality? = null
)
