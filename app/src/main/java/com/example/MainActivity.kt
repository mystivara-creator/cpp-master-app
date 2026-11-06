package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.screens.AppStartupLoadingScreen
import com.example.ui.screens.DailyChallengeScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.LessonDetailScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PlaygroundScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.CppViewModel
import com.example.ui.viewmodel.ScreenDestination

enum class NavigationTab(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val destination: ScreenDestination
) {
    BELAJAR("Belajar", Icons.AutoMirrored.Filled.MenuBook, Icons.AutoMirrored.Outlined.MenuBook, ScreenDestination.Learn),
    TANTANGAN("Tantangan", Icons.Default.Bolt, Icons.Outlined.Bolt, ScreenDestination.DailyChallenge),
    KUIS("Kuis", Icons.Default.Quiz, Icons.Outlined.Quiz, ScreenDestination.QuizHub),
    PLAYGROUND("Playground", Icons.Default.Terminal, Icons.Outlined.Terminal, ScreenDestination.Playground),
    PROFIL("Profil", Icons.Default.Person, Icons.Outlined.Person, ScreenDestination.Profile)
}

class MainActivity : ComponentActivity() {
    private val viewModel: CppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppRoot(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppRoot(viewModel: CppViewModel) {
    val isInitializing by viewModel.isAppInitializing.collectAsState()
    val startupProgress by viewModel.startupProgress.collectAsState()
    val startupStatusText by viewModel.startupStatusText.collectAsState()
    val hasCompletedOnboarding by viewModel.hasCompletedOnboarding.collectAsState()

    when {
        isInitializing -> {
            AppStartupLoadingScreen(
                progress = startupProgress,
                statusMessage = startupStatusText
            )
        }
        !hasCompletedOnboarding -> {
            OnboardingScreen(
                onFinishOnboarding = {
                    viewModel.completeOnboarding()
                }
            )
        }
        else -> {
            MainAppScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun MainAppScreen(viewModel: CppViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    BackHandler(enabled = currentScreen !is ScreenDestination.Learn) {
        viewModel.navigateBack()
    }

    val isTopLevelScreen = currentScreen is ScreenDestination.Learn ||
            currentScreen is ScreenDestination.DailyChallenge ||
            currentScreen is ScreenDestination.QuizHub ||
            currentScreen is ScreenDestination.Playground ||
            currentScreen is ScreenDestination.Profile

    val selectedTab = when (currentScreen) {
        is ScreenDestination.Learn, is ScreenDestination.LessonDetail -> NavigationTab.BELAJAR
        is ScreenDestination.DailyChallenge -> NavigationTab.TANTANGAN
        is ScreenDestination.QuizHub, is ScreenDestination.ActiveQuiz -> NavigationTab.KUIS
        is ScreenDestination.Playground -> NavigationTab.PLAYGROUND
        is ScreenDestination.Profile -> NavigationTab.PROFIL
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (isTopLevelScreen) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    tonalElevation = 0.dp,
                    modifier = Modifier.testTag("main_bottom_nav")
                ) {
                    NavigationTab.entries.forEach { tab ->
                        val isSelected = selectedTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                viewModel.navigateTo(tab.destination)
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.label
                                )
                            },
                            label = {
                                Text(
                                    text = tab.label,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentScreen,
            label = "screen_crossfade",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { destination ->
            when (destination) {
                is ScreenDestination.Learn -> LearnScreen(viewModel = viewModel)
                is ScreenDestination.LessonDetail -> LessonDetailScreen(
                    lessonId = destination.lessonId,
                    viewModel = viewModel
                )
                is ScreenDestination.DailyChallenge -> DailyChallengeScreen(viewModel = viewModel)
                is ScreenDestination.QuizHub, is ScreenDestination.ActiveQuiz -> QuizScreen(viewModel = viewModel)
                is ScreenDestination.Playground -> PlaygroundScreen(viewModel = viewModel)
                is ScreenDestination.Profile -> ProfileScreen(viewModel = viewModel)
            }
        }
    }
}
