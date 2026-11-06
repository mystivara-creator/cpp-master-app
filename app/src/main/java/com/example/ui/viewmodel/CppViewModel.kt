package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CppCodeSimulator
import com.example.data.CppQuizData
import com.example.data.CppRepository
import com.example.data.ExecutionResult
import com.example.data.local.entity.DailyChallengeRecordEntity
import com.example.data.local.entity.LessonProgressEntity
import com.example.data.model.CourseLevel
import com.example.data.model.Lesson
import com.example.data.model.QuizQuestion
import com.example.data.model.UserProfile
import com.example.util.SystemIntegrationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class ScreenDestination {
    object Learn : ScreenDestination()
    data class LessonDetail(val lessonId: String) : ScreenDestination()
    object DailyChallenge : ScreenDestination()
    object QuizHub : ScreenDestination()
    data class ActiveQuiz(val title: String, val questions: List<QuizQuestion>) : ScreenDestination()
    object Playground : ScreenDestination()
    object Profile : ScreenDestination()
}

data class QuizSessionState(
    val title: String,
    val questions: List<QuizQuestion>,
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerConfirmed: Boolean = false,
    val correctCount: Int = 0,
    val isCompleted: Boolean = false
) {
    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(currentIndex)
}

class CppViewModel(application: Application) : AndroidViewModel(application) {
    val repository = CppRepository(application)

    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Startup Loading & First-Install Onboarding State
    private val _isAppInitializing = MutableStateFlow(true)
    val isAppInitializing: StateFlow<Boolean> = _isAppInitializing.asStateFlow()

    private val _startupProgress = MutableStateFlow(0.25f)
    val startupProgress: StateFlow<Float> = _startupProgress.asStateFlow()

    private val _startupStatusText = MutableStateFlow("Menyiapkan kurikulum C++20...")
    val startupStatusText: StateFlow<String> = _startupStatusText.asStateFlow()

    private val _hasCompletedOnboarding = MutableStateFlow(
        prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    )
    val hasCompletedOnboarding: StateFlow<Boolean> = _hasCompletedOnboarding.asStateFlow()

