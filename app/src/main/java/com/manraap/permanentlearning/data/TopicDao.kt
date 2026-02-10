package com.manraap.permanentlearning.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface TopicDao {
    @Query("SELECT * FROM topics ORDER BY nextRevisionAt ASC")
    fun getAllTopics(): LiveData<List<TopicEntity>>

    @Query("SELECT * FROM topics WHERE id = :topicId")
    fun getTopicById(topicId: Long): LiveData<TopicEntity?>

    @Query("SELECT * FROM topics WHERE id = :topicId")
    suspend fun getTopicNow(topicId: Long): TopicEntity?

    @Transaction
    @Query("SELECT * FROM topics WHERE id = :topicId")
    fun getTopicWithHistory(topicId: Long): LiveData<TopicWithHistory?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: TopicEntity): Long

    @Update
    suspend fun updateTopic(topic: TopicEntity)

    @Insert
    suspend fun insertHistory(history: RevisionHistoryEntity)

    @Query("SELECT * FROM topics WHERE nextRevisionAt <= :now ORDER BY nextRevisionAt ASC")
    suspend fun getDueTopics(now: Long): List<TopicEntity>

    @Query("SELECT COUNT(*) FROM topics WHERE nextRevisionAt - createdAt < :thresholdMs")
    fun getShortTermCount(thresholdMs: Long): LiveData<Int>

    @Query("SELECT COUNT(*) FROM topics WHERE nextRevisionAt - createdAt >= :thresholdMs")
    fun getLongTermCount(thresholdMs: Long): LiveData<Int>

    @Query("SELECT COUNT(*) FROM topics WHERE lastRecallQuality = :failedValue")
    fun getWeakTopicsCount(failedValue: String = "DID_NOT_REMEMBER"): LiveData<Int>

    @Query("SELECT * FROM topics WHERE nextRevisionAt <= :now ORDER BY nextRevisionAt ASC LIMIT 1")
    suspend fun getNextDueTopic(now: Long): TopicEntity?
}
