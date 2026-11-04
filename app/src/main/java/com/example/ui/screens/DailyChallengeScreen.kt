package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.data.model.ChallengeDifficulty
import com.example.ui.components.CppCodeEditor
import com.example.ui.components.TerminalOutputView
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

@Composable
fun DailyChallengeScreen(
    viewModel: CppViewModel,
    modifier: Modifier = Modifier
) {
    val currentDay by viewModel.currentDay.collectAsState()
    val challengeCode by viewModel.challengeCode.collectAsState()
    val challengeResult by viewModel.challengeResult.collectAsState()
    val isRunning by viewModel.isChallengeRunning.collectAsState()
    val challengeRecords by viewModel.challengeRecords.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val challenge = viewModel.repository.getChallengeForDay(currentDay)
    val isSolved = challengeRecords[challenge.id]?.isPassed == true

    var showHints by remember { mutableStateOf(false) }
    var showSolution by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CppNavyDark)
            .testTag("daily_challenge_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Tantangan Harian C++",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = CppBluePrimary
                        )
                    )
                    Text(
                        text = "Latih logika & algoritma koding setiap hari",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                }

                // Streak Chip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1E1735),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(CppAmberWarning.copy(alpha = 0.5f))
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = CppAmberWarning,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "${userProfile.currentStreak} Hari",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFEF08A)
                            )
                        )
                    }
                }
            }
        }

        // Days Selector Carousel
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Pilih Hari Tantangan:",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    (1..8).forEach { dayNum ->
                        val dayChallenge = viewModel.repository.getChallengeForDay(dayNum)
                        val daySolved = challengeRecords[dayChallenge.id]?.isPassed == true
                        val isSelected = (dayNum == currentDay)

                        Surface(
                            onClick = {
                                viewModel.selectDay(dayNum)
                                showHints = false
                                showSolution = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = when {
                                isSelected -> CppBluePrimary
                                daySolved -> CppEmeraldTertiary.copy(alpha = 0.2f)
                                else -> CppNavySurface
                            },
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = androidx.compose.ui.graphics.SolidColor(
                                    if (isSelected) CppBluePrimary
                                    else if (daySolved) CppEmeraldTertiary.copy(alpha = 0.4f)
                                    else CppNavyBorder
                                )
                            ),
                            modifier = Modifier.testTag("day_selector_$dayNum")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Hari $dayNum",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color(0xFF0F172A) else TextPrimary,
                                    fontSize = 13.sp
                                )
                                if (daySolved) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selesai",
                                        tint = if (isSelected) Color(0xFF0F172A) else CppEmeraldTertiary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Challenge Info Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CppNavySurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (isSolved) CppEmeraldTertiary.copy(alpha = 0.5f) else CppNavyBorder
                    )
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Badges row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Difficulty Badge
                            val diffColor = when (challenge.difficulty) {
                                ChallengeDifficulty.MUDAH -> CppEmeraldTertiary
                                ChallengeDifficulty.SEDANG -> CppAmberWarning
                                ChallengeDifficulty.SULIT -> CppRoseError
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = diffColor.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = challenge.difficulty.label,
                                    color = diffColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            // Tags
                            challenge.tags.forEach { tag ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = CppNavyCard
                                ) {
                                    Text(
                                        text = tag,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        // XP Reward
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CppBluePrimary.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = CppBluePrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "+${challenge.difficulty.xp} XP",
                                    color = CppBluePrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Hari #${challenge.dayNumber}: ${challenge.title}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = challenge.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextPrimary,
                            lineHeight = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Input & Output Specs
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CppNavyCard)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Format Masukan: ${challenge.inputSpecification}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                        Text(
                            text = "Format Keluaran: ${challenge.outputSpecification}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Example box
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CppCodeBackground)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Contoh Input:",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = challenge.exampleInput,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CppCodeBackground)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Contoh Output:",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = challenge.exampleOutput,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = Color(0xFF6EE7B7)
                            )
                        }
                    }
                }
            }
        }

        // Code Editor
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Solusi C++ Anda:",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        OutlinedButton(
                            onClick = { showHints = !showHints },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, modifier = Modifier.size(14.dp), tint = CppAmberWarning)
                            Spacer(Modifier.width(4.dp))
                            Text("Petunjuk", fontSize = 11.sp, color = CppAmberWarning)
                        }

                        OutlinedButton(
                            onClick = { showSolution = !showSolution },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp), tint = TextSecondary)
                            Spacer(Modifier.width(4.dp))
                            Text("Solusi", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }

                // Hints Expandable
                AnimatedVisibility(visible = showHints) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E2405)),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(CppAmberWarning.copy(alpha = 0.5f))
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "💡 Petunjuk Pengerjaan:",
                                fontWeight = FontWeight.Bold,
                                color = CppAmberWarning,
                                fontSize = 13.sp
                            )
                            challenge.hints.forEach { hint ->
                                Text("• $hint", color = Color(0xFFFEF08A), fontSize = 12.sp, lineHeight = 18.sp)
                            }
                        }
                    }
                }

                // Solution Expandable
                AnimatedVisibility(visible = showSolution) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CppNavySurface),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "📖 Solusi Model Referensi:",
                                fontWeight = FontWeight.Bold,
                                color = CppBluePrimary,
                                fontSize = 13.sp
                            )
                            CppCodeEditor(
                                code = challenge.solutionCode,
                                onCodeChange = {},
                                readOnly = true,
                                title = "solution.cpp",
                                minHeight = 120,
                                maxHeight = 250
                            )
                            Text(
                                text = challenge.solutionExplanation,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // The Interactive Editor
                CppCodeEditor(
                    code = challengeCode,
                    onCodeChange = { viewModel.updateChallengeCode(it) },
                    title = "challenge_${challenge.id}.cpp",
                    onReset = { viewModel.resetChallengeCode() }
                )
            }
        }

        // Action Buttons: Run & Test
        item {
            Button(
                onClick = { viewModel.runChallengeTests() },
                enabled = !isRunning,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("run_challenge_tests_btn"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CppBluePrimary
                )
            ) {
                if (isRunning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color(0xFF0F172A),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mengkompilasi & Menjalankan Tes...", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                } else {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF0F172A))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Uji Solusi pada Test Cases", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                }
            }
        }

        // Test Cases Results
        val result = challengeResult
        if (result != null) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Hasil Evaluasi Test Cases:",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    // Summary Banner
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (result.isSuccess) Color(0xFF052E16) else Color(0xFF3B0F15)
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(
                                if (result.isSuccess) CppEmeraldTertiary else CppRoseError
                            )
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = if (result.isSuccess) Icons.Default.CheckCircle else Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = if (result.isSuccess) CppEmeraldTertiary else CppRoseError,
                                modifier = Modifier.size(24.dp)
                            )
                            Column {
                                Text(
                                    text = if (result.isSuccess) "Tantangan Berhasil Dipecahkan! 🎉" else "Ada Test Case yang Gagal",
                                    fontWeight = FontWeight.Bold,
                                    color = if (result.isSuccess) CppEmeraldTertiary else CppRoseError,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = result.output,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    // Individual Test Cases breakdown
                    result.testCaseResults.forEach { tcResult ->
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = CppNavySurface),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = androidx.compose.ui.graphics.SolidColor(
                                    if (tcResult.isPassed) CppEmeraldTertiary.copy(alpha = 0.5f)
                                    else CppRoseError.copy(alpha = 0.5f)
                                )
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Test Case #${tcResult.index}: ${tcResult.testCase.inputDescription}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = TextPrimary
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (tcResult.isPassed) CppEmeraldTertiary.copy(alpha = 0.2f) else CppRoseError.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = if (tcResult.isPassed) "Lolos ✓" else "Gagal ✗",
                                            color = if (tcResult.isPassed) CppEmeraldTertiary else CppRoseError,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Input: '${tcResult.testCase.simulatedInput}'",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                                Text(
                                    text = "Diharapkan: '${tcResult.testCase.expectedOutput}'",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = Color(0xFF6EE7B7)
                                )
                                Text(
                                    text = "Output Anda: '${tcResult.actualOutput}'",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = if (tcResult.isPassed) Color(0xFF6EE7B7) else CppRoseError
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
