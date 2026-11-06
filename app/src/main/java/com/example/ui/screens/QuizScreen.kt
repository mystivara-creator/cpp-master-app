package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CourseLevel
import com.example.ui.components.CppSyntaxHighlighter
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
    val colorScheme = MaterialTheme.colorScheme

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .testTag("quiz_hub_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Minimalist Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Kuis C++",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )
                    Text(
                        text = "Evaluasi sintaks, konsep OOP, dan tebak output",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Quiz,
                        contentDescription = "Kuis",
                        tint = colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Speed Quiz Card
        item {
            OutlinedCard(
                onClick = { viewModel.startSpeedQuiz() },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("speed_quiz_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = colorScheme.surfaceContainerLow
                ),
                border = BorderStroke(1.dp, colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Kilat",
                            tint = colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Kuis Kilat Acak (5 Soal)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onSurface
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = "+50 XP",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Medium,
                                    color = colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Pertanyaan acak dari seluruh tingkat kesulitan untuk latihan cepat.",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Ujian per Tingkat",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface
            )
        }

        CourseLevel.entries.forEach { level ->
            item {
                OutlinedCard(
                    onClick = { viewModel.startLevelQuiz(level) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("level_quiz_${level.name}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = colorScheme.surfaceContainerLow
                    ),
                    border = BorderStroke(1.dp, colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = null,
                                tint = colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Tingkat ${level.label}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = when (level) {
                                    CourseLevel.DASAR -> "Sintaks dasar, tipe data, percabangan, dan perulangan"
                                    CourseLevel.MENENGAH -> "Pointer, alokasi memori, struct, dan OOP"
                                    CourseLevel.MAHIR -> "Polimorfisme, smart pointer, template, dan STL"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onSurfaceVariant
                            )
                        }

                        FilledTonalButton(
                            onClick = { viewModel.startLevelQuiz(level) },
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text("Mulai", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }

        // Quiz Summary Stats
        item {
            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = colorScheme.surfaceContainerLow
                ),
                border = BorderStroke(1.dp, colorScheme.outlineVariant)
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
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = colorScheme.onSurface
                        )
                        Text(
                            text = "Kuis Selesai",
                            style = MaterialTheme.typography.labelSmall,
                            color = colorScheme.onSurfaceVariant
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${userProfile.quizzesCompleted * 35} XP",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = colorScheme.primary
                        )
                        Text(
                            text = "Total XP Kuis",
                            style = MaterialTheme.typography.labelSmall,
                            color = colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
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
    val colorScheme = MaterialTheme.colorScheme

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .testTag("active_quiz_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                        tint = colorScheme.onSurface
                    )
                }

                Text(
                    text = "Soal ${session.currentIndex + 1} / ${session.questions.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colorScheme.secondaryContainer
                ) {
                    Text(
                        text = currentQ.category,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        item {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = colorScheme.primary,
                trackColor = colorScheme.surfaceContainerHighest
            )
        }

        // Question Statement
        item {
            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = colorScheme.surfaceContainerLow
                ),
                border = BorderStroke(1.dp, colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = currentQ.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )

                    if (currentQ.codeSnippet != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = colorScheme.surfaceContainer
                            ),
                            border = BorderStroke(1.dp, colorScheme.outlineVariant)
                        ) {
                            val highlightedSnippet = remember(currentQ.codeSnippet, colorScheme.onSurface, colorScheme.primary) {
                                CppSyntaxHighlighter.highlightCppCode(
                                    code = currentQ.codeSnippet,
                                    baseTextColor = colorScheme.onSurface,
                                    keywordColor = colorScheme.primary,
                                    commentColor = colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = highlightedSnippet,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    lineHeight = 18.sp
                                ),
                                modifier = Modifier.padding(12.dp)
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
                        session.isAnswerConfirmed && isCorrect -> colorScheme.primary
                        session.isAnswerConfirmed && isSelected && !isCorrect -> colorScheme.error
                        isSelected -> colorScheme.primary
                        else -> colorScheme.outlineVariant
                    }

                    val bgColor = when {
                        session.isAnswerConfirmed && isCorrect -> colorScheme.primaryContainer.copy(alpha = 0.5f)
                        session.isAnswerConfirmed && isSelected && !isCorrect -> colorScheme.errorContainer.copy(alpha = 0.5f)
                        isSelected -> colorScheme.secondaryContainer
                        else -> colorScheme.surfaceContainerLow
                    }

                    OutlinedCard(
                        onClick = { viewModel.selectQuizOption(index) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.outlinedCardColors(containerColor = bgColor),
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("quiz_option_$index")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            session.isAnswerConfirmed && isCorrect -> colorScheme.primary
                                            session.isAnswerConfirmed && isSelected && !isCorrect -> colorScheme.error
                                            isSelected -> colorScheme.primary
                                            else -> colorScheme.surfaceContainerHigh
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${('A'.code + index).toChar()}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = when {
                                        session.isAnswerConfirmed && isCorrect -> colorScheme.onPrimary
                                        session.isAnswerConfirmed && isSelected && !isCorrect -> colorScheme.onError
                                        isSelected -> colorScheme.onPrimary
                                        else -> colorScheme.onSurfaceVariant
                                    }
                                )
                            }

                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )

                            if (session.isAnswerConfirmed && isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Benar",
                                    tint = colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else if (session.isAnswerConfirmed && isSelected && !isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Salah",
                                    tint = colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (session.isAnswerConfirmed) {
            item {
                val isCorrect = session.selectedOptionIndex == currentQ.correctIndex
                OutlinedCard(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = if (isCorrect) colorScheme.primaryContainer.copy(alpha = 0.4f)
                        else colorScheme.errorContainer.copy(alpha = 0.4f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isCorrect) colorScheme.primary else colorScheme.error
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
                                tint = if (isCorrect) colorScheme.primary else colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (isCorrect) "Jawaban Benar" else "Jawaban Belum Tepat",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = currentQ.explanation,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            if (!session.isAnswerConfirmed) {
                Button(
                    onClick = { viewModel.confirmQuizAnswer() },
                    enabled = session.selectedOptionIndex != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("quiz_confirm_btn"),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Konfirmasi Jawaban",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            } else {
                Button(
                    onClick = { viewModel.nextQuizQuestion() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("quiz_next_btn"),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = if (session.currentIndex + 1 < session.questions.size) "Soal Selanjutnya" else "Lihat Hasil Kuis",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
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
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = colorScheme.onPrimaryContainer,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (percentage >= 80) "Hasil Sangat Baik" else if (percentage >= 50) "Hasil Cukup Baik" else "Terus Berlatih",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Benar $correct dari $total pertanyaan ($percentage%)",
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = colorScheme.secondaryContainer
        ) {
            Text(
                text = "+$earnedXp XP ditambahkan",
                style = MaterialTheme.typography.labelLarge,
                color = colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.navigateTo(ScreenDestination.QuizHub) },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("quiz_finish_back_btn")
        ) {
            Text("Kembali ke Daftar Kuis", style = MaterialTheme.typography.labelLarge)
        }
    }
}
