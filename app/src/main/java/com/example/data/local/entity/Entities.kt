package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey val lessonId: String,
    val isCompleted: Boolean = false,
    val isBookmarked: Boolean = false,
    val score: Int = 0,
    val completedAt: Long = 0L
)

@Entity(tableName = "daily_challenge_records")
data class DailyChallengeRecordEntity(
    @PrimaryKey val challengeId: String,
    val dayNumber: Int,
    val dateSolved: String,
    val isPassed: Boolean = false,
    val submittedCode: String = "",
    val testCasesPassed: Int = 0,
    val totalTestCases: Int = 0,
    val solvedTimestamp: Long = 0L
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val totalXp: Int = 0,
    val currentStreak: Int = 1,
    val bestStreak: Int = 1,
    val lastActiveDate: String = "",
    val quizzesCompleted: Int = 0
)
