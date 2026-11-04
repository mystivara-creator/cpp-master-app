package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import com.example.data.auth.AuthManager
import com.example.ui.components.LevelProgressCard
import com.example.ui.components.StatCard
import com.example.ui.theme.CppAmberWarning
import com.example.ui.theme.CppBluePrimary
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

data class AchievementBadge(
    val id: String,
    val title: String,
    val desc: String,
    val iconEmoji: String,
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

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        AuthManager.initAuth(context)
    }

    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    var isSigningIn by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    // Compute Badges
    val badges = remember(userProfile.completedLessons, userProfile.solvedChallenges, userProfile.currentStreak, userProfile.bestStreak, userProfile.quizzesCompleted, userProfile.totalXp) {
        listOf(
            AchievementBadge(
                "b1", "Langkah Pertama", "Pelajari materi pertama C++", "🚀",
                userProfile.completedLessons >= 1
            ),
            AchievementBadge(
                "b2", "Ahli Dasar", "Tuntaskan 6 materi Dasar C++", "📜",
                userProfile.completedLessons >= 6
            ),
            AchievementBadge(
                "b3", "Penantang Kode", "Selesaikan tantangan harian pertamamu", "⚡",
                userProfile.solvedChallenges >= 1
            ),
            AchievementBadge(
                "b4", "Streak 3 Hari", "Aktif belajar 3 hari berturut-turut", "🔥",
                userProfile.currentStreak >= 3 || userProfile.bestStreak >= 3
            ),
            AchievementBadge(
                "b5", "Pemberantas Kuis", "Selesaikan minimal 3 sesi kuis", "🎯",
                userProfile.quizzesCompleted >= 3
            ),
            AchievementBadge(
                "b6", "Arsitek Sistem", "Capai level Arsitek C++ (>700 XP)", "🏆",
                userProfile.totalXp >= 700
            )
        )
    }

    // Bookmarked lessons
    val bookmarkedLessons = remember(lessonProgress) {
        viewModel.repository.allLessons.filter {
            lessonProgress[it.id]?.isBookmarked == true
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Creator Spotlight Card (mystivara / mystivara-creator)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D3B)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(CppBluePrimary, Color(0xFF818CF8), CppAmberWarning)
                    )
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CppBluePrimary.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "KREATOR RESMI",
                                color = CppBluePrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CppEmeraldTertiary.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Android 15+ Native",
                                color = CppEmeraldTertiary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(CppBluePrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
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
                                    text = "mystivara",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextPrimary
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified Creator",
                                    tint = CppBluePrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = "GitHub: @mystivara-creator",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF93C5FD),
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text(
                                text = "Pencipta & Pengembang Aplikasi C++ Master",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                            )
                        }
                    }
                }
            }
        }

        // Google Sign-In Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CppNavySurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (currentUser != null) CppEmeraldTertiary.copy(alpha = 0.4f) else CppNavyBorder
                    )
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (currentUser != null) {
                        // User is signed in with Google
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(CppEmeraldTertiary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = CppEmeraldTertiary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentUser?.displayName ?: "Pengguna Google",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = currentUser?.email ?: "",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                                Text(
                                    text = "Terhubung dengan Akun Google ✓",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CppEmeraldTertiary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            IconButton(
                                onClick = {
                                    AuthManager.signOut(context, credentialManager, coroutineScope) {
                                        Toast.makeText(context, "Berhasil keluar", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    contentDescription = "Keluar",
                                    tint = CppRoseError
                                )
                            }
                        }
                    } else {
                        // User is NOT signed in
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF29355E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = CppBluePrimary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Akun Google Cloud Sync",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Simpan progres belajar & streak ke cloud",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                isSigningIn = true
                                AuthManager.onGoogleSignInClicked(
                                    context = context,
                                    credentialManager = credentialManager,
                                    scope = coroutineScope,
                                    onSuccess = { user ->
                                        isSigningIn = false
                                        Toast.makeText(context, "Selamat datang, ${user.displayName}!", Toast.LENGTH_SHORT).show()
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
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("google_sign_in_btn")
                        ) {
                            if (isSigningIn) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color(0xFF0F172A),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Menghubungkan Google...", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                            } else {
                                Text(
                                    text = "Masuk dengan Google (Sign in with Google)",
                                    color = Color(0xFF1E293B),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // User Profile Header Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CppNavySurface)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(CppBluePrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = CppBluePrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentUser?.displayName ?: "Pelajar C++",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = userProfile.levelTitle,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = CppBluePrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = CppAmberWarning,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${userProfile.totalXp} XP Terkumpul",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CppAmberWarning
                                )
                            )
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
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Pelajaran Tuntas",
                    value = "${userProfile.completedLessons} / 19",
                    icon = Icons.Default.MenuBook,
                    color = CppBluePrimary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Tantangan Lolos",
                    value = "${userProfile.solvedChallenges}",
                    icon = Icons.Default.Terminal,
                    color = CppEmeraldTertiary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Kuis Selesai",
                    value = "${userProfile.quizzesCompleted}",
                    icon = Icons.Default.Quiz,
                    color = Color(0xFF818CF8),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Streak Tertinggi",
                    value = "${userProfile.bestStreak} Hari",
                    icon = Icons.Default.LocalFireDepartment,
                    color = CppAmberWarning,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Badges Section
        item {
            Text(
                text = "Lencana Prestasi (${badges.count { it.isUnlocked }} / ${badges.size})",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                badges.forEach { badge ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (badge.isUnlocked) CppNavySurface else CppNavySurface.copy(alpha = 0.5f)
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(
                                if (badge.isUnlocked) CppAmberWarning.copy(alpha = 0.4f) else CppNavyBorder
                            )
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (badge.isUnlocked) CppAmberWarning.copy(alpha = 0.15f)
                                        else CppNavyBorder.copy(alpha = 0.3f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (badge.isUnlocked) {
                                    Text(badge.iconEmoji, fontSize = 20.sp)
                                } else {
                                    Icon(Icons.Default.Lock, contentDescription = "Terkunci", tint = TextSecondary, modifier = Modifier.size(18.dp))
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = badge.title,
                                    fontWeight = FontWeight.Bold,
                                    color = if (badge.isUnlocked) TextPrimary else TextSecondary,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = badge.desc,
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }

                            if (badge.isUnlocked) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = CppEmeraldTertiary.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "Terbuka ✓",
                                        color = CppEmeraldTertiary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bookmarks Section
        if (bookmarkedLessons.isNotEmpty()) {
            item {
                Text(
                    text = "Materi Tersimpan (${bookmarkedLessons.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
            }

            items(bookmarkedLessons, key = { it.id }) { bLesson ->
                Surface(
                    onClick = { viewModel.navigateTo(ScreenDestination.LessonDetail(bLesson.id)) },
                    shape = RoundedCornerShape(12.dp),
                    color = CppNavySurface,
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(CppNavyBorder)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.Bookmark, contentDescription = null, tint = CppAmberWarning)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(bLesson.title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                            Text(bLesson.level.label, color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Reset Data Button
        item {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { showResetDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CppRoseError),
                modifier = Modifier.fillMaxWidth().testTag("reset_progress_btn")
            ) {
                Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Reset Seluruh Progres Belajar")
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Semua Progres?") },
            text = { Text("Semua XP, materi yang telah tuntas, rekor tantangan harian, dan lencana akan direset kembali ke awal.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetData()
                        showResetDialog = false
                        Toast.makeText(context, "Progres telah direset.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CppRoseError)
                ) {
                    Text("Ya, Reset", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Batal")
                }
            },
            containerColor = CppNavySurface,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary
        )
    }
}