    // Current Navigation Screen
    private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Learn)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    // Database flows
    val userProfile: StateFlow<UserProfile> = repository.userProfileFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserProfile()
    )

    val lessonProgress: StateFlow<Map<String, LessonProgressEntity>> =
        repository.lessonProgressFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyMap()
        )

    val challengeRecords: StateFlow<Map<String, DailyChallengeRecordEntity>> =
        repository.challengeRecordsFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyMap()
        )

    // Learn Screen Filter & Search
    private val _selectedCourseLevel = MutableStateFlow(CourseLevel.DASAR)
    val selectedCourseLevel: StateFlow<CourseLevel> = _selectedCourseLevel.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Daily Challenge State
    private val _currentDay = MutableStateFlow(1)
    val currentDay: StateFlow<Int> = _currentDay.asStateFlow()

    private val _challengeCode = MutableStateFlow("")
    val challengeCode: StateFlow<String> = _challengeCode.asStateFlow()

    private val _challengeResult = MutableStateFlow<ExecutionResult?>(null)
    val challengeResult: StateFlow<ExecutionResult?> = _challengeResult.asStateFlow()

    private val _isChallengeRunning = MutableStateFlow(false)
    val isChallengeRunning: StateFlow<Boolean> = _isChallengeRunning.asStateFlow()

    // Quiz Session State
    private val _quizSession = MutableStateFlow<QuizSessionState?>(null)
    val quizSession: StateFlow<QuizSessionState?> = _quizSession.asStateFlow()

    // Playground State
    private val _playgroundCode = MutableStateFlow(
        """#include <iostream>
#include <vector>
#include <string>

int main() {
    std::cout << "Halo dari C++ Playground!\n";
    std::vector<int> angka = {10, 20, 30, 40, 50};
    
    std::cout << "Isi deret angka: ";
    for (int n : angka) {
        std::cout << n << " ";
    }
    std::cout << "\n";
    return 0;
}"""
    )
    val playgroundCode: StateFlow<String> = _playgroundCode.asStateFlow()

    private val _playgroundInput = MutableStateFlow("")
    val playgroundInput: StateFlow<String> = _playgroundInput.asStateFlow()

    private val _playgroundResult = MutableStateFlow<ExecutionResult?>(null)
    val playgroundResult: StateFlow<ExecutionResult?> = _playgroundResult.asStateFlow()

    private val _isPlaygroundRunning = MutableStateFlow(false)
    val isPlaygroundRunning: StateFlow<Boolean> = _isPlaygroundRunning.asStateFlow()

    init {
        val today = repository.getTodayChallenge()
        _currentDay.value = today.dayNumber
        _challengeCode.value = today.starterCode

        viewModelScope.launch {
            _startupProgress.value = 0.35f
            _startupStatusText.value = "Menyiapkan database & ruang kerja C++..."
            withContext(Dispatchers.IO) {
                runCatching {
                    repository.ensureProfileInitialized()
                    SystemIntegrationHelper.getWorkspaceDirectory(getApplication())
                }
            }
            _startupProgress.value = 0.75f
            _startupStatusText.value = "Memuat modul & simulator C++20..."
            delay(280L)
            _startupProgress.value = 1.0f
            _startupStatusText.value = "Siap digunakan"
            delay(120L)
            _isAppInitializing.value = false
        }
    }

    fun completeOnboarding() {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, true).apply()
        _hasCompletedOnboarding.value = true
    }

    fun reopenOnboarding() {
        _hasCompletedOnboarding.value = false
    }

    // Navigation
    fun navigateTo(destination: ScreenDestination) {
        _currentScreen.value = destination
    }

    fun navigateBack(): Boolean {
        return when (_currentScreen.value) {
            is ScreenDestination.LessonDetail -> {
                _currentScreen.value = ScreenDestination.Learn
                true
            }
            is ScreenDestination.ActiveQuiz -> {
                _currentScreen.value = ScreenDestination.QuizHub
                true
            }
            is ScreenDestination.DailyChallenge,
            is ScreenDestination.QuizHub,
            is ScreenDestination.Playground,
            is ScreenDestination.Profile -> {
                _currentScreen.value = ScreenDestination.Learn
                true
            }
            ScreenDestination.Learn -> false
        }
    }

    // Learn Filters
    fun setCourseLevel(level: CourseLevel) {
        _selectedCourseLevel.value = level
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun markLessonCompleted(lessonId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markLessonCompleted(lessonId)
        }
    }

    fun toggleLessonBookmark(lessonId: String, currentBookmarked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleBookmark(lessonId, currentBookmarked)
        }
    }

    // Daily Challenge Actions
    fun selectDay(day: Int) {
        _currentDay.value = day
        val challenge = repository.getChallengeForDay(day)
        val saved = challengeRecords.value[challenge.id]
        _challengeCode.value = if (saved != null && saved.submittedCode.isNotBlank()) {
            saved.submittedCode
        } else {
            challenge.starterCode
        }
        _challengeResult.value = null
    }

    fun updateChallengeCode(newCode: String) {
        _challengeCode.value = newCode
    }

    fun resetChallengeCode() {
        val challenge = repository.getChallengeForDay(_currentDay.value)
        _challengeCode.value = challenge.starterCode
        _challengeResult.value = null
    }

    fun runChallengeTests() {
        val challenge = repository.getChallengeForDay(_currentDay.value)
        val code = _challengeCode.value
        _isChallengeRunning.value = true

        viewModelScope.launch {
            val result = withContext(Dispatchers.Default) {
                CppCodeSimulator.evaluateChallenge(challenge, code)
            }
            _challengeResult.value = result
            _isChallengeRunning.value = false

            if (result.isSuccess) {
                withContext(Dispatchers.IO) {
                    repository.saveChallengeSubmission(
                        challenge = challenge,
                        code = code,
                        isPassed = true,
                        passedCases = challenge.testCases.size,
                        totalCases = challenge.testCases.size
                    )
                    SystemIntegrationHelper.saveCppFileToWorkspace(
                        getApplication(),
                        "challenge_day_${challenge.dayNumber}.cpp",
                        code
                    )
                }
            }
        }
    }

    // Quiz Session Actions
    fun startTopicQuiz(lesson: Lesson) {
        val questions = CppQuizData.allQuestions.filter {
            it.level == lesson.level
        }.take(5)
        startQuizSession("Kuis: ${lesson.title}", questions)
    }

    fun startLevelQuiz(level: CourseLevel) {
        val questions = CppQuizData.getQuestionsByLevel(level).shuffled().take(8)
        startQuizSession("Ujian Tingkat: ${level.label}", questions)
    }

    fun startSpeedQuiz() {
        val questions = CppQuizData.getRandomQuiz(5)
        startQuizSession("Tantangan Kuis Kilat C++", questions)
    }

    private fun startQuizSession(title: String, questions: List<QuizQuestion>) {
        if (questions.isEmpty()) return
        _quizSession.value = QuizSessionState(
            title = title,
            questions = questions,
            currentIndex = 0,
            selectedOptionIndex = null,
            isAnswerConfirmed = false,
            correctCount = 0,
            isCompleted = false
        )
        _currentScreen.value = ScreenDestination.ActiveQuiz(title, questions)
    }

    fun selectQuizOption(index: Int) {
        val current = _quizSession.value ?: return
        if (current.isAnswerConfirmed || current.isCompleted) return
        _quizSession.value = current.copy(selectedOptionIndex = index)
    }

    fun confirmQuizAnswer() {
        val current = _quizSession.value ?: return
        val q = current.currentQuestion ?: return
        val selected = current.selectedOptionIndex ?: return

        val isCorrect = (selected == q.correctIndex)
        val newCount = if (isCorrect) current.correctCount + 1 else current.correctCount

        _quizSession.value = current.copy(
            isAnswerConfirmed = true,
            correctCount = newCount
        )
    }

    fun nextQuizQuestion() {
        val current = _quizSession.value ?: return
        if (current.currentIndex + 1 < current.questions.size) {
            _quizSession.value = current.copy(
                currentIndex = current.currentIndex + 1,
                selectedOptionIndex = null,
                isAnswerConfirmed = false
            )
        } else {
            _quizSession.value = current.copy(isCompleted = true)
            viewModelScope.launch(Dispatchers.IO) {
                repository.recordQuizCompletion(current.correctCount, current.questions.size)
            }
        }
    }

    // Playground Actions
    fun updatePlaygroundCode(newCode: String) {
        _playgroundCode.value = newCode
    }

    fun updatePlaygroundInput(newInput: String) {
        _playgroundInput.value = newInput
    }

    fun runPlayground() {
        val code = _playgroundCode.value
        val input = _playgroundInput.value
        _isPlaygroundRunning.value = true
        viewModelScope.launch {
            val result = withContext(Dispatchers.Default) {
                CppCodeSimulator.runPlayground(code, input)
            }
            _playgroundResult.value = result
            _isPlaygroundRunning.value = false
            withContext(Dispatchers.IO) {
                SystemIntegrationHelper.saveCppFileToWorkspace(
                    getApplication(),
                    "playground_latest.cpp",
                    code
                )
            }
        }
    }

    fun loadPlaygroundTemplate(title: String, templateCode: String) {
        _playgroundCode.value = templateCode
        _playgroundResult.value = null
    }

    fun resetData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.resetAllData()
        }
    }

    companion object {
        const val PREFS_NAME = "cpp_master_prefs"
        const val KEY_ONBOARDING_COMPLETED = "has_completed_onboarding"
    }
}
