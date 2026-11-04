package com.example.data.model

enum class CourseLevel(val label: String, val badgeColor: Long) {
    DASAR("Dasar C++", 0xFF38BDF8),        // Cyan
    MENENGAH("Menengah C++", 0xFF818CF8),  // Indigo
    MAHIR("Mahir C++", 0xFFF59E0B)         // Amber/Gold
}

data class Lesson(
    val id: String,
    val level: CourseLevel,
    val order: Int,
    val title: String,
    val subtitle: String,
    val estimatedMinutes: Int,
    val summary: String,
    val sections: List<LessonSection>,
    val quickCheck: QuizQuestion? = null
)

sealed class LessonSection {
    data class Paragraph(val text: String) : LessonSection()
    data class Heading(val title: String) : LessonSection()
    data class CodeBlock(
        val code: String,
        val output: String? = null,
        val explanation: String? = null
    ) : LessonSection()
    data class TipBox(val title: String, val message: String) : LessonSection()
    data class WarningBox(val title: String, val message: String) : LessonSection()
    data class KeyPoints(val points: List<String>) : LessonSection()
}

data class QuizQuestion(
    val id: String,
    val category: String,
    val level: CourseLevel,
    val question: String,
    val codeSnippet: String? = null,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

enum class ChallengeDifficulty(val label: String, val xp: Int) {
    MUDAH("Mudah", 25),
    SEDANG("Sedang", 50),
    SULIT("Sulit", 100)
}

data class TestCase(
    val inputDescription: String,
    val simulatedInput: String,
    val expectedOutput: String,
    val isHidden: Boolean = false
)

data class DailyChallenge(
    val id: String,
    val dayNumber: Int,
    val title: String,
    val difficulty: ChallengeDifficulty,
    val tags: List<String>,
    val description: String,
    val inputSpecification: String,
    val outputSpecification: String,
    val exampleInput: String,
    val exampleOutput: String,
    val starterCode: String,
    val testCases: List<TestCase>,
    val hints: List<String>,
    val solutionCode: String,
    val solutionExplanation: String
)

data class UserProfile(
    val totalXp: Int = 0,
    val currentStreak: Int = 1,
    val bestStreak: Int = 1,
    val lastActiveDate: String = "",
    val completedLessons: Int = 0,
    val solvedChallenges: Int = 0,
    val quizzesCompleted: Int = 0
) {
    val levelTitle: String
        get() = when {
            totalXp >= 2000 -> "Guru C++ (Grandmaster)"
            totalXp >= 1200 -> "Arsitek Sistem C++"
            totalXp >= 700  -> "Pengembang Mahir"
            totalXp >= 300  -> "Pengembara Pointer"
            totalXp >= 100  -> "Pemrogram Magang"
            else            -> "Pemula C++"
        }

    val currentLevelNumber: Int
        get() = when {
            totalXp >= 2000 -> 5
            totalXp >= 1200 -> 4
            totalXp >= 700  -> 3
            totalXp >= 300  -> 2
            totalXp >= 100  -> 1
            else            -> 0
        }

    val nextLevelXp: Int
        get() = when {
            totalXp >= 2000 -> 3000
            totalXp >= 1200 -> 2000
            totalXp >= 700  -> 1200
            totalXp >= 300  -> 700
            totalXp >= 100  -> 300
            else            -> 100
        }
}

data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean
)
