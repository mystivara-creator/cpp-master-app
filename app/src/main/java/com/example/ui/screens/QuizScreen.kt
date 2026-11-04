package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CourseLevel
import com.example.ui.components.CppSyntaxHighlighter
import com.example.ui.theme.CppAmberWarning
import com.example.ui.theme.CppBluePrimary
import com.example.ui.theme.CppCodeBackground
import com.example.ui.theme.CppEmeraldTertiary
import com.example.ui.theme.CppNavyBorder
import com.example.ui.theme.CppNavyCard
import com.example.ui.theme.CppNavyDark
import com.example.ui.theme.CppNavySurface
import com.example.ui.theme.CppRoseError
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.CppViewModel
import com.example.ui.viewmodel.QuizSessionState
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun QuizScreen(
    viewModel: CppViewModel,
    modifier: Modifier = Modifier
) {
    val quizSession by viewModel.quizSession.collectAsState()

    if (quizSession != null) {
        ActiveQuizView(
            session = quizSession!!,
            viewModel = viewModel,
            modifier = modifier
        )
    } else {
        QuizHubView(
            viewModel = viewModel,
            modifier = modifier
        )
    }
}

@Composable
fun QuizHubView(
    viewModel: CppViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("quiz_hub_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Kuis Interaktif C++",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = CppBluePrimary
                        )
                    )
                    Text(
                        text = "Uji pemahaman sintaks, logika, & tebak output",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CppNavyCard),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Quiz,
                        contentDescription = "Kuis",
                        tint = CppBluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Speed Quiz Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.startSpeedQuiz() }
                    .testTag("speed_quiz_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2349)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(CppBluePrimary, Color(0xFF818CF8))
                    )
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(CppBluePrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Kilat",
                            tint = CppBluePrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Kuis Kilat Acak (5 Soal)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = CppAmberWarning.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "+50 XP",
                                    color = CppAmberWarning,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Latihan cepat campuran dari semua tingkat (tebak output & konsep sintaks).",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }
            }
        }

        // Section Title: Ujian Tingkat
        item {
            Text(
                text = "Ujian Tingkat Penguasaan C++",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
        }

        // Level Quizzes List
        CourseLevel.values().forEach { level ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.startLevelQuiz(level) }
                        .testTag("level_quiz_${level.name}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CppNavySurface),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(level.badgeColor).copy(alpha = 0.4f),
                                CppNavyBorder
                            )
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(level.badgeColor).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = null,
                                tint = Color(level.badgeColor),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ujian Tingkat: ${level.label}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = when (level) {
                                    CourseLevel.DASAR -> "Sintaks, operator, loop, tipe data, & percabangan"
                                    CourseLevel.MENENGAH -> "Pointer, memori heap, struct, enum class, & class OOP"
                                    CourseLevel.MAHIR -> "Polimorfisme virtual, smart pointer, templates, & STL"
                                },
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        Button(
                            onClick = { viewModel.startLevelQuiz(level) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(level.badgeColor).copy(alpha = 0.2f),
                                contentColor = Color(level.badgeColor)
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Mulai", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Quiz History / Stats Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CppNavyCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${userProfile.quizzesCompleted}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = CppBluePrimary
                            )
                        )
                        Text(
                            text = "Kuis Diselesaikan",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${userProfile.quizzesCompleted * 35} XP",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = CppAmberWarning
                            )
                        )
                        Text(
                            text = "XP dari Kuis",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ActiveQuizView(
    session: QuizSessionState,
    viewModel: CppViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateTo(ScreenDestination.QuizHub)
    }

    if (session.isCompleted) {
        QuizCompletionView(session = session, viewModel = viewModel, modifier = modifier)
        return
    }

    val currentQ = session.currentQuestion ?: return
    val progress = (session.currentIndex + 1).toFloat() / session.questions.size.toFloat()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("active_quiz_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Navigation bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(ScreenDestination.QuizHub) },
                    modifier = Modifier.testTag("quiz_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Keluar Kuis",
                        tint = TextPrimary
                    )
                }

                Text(
                    text = "Soal ${session.currentIndex + 1} dari ${session.questions.size}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CppBluePrimary.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = currentQ.category,
                        color = CppBluePrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Progress Indicator
        item {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = CppBluePrimary,
                trackColor = CppNavyBorder
            )
        }

        // Question Statement
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CppNavySurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = currentQ.question,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            lineHeight = 24.sp
                        )
                    )

                    // Optional Code Snippet
                    if (currentQ.codeSnippet != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(CppCodeBackground)
                                .padding(12.dp)
                        ) {
                            val highlightedSnippet = remember(currentQ.codeSnippet) {
                                CppSyntaxHighlighter.highlightCppCode(currentQ.codeSnippet)
                            }
                            Text(
                                text = highlightedSnippet,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Options List
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                currentQ.options.forEachIndexed { index, option ->
                    val isSelected = session.selectedOptionIndex == index
                    val isCorrect = index == currentQ.correctIndex

                    val borderColor = when {
                        session.isAnswerConfirmed && isCorrect -> CppEmeraldTertiary
                        session.isAnswerConfirmed && isSelected && !isCorrect -> CppRoseError
                        isSelected -> CppBluePrimary
                        else -> CppNavyBorder
                    }

                    val bgColor = when {
                        session.isAnswerConfirmed && isCorrect -> CppEmeraldTertiary.copy(alpha = 0.18f)
                        session.isAnswerConfirmed && isSelected && !isCorrect -> CppRoseError.copy(alpha = 0.18f)
                        isSelected -> CppBluePrimary.copy(alpha = 0.12f)
                        else -> CppNavySurface
                    }

                    Surface(
                        onClick = { viewModel.selectQuizOption(index) },
                        shape = RoundedCornerShape(14.dp),
                        color = bgColor,
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(borderColor)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("quiz_option_$index")
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            session.isAnswerConfirmed && isCorrect -> CppEmeraldTertiary
                                            session.isAnswerConfirmed && isSelected && !isCorrect -> CppRoseError
                                            isSelected -> CppBluePrimary
                                            else -> CppNavyBorder
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${('A'.code + index).toChar()}",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected || (session.isAnswerConfirmed && isCorrect)) Color(0xFF0F172A) else TextPrimary,
                                    fontSize = 13.sp
                                )
                            }

                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextPrimary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            if (session.isAnswerConfirmed && isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Benar",
                                    tint = CppEmeraldTertiary,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else if (session.isAnswerConfirmed && isSelected && !isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Salah",
                                    tint = CppRoseError,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Explanation Card (Visible after confirmation)
        if (session.isAnswerConfirmed) {
            item {
                val isCorrect = session.selectedOptionIndex == currentQ.correctIndex
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect) Color(0xFF052E16) else Color(0xFF3B0F15)
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(
                            if (isCorrect) CppEmeraldTertiary.copy(alpha = 0.5f) else CppRoseError.copy(alpha = 0.5f)
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = if (isCorrect) CppEmeraldTertiary else CppRoseError,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (isCorrect) "Jawaban Benar! 🎉" else "Jawaban Belum Tepat",
                                fontWeight = FontWeight.Bold,
                                color = if (isCorrect) CppEmeraldTertiary else CppRoseError,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = currentQ.explanation,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }
        }

        // Action Button: Konfirmasi or Selanjutnya
        item {
            if (!session.isAnswerConfirmed) {
                Button(
                    onClick = { viewModel.confirmQuizAnswer() },
                    enabled = session.selectedOptionIndex != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("quiz_confirm_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CppBluePrimary)
                ) {
                    Text(
                        text = "Konfirmasi Jawaban",
                        color = Color(0xFF0F172A),
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Button(
                    onClick = { viewModel.nextQuizQuestion() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("quiz_next_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CppBluePrimary)
                ) {
                    Text(
                        text = if (session.currentIndex + 1 < session.questions.size) "Soal Selanjutnya →" else "Lihat Hasil Kuis 🎉",
                        color = Color(0xFF0F172A),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun QuizCompletionView(
    session: QuizSessionState,
    viewModel: CppViewModel,
    modifier: Modifier = Modifier
) {
    val total = session.questions.size
    val correct = session.correctCount
    val percentage = ((correct.toFloat() / total.toFloat()) * 100).toInt()
    val earnedXp = (correct * 10) + 15

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(
                    if (percentage >= 70) CppEmeraldTertiary.copy(alpha = 0.2f) else CppAmberWarning.copy(alpha = 0.2f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = if (percentage >= 70) CppEmeraldTertiary else CppAmberWarning,
                modifier = Modifier.size(54.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (percentage >= 80) "Luar Biasa! Skor Sempurna!" else if (percentage >= 50) "Bagus! Terus Tingkatkan!" else "Jangan Menyerah, Pelajari Lagi!",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Anda menjawab benar $correct dari $total pertanyaan ($percentage%).",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // XP Reward Badge
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = CppAmberWarning.copy(alpha = 0.15f)
        ) {
            Text(
                text = "+$earnedXp XP Ditambahkan ke Profilmu!",
                color = CppAmberWarning,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.navigateTo(ScreenDestination.QuizHub) },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CppBluePrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("quiz_finish_back_btn")
        ) {
            Text("Kembali ke Menu Kuis", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
        }
    }
}
