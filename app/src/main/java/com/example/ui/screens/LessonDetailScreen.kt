package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LessonSection
import com.example.ui.components.CppCodeEditor
import com.example.ui.viewmodel.CppViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
    lessonId: String,
    viewModel: CppViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateTo(ScreenDestination.Learn)
    }

    val lesson = viewModel.repository.getLessonById(lessonId)
    val lessonProgress by viewModel.lessonProgress.collectAsState()
    val isCompleted = lessonProgress[lessonId]?.isCompleted == true
    val isBookmarked = lessonProgress[lessonId]?.isBookmarked == true
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current

    if (lesson == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text("Materi tidak ditemukan.", color = colorScheme.onSurface)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Materi ${lesson.order}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Learn) },
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleLessonBookmark(lesson.id, isBookmarked) },
                        modifier = Modifier.testTag("lesson_detail_bookmark_btn")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Simpan Materi",
                            tint = if (isBookmarked) colorScheme.primary else colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.onSurface,
                    navigationIconContentColor = colorScheme.onSurface
                )
            )
        },
        containerColor = colorScheme.background,
        modifier = modifier.testTag("lesson_detail_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colorScheme.secondaryContainer
                ) {
                    Text(
                        text = lesson.level.label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = "${lesson.estimatedMinutes} menit baca",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = lesson.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface
            )

            Text(
                text = lesson.subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.onSurfaceVariant
            )

            // Summary Card
            OutlinedCard(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = colorScheme.surfaceContainerLow
                ),
                border = BorderStroke(1.dp, colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = lesson.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurface
                    )
                }
            }

            // Render all lesson sections
            lesson.sections.forEach { section ->
                when (section) {
                    is LessonSection.Heading -> {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = section.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = colorScheme.onSurface
                        )
                    }

                    is LessonSection.Paragraph -> {
                        Text(
                            text = section.text,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                            color = colorScheme.onSurface
                        )
                    }

                    is LessonSection.CodeBlock -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            CppCodeEditor(
                                code = section.code,
                                onCodeChange = {},
                                readOnly = true,
                                title = "contoh.cpp"
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (section.output != null) {
                                    Text(
                                        text = "Keluaran:",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = colorScheme.onSurfaceVariant
                                    )
                                } else {
                                    Spacer(modifier = Modifier.width(1.dp))
                                }

                                FilledTonalButton(
                                    onClick = {
                                        viewModel.loadPlaygroundTemplate(lesson.title, section.code)
                                        viewModel.navigateTo(ScreenDestination.Playground)
                                    },
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Buka di Playground",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }

                            if (section.output != null) {
                                OutlinedCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.outlinedCardColors(
                                        containerColor = colorScheme.surfaceContainer
                                    ),
                                    border = BorderStroke(1.dp, colorScheme.outlineVariant)
                                ) {
                                    Text(
                                        text = section.output,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = FontFamily.Monospace,
                                            lineHeight = 18.sp
                                        ),
                                        color = colorScheme.onSurface,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }

                            if (section.explanation != null) {
                                Text(
                                    text = section.explanation,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    is LessonSection.TipBox -> {
                        OutlinedCard(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = colorScheme.secondaryContainer.copy(alpha = 0.4f)
                            ),
                            border = BorderStroke(1.dp, colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = section.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = colorScheme.onSurface
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = section.message,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    is LessonSection.WarningBox -> {
                        OutlinedCard(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = colorScheme.errorContainer.copy(alpha = 0.35f)
                            ),
                            border = BorderStroke(1.dp, colorScheme.error.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = colorScheme.error,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = section.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = colorScheme.onErrorContainer
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = section.message,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorScheme.onSurface
                                )
                            }
                        }
                    }

                    is LessonSection.KeyPoints -> {
                        OutlinedCard(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = colorScheme.surfaceContainerLow
                            ),
                            border = BorderStroke(1.dp, colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Ringkasan Materi",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                section.points.forEach { pt ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "•",
                                            color = colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = pt,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (lesson.quickCheck != null) {
                Spacer(modifier = Modifier.height(4.dp))
                QuickCheckSection(
                    question = lesson.quickCheck,
                    onCorrectAnswer = {
                        viewModel.markLessonCompleted(lesson.id)
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.markLessonCompleted(lesson.id)
                    Toast.makeText(context, "Materi ditandai selesai (+25 XP)", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("mark_lesson_completed_btn"),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(
                    imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isCompleted) "Materi Selesai" else "Tandai Selesai (+25 XP)",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            OutlinedButton(
                onClick = { viewModel.startTopicQuiz(lesson) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("start_topic_quiz_btn"),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Quiz,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Mulai Kuis Topik Ini",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun QuickCheckSection(
    question: com.example.data.model.QuizQuestion,
    onCorrectAnswer: () -> Unit
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var hasAnswered by remember { mutableStateOf(false) }
    val colorScheme = MaterialTheme.colorScheme

    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = colorScheme.surfaceContainerLow
        ),
        border = BorderStroke(1.dp, colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Evaluasi Singkat",
                style = MaterialTheme.typography.labelLarge,
                color = colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = question.question,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            question.options.forEachIndexed { index, option ->
                val isSelected = selectedIndex == index
                val isCorrect = index == question.correctIndex

                val borderColor = when {
                    hasAnswered && isCorrect -> colorScheme.primary
                    hasAnswered && isSelected && !isCorrect -> colorScheme.error
                    isSelected -> colorScheme.primary
                    else -> colorScheme.outlineVariant
                }

                val containerColor = when {
                    hasAnswered && isCorrect -> colorScheme.primaryContainer.copy(alpha = 0.5f)
                    hasAnswered && isSelected && !isCorrect -> colorScheme.errorContainer.copy(alpha = 0.5f)
                    isSelected -> colorScheme.secondaryContainer
                    else -> colorScheme.surface
                }

                OutlinedCard(
                    onClick = {
                        if (!hasAnswered) {
                            selectedIndex = index
                            hasAnswered = true
                            if (index == question.correctIndex) {
                                onCorrectAnswer()
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = containerColor),
                    border = BorderStroke(1.dp, borderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(colorScheme.surfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${('A'.code + index).toChar()}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.onSurface,
                            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                        )
                    }
                }
            }

            if (hasAnswered) {
                Spacer(modifier = Modifier.height(10.dp))
                val isAnswerRight = selectedIndex == question.correctIndex
                Text(
                    text = if (isAnswerRight) "Benar. ${question.explanation}"
                    else "Belum tepat. ${question.explanation}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isAnswerRight) colorScheme.primary else colorScheme.error
                )
            }
        }
    }
}
