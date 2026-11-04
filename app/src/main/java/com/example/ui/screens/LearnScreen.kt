package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CourseLevel
import com.example.ui.components.LessonCard
import com.example.ui.components.LevelProgressCard
import com.example.ui.components.StreakBannerCard
import com.example.ui.theme.CppBluePrimary
import com.example.ui.theme.CppNavyBorder
import com.example.ui.theme.CppNavyCard
import com.example.ui.theme.CppNavyDark
import com.example.ui.theme.CppNavySurface
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.CppViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(
    viewModel: CppViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val lessonProgress by viewModel.lessonProgress.collectAsState()
    val selectedLevel by viewModel.selectedCourseLevel.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val lessonsForLevel = remember(selectedLevel) {
        viewModel.repository.getLessonsByLevel(selectedLevel)
    }
    val filteredLessons = remember(selectedLevel, searchQuery, lessonsForLevel) {
        if (searchQuery.isBlank()) {
            lessonsForLevel
        } else {
            viewModel.repository.allLessons.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.subtitle.contains(searchQuery, ignoreCase = true) ||
                        it.summary.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("learn_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "C++ Master",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = CppBluePrimary,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Text(
                        text = "Kuasai C++ dari dasar hingga mahir",
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
                        imageVector = Icons.Default.School,
                        contentDescription = "Belajar",
                        tint = CppBluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Streak Banner & Progress
        item {
            StreakBannerCard(
                streakDays = userProfile.currentStreak,
                bestStreak = userProfile.bestStreak
            )
        }

        item {
            LevelProgressCard(userProfile = userProfile)
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Cari materi, konsep, atau sintaks C++...", color = TextSecondary) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Cari", tint = TextSecondary)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Hapus", tint = TextSecondary)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CppBluePrimary,
                    unfocusedBorderColor = CppNavyBorder,
                    focusedContainerColor = CppNavySurface,
                    unfocusedContainerColor = CppNavySurface,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_lessons_field")
            )
        }

        // Level Tabs (Only when not searching)
        if (searchQuery.isBlank()) {
            item {
                TabRow(
                    selectedTabIndex = selectedLevel.ordinal,
                    containerColor = CppNavySurface,
                    contentColor = CppBluePrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedLevel.ordinal]),
                            color = CppBluePrimary,
                            height = 3.dp
                        )
                    },
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    CourseLevel.values().forEach { level ->
                        Tab(
                            selected = selectedLevel == level,
                            onClick = { viewModel.setCourseLevel(level) },
                            text = {
                                Text(
                                    text = level.label,
                                    fontWeight = if (selectedLevel == level) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp
                                )
                            },
                            modifier = Modifier.testTag("tab_${level.name}")
                        )
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (searchQuery.isNotBlank()) "Hasil Pencarian (${filteredLessons.size})"
                    else "Daftar Materi: ${selectedLevel.label}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                val completedInLevel = filteredLessons.count { lessonProgress[it.id]?.isCompleted == true }
                Text(
                    text = "$completedInLevel / ${filteredLessons.size} Tuntas",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = CppBluePrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        // Lessons List
        if (filteredLessons.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tidak ada materi yang cocok dengan '$searchQuery'",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                }
            }
        } else {
            items(filteredLessons, key = { it.id }) { lesson ->
                val progress = lessonProgress[lesson.id]
                val isCompleted = progress?.isCompleted == true
                val isBookmarked = progress?.isBookmarked == true

                LessonCard(
                    lesson = lesson,
                    isCompleted = isCompleted,
                    isBookmarked = isBookmarked,
                    onClick = {
                        viewModel.navigateTo(ScreenDestination.LessonDetail(lesson.id))
                    },
                    onToggleBookmark = {
                        viewModel.toggleLessonBookmark(lesson.id, isBookmarked)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
