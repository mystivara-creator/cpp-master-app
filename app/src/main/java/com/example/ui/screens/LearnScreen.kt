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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
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
import com.example.data.model.CourseLevel
import com.example.ui.components.LessonCard
import com.example.ui.components.LevelProgressCard
import com.example.ui.components.StreakBannerCard
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
    val colorScheme = MaterialTheme.colorScheme

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
            .background(colorScheme.background)
            .testTag("learn_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Minimalist Google-style Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "C++ Master",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )
                    Text(
                        text = "Kurikulum pemrograman C++ modern",
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
                        imageVector = Icons.Default.Code,
                        contentDescription = "C++ Master",
                        tint = colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Google-style pill Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = {
                    Text(
                        text = "Cari materi atau konsep C++",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cari",
                        tint = colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Hapus",
                                tint = colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surfaceContainerHigh,
                    unfocusedContainerColor = colorScheme.surfaceContainerLow,
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = colorScheme.onSurface,
                    unfocusedTextColor = colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_lessons_field")
            )
        }

        // Streak & Level Progress
        item {
            StreakBannerCard(
                streakDays = userProfile.currentStreak,
                bestStreak = userProfile.bestStreak
            )
        }

        item {
            LevelProgressCard(userProfile = userProfile)
        }

        // Material 3 PrimaryTabRow
        if (searchQuery.isBlank()) {
            item {
                PrimaryTabRow(
                    selectedTabIndex = selectedLevel.ordinal,
                    containerColor = colorScheme.surfaceContainerLow,
                    contentColor = colorScheme.primary,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    CourseLevel.entries.forEach { level ->
                        val isSelected = selectedLevel == level
                        Tab(
                            selected = isSelected,
                            onClick = { viewModel.setCourseLevel(level) },
                            text = {
                                Text(
                                    text = level.label,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isSelected) colorScheme.primary else colorScheme.onSurfaceVariant
                                )
                            },
                            modifier = Modifier.testTag("tab_${level.name}")
                        )
                    }
                }
            }
        }

        // Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (searchQuery.isNotBlank()) "Hasil Pencarian (${filteredLessons.size})"
                    else "Materi ${selectedLevel.label}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )

                val completedInLevel = filteredLessons.count { lessonProgress[it.id]?.isCompleted == true }
                Text(
                    text = "$completedInLevel / ${filteredLessons.size} selesai",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.onSurfaceVariant
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
                        text = "Tidak ada materi untuk '$searchQuery'",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
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
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
