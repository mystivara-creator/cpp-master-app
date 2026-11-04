package com.example

import com.example.data.CppChallengeData
import com.example.data.CppCodeSimulator
import com.example.data.CppCurriculumData
import com.example.data.CppQuizData
import com.example.data.model.CourseLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testCurriculumIntegrity() {
        val lessons = CppCurriculumData.lessons
        assertEquals(19, lessons.size)

        val dasarCount = lessons.count { it.level == CourseLevel.DASAR }
        val menengahCount = lessons.count { it.level == CourseLevel.MENENGAH }
        val mahirCount = lessons.count { it.level == CourseLevel.MAHIR }

        assertEquals(6, dasarCount)
        assertEquals(6, menengahCount)
        assertEquals(7, mahirCount)
    }

    @Test
    fun testQuizQuestionValidity() {
        val questions = CppQuizData.allQuestions
        assertTrue(questions.size >= 15)

        for (q in questions) {
            assertTrue("Pertanyaan ${q.id} harus memiliki opsi", q.options.isNotEmpty())
            assertTrue(
                "Indeks jawaban benar ${q.id} harus valid",
                q.correctIndex in q.options.indices
            )
            assertTrue("Penjelasan ${q.id} tidak boleh kosong", q.explanation.isNotBlank())
        }
    }

    @Test
    fun testDailyChallengesValidity() {
        val challenges = CppChallengeData.challenges
        assertTrue(challenges.isNotEmpty())

        challenges.forEach { ch ->
            assertTrue("Starter code tidak boleh kosong", ch.starterCode.contains("main"))
            assertTrue("Test cases harus ada", ch.testCases.isNotEmpty())
            assertNotNull(ch.solutionCode)
        }
    }

    @Test
    fun testCppCodeSimulatorSyntax() {
        val invalidCode = "int main() { return 0;" // Missing '}'
        val result = CppCodeSimulator.runPlayground(invalidCode)
        assertFalse(result.isSuccess)
        assertNotNull(result.errorMessage)

        val validCode = """#include <iostream>
int main() {
    std::cout << "Test Sukses";
    return 0;
}"""
        val validResult = CppCodeSimulator.runPlayground(validCode)
        assertTrue(validResult.isSuccess)
        assertTrue(validResult.output.contains("Test Sukses"))
    }

    @Test
    fun testEvaluateChallengeDay1() {
        val ch1 = CppChallengeData.challenges[0]
        val result = CppCodeSimulator.evaluateChallenge(ch1, ch1.solutionCode)
        assertTrue("Solusi model hari 1 harus lolos semua test case", result.isSuccess)
        assertEquals(ch1.testCases.size, result.testCaseResults.size)
        assertTrue(result.testCaseResults.all { it.isPassed })
    }
}
