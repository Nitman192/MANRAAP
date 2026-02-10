package com.manraap.permanentlearning.data

import androidx.lifecycle.LiveData
import com.manraap.permanentlearning.util.RevisionSchedule

class TopicRepository(private val dao: TopicDao) {
    fun getAllTopics(): LiveData<List<TopicEntity>> = dao.getAllTopics()
    fun getTopicById(topicId: Long): LiveData<TopicEntity?> = dao.getTopicById(topicId)
    fun getTopicWithHistory(topicId: Long): LiveData<TopicWithHistory?> = dao.getTopicWithHistory(topicId)

    fun getShortTermCount() = dao.getShortTermCount(30L * DAY_MS)
    fun getLongTermCount() = dao.getLongTermCount(30L * DAY_MS)
    fun getWeakTopicsCount() = dao.getWeakTopicsCount()

    suspend fun getDueTopics(now: Long): List<TopicEntity> = dao.getDueTopics(now)
    suspend fun getNextDueTopic(now: Long): TopicEntity? = dao.getNextDueTopic(now)

    suspend fun addTopic(
        title: String,
        explanation: String,
        keywords: String,
        questions: List<String>,
        errorLog: String
    ): Long {
        val now = System.currentTimeMillis()
        val initialStage = 0
        val topic = TopicEntity(
            title = title,
            explanation = explanation,
            keywords = keywords,
            questions = questions.joinToString(separator = "\n"),
            errorLog = errorLog,
            createdAt = now,
            nextRevisionAt = now + RevisionSchedule.stageToDelayMs(initialStage),
            currentStage = initialStage
        )
        return dao.insertTopic(topic)
    }

    suspend fun updateTopic(
        topicId: Long,
        title: String,
        explanation: String,
        keywords: String,
        questions: List<String>,
        errorLog: String
    ) {
        val existing = dao.getTopicNow(topicId) ?: return
        dao.updateTopic(
            existing.copy(
                title = title,
                explanation = explanation,
                keywords = keywords,
                questions = questions.joinToString(separator = "\n"),
                errorLog = errorLog
            )
        )
    }

    suspend fun applyRecallFeedback(topicId: Long, quality: RecallQuality, errorNote: String) {
        val topic = dao.getTopicNow(topicId) ?: return
        val previousStage = topic.currentStage
        val newStage = when (quality) {
            RecallQuality.DID_NOT_REMEMBER -> (previousStage - 1).coerceAtLeast(0)
            RecallQuality.PARTIALLY_REMEMBERED -> previousStage
            RecallQuality.FULLY_REMEMBERED -> (previousStage + 1).coerceAtMost(RevisionSchedule.intervalCount() - 1)
        }
        val now = System.currentTimeMillis()
        val nextRevisionAt = now + RevisionSchedule.stageToDelayMs(newStage)

        dao.updateTopic(
            topic.copy(
                currentStage = newStage,
                nextRevisionAt = nextRevisionAt,
                lastReviewedAt = now,
                lastRecallQuality = quality,
                errorLog = mergeErrorLog(topic.errorLog, errorNote)
            )
        )
        dao.insertHistory(
            RevisionHistoryEntity(
                topicId = topicId,
                reviewedAt = now,
                previousStage = previousStage,
                newStage = newStage,
                recallQuality = quality,
                errorNote = errorNote
            )
        )
    }

    private fun mergeErrorLog(current: String, incoming: String): String {
        if (incoming.isBlank()) return current
        val timestamp = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
            .format(java.util.Date())
        return (current.takeIf { it.isNotBlank() }?.plus("\n") ?: "") + "[$timestamp] $incoming"
    }

    companion object {
        private const val DAY_MS = 24L * 60L * 60L * 1000L
    }
}
