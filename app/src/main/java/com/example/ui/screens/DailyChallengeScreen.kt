package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CppCodeEditor
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
    val colorScheme = MaterialTheme.colorScheme

    val challenge = viewModel.repository.getChallengeForDay(currentDay)
    val isSolved = challengeRecords[challenge.id]?.isPassed == true

    var showHints by remember { mutableStateOf(false) }
    var showSolution by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .testTag("daily_challenge_screen"),
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
                        text = "Tantangan Harian",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )
                    Text(
                        text = "Latihan algoritma & pemecahan masalah C++",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = colorScheme.secondaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${userProfile.currentStreak} hari",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium,
                            color = colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        // Day Selector with Material 3 FilterChip
        item {
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

                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            viewModel.selectDay(dayNum)
                            showHints = false
                            showSolution = false
                        },
                        label = {
                            Text(
                                text = "Hari $dayNum",
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        leadingIcon = if (daySolved) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selesai",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else null,
                        modifier = Modifier.testTag("day_selector_$dayNum")
                    )
                }
            }
        }

        // Challenge Info Card
        item {
            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = colorScheme.surfaceContainerLow
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSolved) colorScheme.primary.copy(alpha = 0.5f) else colorScheme.outlineVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = challenge.difficulty.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Medium,
                                    color = colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            challenge.tags.forEach { tag ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = colorScheme.surfaceContainerHigh
                                ) {
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = colorScheme.primaryContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "+${challenge.difficulty.xp} XP",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Hari ${challenge.dayNumber}: ${challenge.title}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = challenge.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = colorScheme.surfaceContainer
                        ),
                        border = BorderStroke(1.dp, colorScheme.outlineVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Masukan: ${challenge.inputSpecification}",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Keluaran: ${challenge.outputSpecification}",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedCard(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = colorScheme.surfaceContainer
                            ),
                            border = BorderStroke(1.dp, colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Contoh Input",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = challenge.exampleInput,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = colorScheme.onSurface
                                )
                            }
                        }

                        OutlinedCard(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = colorScheme.surfaceContainer
                            ),
                            border = BorderStroke(1.dp, colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Contoh Output",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = challenge.exampleOutput,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Code Editor Section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Kode Solusi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { showHints = !showHints },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Petunjuk", style = MaterialTheme.typography.labelMedium)
                        }

                        OutlinedButton(
                            onClick = { showSolution = !showSolution },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Solusi", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                AnimatedVisibility(visible = showHints) {
                    OutlinedCard(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = colorScheme.secondaryContainer.copy(alpha = 0.4f)
                        ),
                        border = BorderStroke(1.dp, colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Petunjuk Pengerjaan",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onSurface
                            )
                            challenge.hints.forEach { hint ->
                                Text(
                                    text = "• $hint",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                AnimatedVisibility(visible = showSolution) {
                    OutlinedCard(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = colorScheme.surfaceContainerLow
                        ),
                        border = BorderStroke(1.dp, colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Referensi Solusi",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onSurface
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
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                CppCodeEditor(
                    code = challengeCode,
                    onCodeChange = { viewModel.updateChallengeCode(it) },
                    title = "challenge_${challenge.id}.cpp",
                    onReset = { viewModel.resetChallengeCode() }
                )
            }
        }

        // Run & Test Action Button
        item {
            Button(
                onClick = { viewModel.runChallengeTests() },
                enabled = !isRunning,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("run_challenge_tests_btn"),
                shape = RoundedCornerShape(24.dp)
            ) {
                if (isRunning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Menjalankan Test Cases...", style = MaterialTheme.typography.labelLarge)
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Uji Solusi", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        // Evaluation Results
        val result = challengeResult
        if (result != null) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Hasil Evaluasi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )

                    OutlinedCard(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = if (result.isSuccess) {
                                colorScheme.primaryContainer.copy(alpha = 0.45f)
                            } else {
                                colorScheme.errorContainer.copy(alpha = 0.45f)
                            }
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (result.isSuccess) colorScheme.primary else colorScheme.error
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = if (result.isSuccess) Icons.Default.CheckCircle else Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = if (result.isSuccess) colorScheme.primary else colorScheme.error,
                                modifier = Modifier.size(22.dp)
                            )
                            Column {
                                Text(
                                    text = if (result.isSuccess) "Semua Test Case Lolos" else "Beberapa Test Case Belum Lolos",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colorScheme.onSurface
                                )
                                Text(
                                    text = result.output,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    result.testCaseResults.forEach { tcResult ->
                        OutlinedCard(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = colorScheme.surfaceContainerLow
                            ),
                            border = BorderStroke(1.dp, colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Test Case #${tcResult.index}: ${tcResult.testCase.inputDescription}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = colorScheme.onSurface
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (tcResult.isPassed) colorScheme.primaryContainer else colorScheme.errorContainer
                                    ) {
                                        Text(
                                            text = if (tcResult.isPassed) "Lolos" else "Gagal",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Medium,
                                            color = if (tcResult.isPassed) colorScheme.onPrimaryContainer else colorScheme.onErrorContainer,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Input: '${tcResult.testCase.simulatedInput}'",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Diharapkan: '${tcResult.testCase.expectedOutput}'",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = colorScheme.onSurface
                                )
                                Text(
                                    text = "Output: '${tcResult.actualOutput}'",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = if (tcResult.isPassed) colorScheme.primary else colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
