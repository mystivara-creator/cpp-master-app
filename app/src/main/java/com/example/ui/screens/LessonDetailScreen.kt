package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Lesson
import com.example.data.model.LessonSection
import com.example.ui.components.CppCodeEditor
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

    val context = LocalContext.current

    if (lesson == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(CppNavyDark),
            contentAlignment = Alignment.Center
        ) {
            Text("Materi tidak ditemukan.", color = TextPrimary)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Materi #${lesson.order}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.Learn) },
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = TextPrimary
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
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) CppAmberWarning else TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CppNavyDark
                )
            )
        },
        containerColor = CppNavyDark,
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
            // Level badge & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(lesson.level.badgeColor).copy(alpha = 0.2f)
                ) {
                    Text(
                        text = lesson.level.label,
                        color = Color(lesson.level.badgeColor),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = "${lesson.estimatedMinutes} menit baca",
                    style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary)
                )
            }

            Text(
                text = lesson.title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    lineHeight = 32.sp
                )
            )

            Text(
                text = lesson.subtitle,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = CppBluePrimary,
                    fontWeight = FontWeight.Medium
                )
            )

            // Summary Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CppNavySurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CppNavyBorder))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = CppBluePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = lesson.summary,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextPrimary,
                            lineHeight = 22.sp
                        )
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
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }

                    is LessonSection.Paragraph -> {
                        Text(
                            text = section.text,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary,
                                lineHeight = 24.sp
                            )
                        )
                    }

                    is LessonSection.CodeBlock -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            CppCodeEditor(
                                code = section.code,
                                onCodeChange = {},
                                readOnly = true,
                                title = "Contoh C++"
                            )

                            // Button to try in playground
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (section.output != null) {
                                    Text(
                                        text = "Output yang Diharapkan:",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TextSecondary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                } else {
                                    Spacer(modifier = Modifier.width(1.dp))
                                }

                                OutlinedButton(
                                    onClick = {
                                        viewModel.loadPlaygroundTemplate(lesson.title, section.code)
                                        viewModel.navigateTo(ScreenDestination.Playground)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = CppBluePrimary
                                    ),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                        horizontal = 10.dp,
                                        vertical = 4.dp
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Coba di Playground", fontSize = 12.sp)
                                }
                            }

                            if (section.output != null) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(CppCodeBackground)
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = section.output,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = FontFamily.Monospace,
                                            color = Color(0xFF6EE7B7),
                                            lineHeight = 18.sp
                                        )
                                    )
                                }
                            }

                            if (section.explanation != null) {
                                Text(
                                    text = section.explanation,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        lineHeight = 18.sp
                                    )
                                )
                            }
                        }
                    }

                    is LessonSection.TipBox -> {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF063328)),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = androidx.compose.ui.graphics.SolidColor(CppEmeraldTertiary.copy(alpha = 0.5f))
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = CppEmeraldTertiary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = section.title,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = CppEmeraldTertiary
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = section.message,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFFE2E8F0),
                                        lineHeight = 20.sp
                                    )
                                )
                            }
                        }
                    }

                    is LessonSection.WarningBox -> {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF3B1219)),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = androidx.compose.ui.graphics.SolidColor(CppRoseError.copy(alpha = 0.5f))
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = CppRoseError,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = section.title,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = CppRoseError
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = section.message,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFFFEE2E2),
                                        lineHeight = 20.sp
                                    )
                                )
                            }
                        }
                    }

                    is LessonSection.KeyPoints -> {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = CppNavySurface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Poin Kunci & Ringkasan:",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = CppBluePrimary
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                section.points.forEach { pt ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 3.dp),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text("•", color = CppBluePrimary, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = pt,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextPrimary,
                                                lineHeight = 18.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Quick Check Mini Quiz
            if (lesson.quickCheck != null) {
                Spacer(modifier = Modifier.height(8.dp))
                QuickCheckSection(
                    question = lesson.quickCheck,
                    onCorrectAnswer = {
                        viewModel.markLessonCompleted(lesson.id)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Completion Action Button
            Button(
                onClick = {
                    viewModel.markLessonCompleted(lesson.id)
                    Toast.makeText(context, "Materi ditandai tuntas! (+25 XP)", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("mark_lesson_completed_btn"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) CppEmeraldTertiary else CppBluePrimary
                )
            ) {
                Icon(
                    imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isCompleted) "Materi Sudah Tuntas (Selesai)" else "Tandai Selesai & Klaim +25 XP",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }

            // Start Topic Quiz
            OutlinedButton(
                onClick = {
                    viewModel.startTopicQuiz(lesson)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("start_topic_quiz_btn"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CppBluePrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Quiz,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Uji Pemahaman: Ikuti Kuis Topik Ini", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(32.dp))
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

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CppNavyCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(CppBluePrimary.copy(alpha = 0.3f))
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(CppBluePrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("?", color = CppBluePrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Text(
                    text = "Cek Pemahaman Cepat",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = CppBluePrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = question.question,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            question.options.forEachIndexed { index, option ->
                val isSelected = selectedIndex == index
                val isCorrect = index == question.correctIndex

                val borderColor = when {
                    hasAnswered && isCorrect -> CppEmeraldTertiary
                    hasAnswered && isSelected && !isCorrect -> CppRoseError
                    isSelected -> CppBluePrimary
                    else -> CppNavyBorder
                }

                val bgColor = when {
                    hasAnswered && isCorrect -> CppEmeraldTertiary.copy(alpha = 0.15f)
                    hasAnswered && isSelected && !isCorrect -> CppRoseError.copy(alpha = 0.15f)
                    isSelected -> CppBluePrimary.copy(alpha = 0.1f)
                    else -> CppNavySurface
                }

                Surface(
                    onClick = {
                        if (!hasAnswered) {
                            selectedIndex = index
                            hasAnswered = true
                            if (index == question.correctIndex) {
                                onCorrectAnswer()
                            }
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    color = bgColor,
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(borderColor)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${('A'.code + index).toChar()}.",
                            fontWeight = FontWeight.Bold,
                            color = if (hasAnswered && isCorrect) CppEmeraldTertiary else TextSecondary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        )
                    }
                }
            }

            if (hasAnswered) {
                Spacer(modifier = Modifier.height(10.dp))
                val isAnswerRight = selectedIndex == question.correctIndex
                Text(
                    text = if (isAnswerRight) "Tepat Sekali! ${question.explanation}"
                    else "Belum Tepat. ${question.explanation}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isAnswerRight) CppEmeraldTertiary else CppRoseError,
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}
