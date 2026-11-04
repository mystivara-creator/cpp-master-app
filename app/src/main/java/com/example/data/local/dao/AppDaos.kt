package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.DailyChallengeRecordEntity
import com.example.data.local.entity.LessonProgressEntity
import com.example.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonProgressDao {
    @Query("SELECT * FROM lesson_progress")
    fun getAllProgress(): Flow<List<LessonProgressEntity>>

    @Query("SELECT * FROM lesson_progress WHERE lessonId = :lessonId LIMIT 1")
    suspend fun getProgress(lessonId: String): LessonProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: LessonProgressEntity)

    @Query("UPDATE lesson_progress SET isCompleted = 1, completedAt = :timestamp WHERE lessonId = :lessonId")
    suspend fun markCompleted(lessonId: String, timestamp: Long)

    @Query("UPDATE lesson_progress SET isBookmarked = :isBookmarked WHERE lessonId = :lessonId")
    suspend fun updateBookmark(lessonId: String, isBookmarked: Boolean)

    @Query("SELECT COUNT(*) FROM lesson_progress WHERE isCompleted = 1")
    fun getCompletedCount(): Flow<Int>

    @Query("DELETE FROM lesson_progress")
    suspend fun clearAll()
}

@Dao
interface DailyChallengeDao {
    @Query("SELECT * FROM daily_challenge_records")
    fun getAllRecords(): Flow<List<DailyChallengeRecordEntity>>

    @Query("SELECT * FROM daily_challenge_records WHERE challengeId = :challengeId LIMIT 1")
    suspend fun getRecord(challengeId: String): DailyChallengeRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(record: DailyChallengeRecordEntity)

    @Query("SELECT COUNT(*) FROM daily_challenge_records WHERE isPassed = 1")
    fun getSolvedCount(): Flow<Int>

    @Query("DELETE FROM daily_challenge_records")
    suspend fun clearAll()
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileDirect(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET totalXp = totalXp + :xp WHERE id = 1")
    suspend fun addXp(xp: Int)

    @Query("UPDATE user_profile SET quizzesCompleted = quizzesCompleted + 1 WHERE id = 1")
    suspend fun incrementQuizzesCompleted()

    @Query("DELETE FROM user_profile")
    suspend fun clearAll()
}
