package com.example.data

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.entity.DailyChallengeRecordEntity
import com.example.data.local.entity.LessonProgressEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.CourseLevel
import com.example.data.model.DailyChallenge
import com.example.data.model.Lesson
import com.example.data.model.QuizQuestion
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CppRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val lessonDao = db.lessonProgressDao()
    private val challengeDao = db.dailyChallengeDao()
    private val profileDao = db.userProfileDao()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // 1. Lessons & Progress
    val allLessons: List<Lesson> = CppCurriculumData.lessons

    fun getLessonsByLevel(level: CourseLevel): List<Lesson> {
        return allLessons.filter { it.level == level }
    }

    fun getLessonById(id: String): Lesson? {
        return allLessons.find { it.id == id }
    }

    val lessonProgressFlow: Flow<Map<String, LessonProgressEntity>> =
        lessonDao.getAllProgress().map { list ->
            list.associateBy { it.lessonId }
        }

    suspend fun markLessonCompleted(lessonId: String) {
        val existing = lessonDao.getProgress(lessonId)
        val wasAlreadyCompleted = existing?.isCompleted == true
        val now = System.currentTimeMillis()

        if (existing == null) {
            lessonDao.insertOrUpdate(
                LessonProgressEntity(
                    lessonId = lessonId,
                    isCompleted = true,
                    completedAt = now
                )
            )
        } else {
            lessonDao.markCompleted(lessonId, now)
        }

        if (!wasAlreadyCompleted) {
            addXp(25)
            updateActiveStreak()
        }
    }

    suspend fun toggleBookmark(lessonId: String, currentBookmarked: Boolean) {
        val existing = lessonDao.getProgress(lessonId)
        val newBookmark = !currentBookmarked
        if (existing == null) {
            lessonDao.insertOrUpdate(
                LessonProgressEntity(
                    lessonId = lessonId,
                    isBookmarked = newBookmark
                )
            )
        } else {
            lessonDao.updateBookmark(lessonId, newBookmark)
        }
    }

    // 2. Daily Challenges
    val allChallenges: List<DailyChallenge> = CppChallengeData.challenges

    fun getTodayChallenge(): DailyChallenge {
        return CppChallengeData.getTodayChallenge()
    }

    fun getChallengeForDay(day: Int): DailyChallenge {
        return CppChallengeData.getChallengeForDay(day)
    }

    val challengeRecordsFlow: Flow<Map<String, DailyChallengeRecordEntity>> =
        challengeDao.getAllRecords().map { list ->
            list.associateBy { it.challengeId }
        }

    suspend fun saveChallengeSubmission(
        challenge: DailyChallenge,
        code: String,
        isPassed: Boolean,
        passedCases: Int,
        totalCases: Int
    ) {
        val todayStr = dateFormat.format(Date())
        val existing = challengeDao.getRecord(challenge.id)
        val wasAlreadyPassed = existing?.isPassed == true

        val record = DailyChallengeRecordEntity(
            challengeId = challenge.id,
            dayNumber = challenge.dayNumber,
            dateSolved = todayStr,
            isPassed = isPassed,
            submittedCode = code,
            testCasesPassed = passedCases,
            totalTestCases = totalCases,
            solvedTimestamp = System.currentTimeMillis()
        )
        challengeDao.insertOrUpdate(record)

        if (isPassed && !wasAlreadyPassed) {
            addXp(challenge.difficulty.xp)
            updateActiveStreak()
        }
    }

    // 3. User Profile & Streak
    val userProfileFlow: Flow<UserProfile> = combine(
        profileDao.getUserProfile(),
        lessonDao.getCompletedCount(),
        challengeDao.getSolvedCount()
    ) { profileEntity, completedLessons, solvedChallenges ->
        val entity = profileEntity ?: UserProfileEntity()
        UserProfile(
            totalXp = entity.totalXp,
            currentStreak = entity.currentStreak,
            bestStreak = entity.bestStreak,
            lastActiveDate = entity.lastActiveDate,
            completedLessons = completedLessons,
            solvedChallenges = solvedChallenges,
            quizzesCompleted = entity.quizzesCompleted
        )
    }

    suspend fun addXp(amount: Int) {
        ensureProfileInitialized()
        profileDao.addXp(amount)
    }

    suspend fun recordQuizCompletion(score: Int, total: Int) {
        ensureProfileInitialized()
        profileDao.incrementQuizzesCompleted()
        val earnedXp = (score * 10) + 15
        profileDao.addXp(earnedXp)
        updateActiveStreak()
    }

    private suspend fun ensureProfileInitialized() {
        val existing = profileDao.getUserProfileDirect()
        if (existing == null) {
            val todayStr = dateFormat.format(Date())
            profileDao.insertOrUpdate(
                UserProfileEntity(
                    id = 1,
                    totalXp = 50, // Initial welcome bonus
                    currentStreak = 1,
                    bestStreak = 1,
                    lastActiveDate = todayStr,
                    quizzesCompleted = 0
                )
            )
        }
    }

    private suspend fun updateActiveStreak() {
        ensureProfileInitialized()
        val current = profileDao.getUserProfileDirect() ?: return
        val todayStr = dateFormat.format(Date())

        if (current.lastActiveDate == todayStr) {
            // Already active today
            return
        }

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayStr = dateFormat.format(cal.time)

        val newStreak = if (current.lastActiveDate == yesterdayStr) {
            current.currentStreak + 1
        } else {
            1
        }

        val newBest = maxOf(newStreak, current.bestStreak)

        profileDao.insertOrUpdate(
            current.copy(
                currentStreak = newStreak,
                bestStreak = newBest,
                lastActiveDate = todayStr
            )
        )
    }

    suspend fun resetAllData() {
        lessonDao.clearAll()
        challengeDao.clearAll()
        profileDao.clearAll()
        ensureProfileInitialized()
    }
}
