package com.example.ui.screens

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.util.SystemIntegrationHelper
import kotlinx.coroutines.launch

@Composable
fun AppStartupLoadingScreen(
    progress: Float,
    statusMessage: String,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 250),
        label = "startup_loading_progress"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(32.dp)
            .testTag("startup_loading_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Minimalist GitHub-inspired C++ emblem
        Surface(
            modifier = Modifier.size(84.dp),
            shape = RoundedCornerShape(22.dp),
            color = colorScheme.onSurface
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = "C++ Master",
                    tint = colorScheme.surface,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "C++ Master",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Platform Belajar & Playground C++20 Modern",
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(36.dp))

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .width(220.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = colorScheme.primary,
            trackColor = colorScheme.surfaceContainerHighest
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = statusMessage,
            style = MaterialTheme.typography.labelMedium,
            color = colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "com.varalearn.cxx • oleh mystivara",
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = FontFamily.Monospace
            ),
            color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

private data class IntroPageData(
    val badge: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val highlights: List<String>
)

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    val coroutineScope = rememberCoroutineScope()

    var notificationGranted by remember {
        mutableStateOf(SystemIntegrationHelper.isNotificationPermissionGranted(context))
    }
    var storageGranted by remember {
        mutableStateOf(SystemIntegrationHelper.isStoragePermissionGranted(context))
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        notificationGranted = isGranted
        if (isGranted) {
            SystemIntegrationHelper.sendStudyNotification(
                context = context,
                title = "Notifikasi Belajar Aktif",
                message = "Pengingat latihan harian C++ Master telah berhasil diaktifkan."
            )
            Toast.makeText(context, "Izin notifikasi diaktifkan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Notifikasi dapat diaktifkan kapan saja di Profil", Toast.LENGTH_SHORT).show()
        }
    }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        storageGranted = isGranted || Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        SystemIntegrationHelper.getWorkspaceDirectory(context)
        Toast.makeText(
            context,
            if (storageGranted) "Akses penyimpanan file .cpp siap digunakan" else "Menggunakan ruang kerja internal aplikasi",
            Toast.LENGTH_SHORT
        ).show()
    }

    val pages = remember {
        listOf(
            IntroPageData(
                badge = "KURIKULUM TERSTRUKTUR",
                title = "Kuasai C++ dari Dasar hingga Mahir",
                description = "Pelajari konsep pemrograman sistem secara bertahap dengan penjelasan ringkas, contoh kode interaktif, dan kuis evaluasi.",
                icon = Icons.AutoMirrored.Filled.MenuBook,
                highlights = listOf(
                    "19 modul lengkap: Dasar, Menengah (OOP & Pointer), dan Mahir (STL & C++20)",
                    "Evaluasi pemahaman instan di setiap akhir materi",
                    "Desain Material 3 yang bersih, fokus, dan nyaman dibaca"
                )
            ),
            IntroPageData(
                badge = "PLAYGROUND & TANTANGAN",
                title = "Tulis, Jalankan, & Simpan Kode .cpp",
                description = "Praktikkan algoritma langsung dari perangkat Android Anda tanpa konfigurasi rumit, lengkap dengan dukungan ekspor dan impor file .cpp.",
                icon = Icons.Default.Terminal,
                highlights = listOf(
                    "Editor C++ dengan penomoran baris dan bilah simbol cepat",
                    "8 Tantangan Harian dengan pengujian test cases otomatis",
                    "Simpan dan buka file sumber .cpp langsung di penyimpanan perangkat"
                )
            ),
            IntroPageData(
                badge = "IZIN AKSES & PERSIAPAN",
                title = "Aktifkan Notifikasi & Penyimpanan",
                description = "Izinkan akses notifikasi untuk pengingat latihan harian serta akses penyimpanan untuk mengelola file kode C++ (.cpp) Anda.",
                icon = Icons.Default.NotificationsActive,
                highlights = emptyList()
            )
        )
    }

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(24.dp)
            .testTag("onboarding_screen")
    ) {
        // Top Bar with Skip Action
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = colorScheme.secondaryContainer
            ) {
                Text(
                    text = "Langkah ${pagerState.currentPage + 1} / ${pages.size}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            TextButton(
                onClick = {
                    SystemIntegrationHelper.getWorkspaceDirectory(context)
                    onFinishOnboarding()
                },
                modifier = Modifier.testTag("skip_onboarding_btn")
            ) {
                Text("Lewati", style = MaterialTheme.typography.labelLarge)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pager Content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { pageIndex ->
            val page = pages[pageIndex]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = page.icon,
                        contentDescription = null,
                        tint = colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = page.badge,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = page.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = page.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (pageIndex < 2) {
                    OutlinedCard(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = colorScheme.surfaceContainerLow
                        ),
                        border = BorderStroke(1.dp, colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            page.highlights.forEach { item ->
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = item,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Page 3: Interactive Notification & Storage Permission Cards
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedCard(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = colorScheme.surfaceContainerLow
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (notificationGranted) colorScheme.primary else colorScheme.outlineVariant
                            ),
                            modifier = Modifier.fillMaxWidth()
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
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = colorScheme.onSecondaryContainer,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Izin Notifikasi",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (notificationGranted) "Aktif untuk pengingat latihan harian"
                                        else "Pengingat streak & progres belajar C++",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colorScheme.onSurfaceVariant
                                    )
                                }

                                FilledTonalButton(
                                    onClick = {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !notificationGranted) {
                                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                        } else {
                                            notificationGranted = true
                                            SystemIntegrationHelper.sendStudyNotification(context)
                                            Toast.makeText(context, "Notifikasi pengingat aktif", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.testTag("onboarding_notif_permission_btn")
                                ) {
                                    if (notificationGranted) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Text("Diizinkan", style = MaterialTheme.typography.labelMedium)
                                    } else {
                                        Text("Izinkan", style = MaterialTheme.typography.labelMedium)
                                    }
                                }
                            }
                        }

                        OutlinedCard(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = colorScheme.surfaceContainerLow
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (storageGranted) colorScheme.primary else colorScheme.outlineVariant
                            ),
                            modifier = Modifier.fillMaxWidth()
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
                                        imageVector = Icons.Default.FolderOpen,
                                        contentDescription = null,
                                        tint = colorScheme.onSecondaryContainer,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Akses Penyimpanan File .cpp",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Simpan & muat kode sumber C++ di penyimpanan perangkat",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colorScheme.onSurfaceVariant
                                    )
                                }

                                FilledTonalButton(
                                    onClick = {
                                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && !storageGranted) {
                                            storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                        } else {
                                            storageGranted = true
                                            SystemIntegrationHelper.getWorkspaceDirectory(context)
                                            Toast.makeText(
                                                context,
                                                "Ruang penyimpanan file .cpp siap digunakan",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.testTag("onboarding_storage_permission_btn")
                                ) {
                                    if (storageGranted) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Text("Siap", style = MaterialTheme.typography.labelMedium)
                                    } else {
                                        Text("Izinkan", style = MaterialTheme.typography.labelMedium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom Controls: Page Dots & Next / Finish Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(pages.size) { idx ->
                    val isCurrent = pagerState.currentPage == idx
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(if (isCurrent) 24.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCurrent) colorScheme.primary
                                else colorScheme.surfaceContainerHighest
                            )
                    )
                }
            }

            val isLastPage = pagerState.currentPage == pages.size - 1
            Button(
                onClick = {
                    if (!isLastPage) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        SystemIntegrationHelper.getWorkspaceDirectory(context)
                        if (notificationGranted) {
                            SystemIntegrationHelper.sendStudyNotification(
                                context = context,
                                title = "Selamat Datang di C++ Master",
                                message = "Ruang kerja C++20 siap. Mulai pelajari materi pertamamu hari ini!"
                            )
                        }
                        onFinishOnboarding()
                    }
                },
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                modifier = Modifier.testTag(if (isLastPage) "finish_onboarding_btn" else "onboarding_next_btn")
            ) {
                Text(
                    text = if (isLastPage) "Mulai Belajar C++" else "Selanjutnya",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = if (isLastPage) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
