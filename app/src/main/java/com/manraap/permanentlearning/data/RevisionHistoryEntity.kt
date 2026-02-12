package com.manraap.permanentlearning.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "revision_history",
    foreignKeys = [
        ForeignKey(
            entity = TopicEntity::class,
            parentColumns = ["id"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("topicId"), Index("reviewedAt")]
)
data class RevisionHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topicId: Long,
    val reviewedAt: Long,
    val previousStage: Int,
    val newStage: Int,
    val recallQuality: RecallQuality,
    val errorNote: String = ""
)
