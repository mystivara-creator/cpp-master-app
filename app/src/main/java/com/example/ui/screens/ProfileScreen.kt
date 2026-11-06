package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.data.auth.AuthManager
import com.example.ui.components.LevelProgressCard
import com.example.ui.components.StatCard
import com.example.ui.viewmodel.CppViewModel
import com.example.ui.viewmodel.ScreenDestination

data class AchievementBadge(
    val id: String,
    val title: String,
    val desc: String,
    val isUnlocked: Boolean
)

@Composable
fun ProfileScreen(
    viewModel: CppViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val lessonProgress by viewModel.lessonProgress.collectAsState()
    val currentUser by AuthManager.currentUser.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        AuthManager.initAuth(context)
    }

    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember {
        runCatching { CredentialManager.create(context) }.getOrNull()
    }

    var isSigningIn by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    val badges = remember(
        userProfile.completedLessons,
        userProfile.solvedChallenges,
        userProfile.currentStreak,
        userProfile.bestStreak,
        userProfile.quizzesCompleted,
        userProfile.totalXp
    ) {
        listOf(
            AchievementBadge(
                "b1", "Langkah Pertama", "Pelajari materi pertama C++",
                userProfile.completedLessons >= 1
            ),
            AchievementBadge(
                "b2", "Ahli Dasar", "Tuntaskan 6 materi Dasar C++",
                userProfile.completedLessons >= 6
            ),
            AchievementBadge(
                "b3", "Penantang Kode", "Selesaikan tantangan harian pertamamu",
                userProfile.solvedChallenges >= 1
            ),
            AchievementBadge(
                "b4", "Streak 3 Hari", "Aktif belajar 3 hari berturut-turut",
                userProfile.currentStreak >= 3 || userProfile.bestStreak >= 3
            ),
            AchievementBadge(
                "b5", "Pemberantas Kuis", "Selesaikan minimal 3 sesi kuis",
                userProfile.quizzesCompleted >= 3
            ),
            AchievementBadge(
                "b6", "Arsitek Sistem", "Capai level Arsitek C++ (>700 XP)",
                userProfile.totalXp >= 700
            )
        )
    }

    val bookmarkedLessons = remember(lessonProgress) {
        viewModel.repository.allLessons.filter {
            lessonProgress[it.id]?.isBookmarked == true
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Minimalist Header
        item {
            Column {
                Text(
                    text = "Profil & Progres",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )
                Text(
                    text = "Kelola akun, pencapaian, dan riwayat belajar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }

        // User Profile Summary Card
        item {
            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = colorScheme.surfaceContainerLow
                ),
                border = BorderStroke(1.dp, colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentUser?.displayName ?: "Pelajar C++",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = colorScheme.onSurface
                        )
                        Text(
                            text = userProfile.levelTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${userProfile.totalXp} XP terkumpul",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                color = colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // Google Account Card
        item {
            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = colorScheme.surfaceContainerLow
                ),
                border = BorderStroke(1.dp, colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (currentUser != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(colorScheme.secondaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentUser?.displayName ?: "Akun Google",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colorScheme.onSurface
                                )
                                Text(
                                    text = currentUser?.email ?: "Tersinkronisasi",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorScheme.onSurfaceVariant
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (credentialManager != null) {
                                        AuthManager.signOut(context, credentialManager, coroutineScope) {
                                            Toast.makeText(context, "Berhasil keluar", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    contentDescription = "Keluar",
                                    tint = colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(colorScheme.secondaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Sinkronisasi Akun Google",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colorScheme.onSurface
                                )
                                Text(
                                    text = "Hubungkan akun untuk mencadangkan progres belajar",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        FilledTonalButton(
                            onClick = {
                                val cm = credentialManager
                                if (cm == null) {
                                    Toast.makeText(context, "Layanan kredensial tidak tersedia", Toast.LENGTH_SHORT).show()
                                    return@FilledTonalButton
                                }
                                isSigningIn = true
                                AuthManager.onGoogleSignInClicked(
                                    context = context,
                                    credentialManager = cm,
                                    scope = coroutineScope,
                                    onSuccess = { user ->
                                        isSigningIn = false
                                        Toast.makeText(context, "Selamat datang, ${user.displayName ?: "Pengguna"}", Toast.LENGTH_SHORT).show()
                                    },
                                    onError = { error ->
                                        isSigningIn = false
                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                    },
                                    onCancelled = {
                                        isSigningIn = false
                                    }
                                )
                            },
                            enabled = !isSigningIn,
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("google_sign_in_btn")
                        ) {
                            if (isSigningIn) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Menghubungkan...", style = MaterialTheme.typography.labelLarge)
                            } else {
                                Text(
                                    text = "Masuk dengan Google",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }

        // Level Progress Card
        item {
            LevelProgressCard(userProfile = userProfile)
        }

        // Stats Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Materi Selesai",
                    value = "${userProfile.completedLessons} / 19",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Tantangan Lolos",
                    value = "${userProfile.solvedChallenges}",
                    icon = Icons.Default.Terminal,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Kuis Selesai",
                    value = "${userProfile.quizzesCompleted}",
                    icon = Icons.Default.Quiz,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Streak Terbaik",
                    value = "${userProfile.bestStreak} hari",
                    icon = Icons.Default.LocalFireDepartment,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Achievements Section
        item {
            Text(
                text = "Pencapaian (${badges.count { it.isUnlocked }} / ${badges.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                badges.forEach { badge ->
                    OutlinedCard(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = colorScheme.surfaceContainerLow
                        ),
                        border = BorderStroke(1.dp, colorScheme.outlineVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (badge.isUnlocked) colorScheme.primaryContainer
                                        else colorScheme.surfaceContainerHighest
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (badge.isUnlocked) Icons.Default.EmojiEvents else Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = if (badge.isUnlocked) colorScheme.onPrimaryContainer else colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = badge.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (badge.isUnlocked) colorScheme.onSurface else colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = badge.desc,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorScheme.onSurfaceVariant
                                )
                            }

                            if (badge.isUnlocked) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = colorScheme.secondaryContainer
                                ) {
                                    Text(
                                        text = "Tercapai",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colorScheme.onSecondaryContainer,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bookmarked Lessons Section
        if (bookmarkedLessons.isNotEmpty()) {
            item {
                Text(
                    text = "Materi Tersimpan (${bookmarkedLessons.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )
            }

            items(bookmarkedLessons, key = { it.id }) { bLesson ->
                OutlinedCard(
                    onClick = { viewModel.navigateTo(ScreenDestination.LessonDetail(bLesson.id)) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = colorScheme.surfaceContainerLow
                    ),
                    border = BorderStroke(1.dp, colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = bLesson.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onSurface
                            )
                            Text(
                                text = bLesson.level.label,
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Minimalist Developer Attribution Card (mystivara / @mystivara-creator)
        item {
            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = colorScheme.surfaceContainerLow
                ),
                border = BorderStroke(1.dp, colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "mystivara",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onSurface
                            )
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Kreator",
                                tint = colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "GitHub @mystivara-creator • com.varalearn.cxx",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Reset Progress Button
        item {
            OutlinedButton(
                onClick = { showResetDialog = true },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colorScheme.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("reset_progress_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Reset Progres Belajar",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Semua Progres?") },
            text = {
                Text("Semua XP, materi selesai, riwayat tantangan harian, dan pencapaian akan dikembalikan ke awal.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetData()
                        showResetDialog = false
                        Toast.makeText(context, "Progres telah direset", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.error)
                ) {
                    Text("Reset", color = colorScheme.onError)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
