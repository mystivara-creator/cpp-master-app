package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.screens.DailyChallengeScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.LessonDetailScreen
import com.example.ui.screens.PlaygroundScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.theme.CppBluePrimary
import com.example.ui.theme.CppNavyDark
import com.example.ui.theme.CppNavySurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.CppViewModel
import com.example.ui.viewmodel.ScreenDestination

enum class NavigationTab(val label: String, val icon: ImageVector, val destination: ScreenDestination) {
    BELAJAR("Belajar", Icons.AutoMirrored.Filled.MenuBook, ScreenDestination.Learn),
    TANTANGAN("Tantangan", Icons.Default.Bolt, ScreenDestination.DailyChallenge),
    KUIS("Kuis", Icons.Default.Quiz, ScreenDestination.QuizHub),
    PLAYGROUND("Playground", Icons.Default.Terminal, ScreenDestination.Playground),
    PROFIL("Profil", Icons.Default.Person, ScreenDestination.Profile)
}

class MainActivity : ComponentActivity() {
    private val viewModel: CppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: CppViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

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
                    modifier = Modifier.testTag("main_bottom_nav")
                ) {
                    NavigationTab.values().forEach { tab ->
                        val isSelected = selectedTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                viewModel.navigateTo(tab.destination)
                            },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label
                                )
                            },
                            label = {
                                Text(
                                    text = tab.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
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
