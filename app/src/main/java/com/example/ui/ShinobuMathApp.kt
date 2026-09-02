package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.components.ButterflyGardenScreen
import com.example.components.CelebrationOverlay
import com.example.components.FlippableMathCard
import com.example.components.InteractiveKeypadCard
import com.example.components.InteractiveQuizCard
import com.example.components.ShinobuButterfly
import com.example.components.ShinobuSpeechHeader
import com.example.components.TableSelectorBar
import com.example.model.StudyMode
import com.example.viewmodel.MathViewModel

@Composable
fun ShinobuMathApp(
    viewModel: MathViewModel = viewModel(),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        containerColor = Color(0xFFF8F2FF),
        bottomBar = {
            GeometricBottomNavBar(
                currentMode = uiState.studyMode,
                isGardenOpen = uiState.isGardenOpen,
                onSelectMode = {
                    if (uiState.isGardenOpen) viewModel.toggleGarden(false)
                    viewModel.setStudyMode(it)
                },
                onOpenGarden = { viewModel.toggleGarden(true) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Learning Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top App Bar in Geometric Balance
                TopNavHeader(
                    score = uiState.score,
                    unlockedBadgesCount = uiState.badges.count { it.isUnlocked },
                    onOpenGarden = { viewModel.toggleGarden(true) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Geometric Balance Progress Gauge
                GeometricProgressBar(
                    totalCorrect = uiState.totalCorrect,
                    score = uiState.score
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Shinobu Character Speech Header in Geometric Balance Soft Green
                ShinobuSpeechHeader(
                    quote = uiState.shinobuQuote,
                    score = uiState.score,
                    streak = uiState.streak,
                    onAvatarClick = { viewModel.triggerRandomShinobuQuote() }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Study Mode Tabs (Flashcard, Quiz, Keypad)
                StudyModeTabRow(
                    selectedMode = uiState.studyMode,
                    onSelectMode = { viewModel.setStudyMode(it) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Table Selector Bar (1 to 12)
                TableSelectorBar(
                    selectedTable = uiState.selectedTable,
                    tableProgressMap = uiState.tableProgress,
                    onSelectTable = { viewModel.selectTable(it) }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Interactive Active Problem Area
                AnimatedContent(
                    targetState = uiState.studyMode,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "mode_switch_anim"
                ) { mode ->
                    when (mode) {
                        StudyMode.FLASHCARD -> {
                            FlippableMathCard(
                                problem = uiState.currentProblem,
                                isFlipped = uiState.isCardFlipped,
                                onFlip = { viewModel.flipCard() },
                                onCorrectAnswer = { viewModel.handleCorrectAnswer() },
                                onPracticeAgain = { viewModel.handlePracticeAgain() }
                            )
                        }

                        StudyMode.QUIZ -> {
                            InteractiveQuizCard(
                                problem = uiState.currentProblem,
                                options = uiState.quizOptions,
                                onSelectOption = { viewModel.selectQuizOption(it) },
                                selectedOption = uiState.selectedQuizOption,
                                isAnswerChecked = uiState.isQuizAnswerChecked
                            )
                        }

                        StudyMode.KEYPAD -> {
                            InteractiveKeypadCard(
                                problem = uiState.currentProblem,
                                inputBuffer = uiState.keypadInput,
                                onDigitPress = { viewModel.onKeypadDigit(it) },
                                onDeletePress = { viewModel.onKeypadDelete() },
                                onSubmitPress = { viewModel.onKeypadSubmit() }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Reward Celebrations & Floating Particles Overlay
            CelebrationOverlay(
                trigger = uiState.celebrationTrigger,
                pointsEarned = uiState.lastPointsEarned,
                streak = uiState.streak
            )

            // Butterfly Garden Full View Modal
            if (uiState.isGardenOpen) {
                ButterflyGardenScreen(
                    score = uiState.score,
                    bestStreak = uiState.bestStreak,
                    totalCorrect = uiState.totalCorrect,
                    badges = uiState.badges,
                    onClose = { viewModel.toggleGarden(false) }
                )
            }
        }
    }
}

/**
 * Geometric Balance Header with circular avatar, imperial purple title, and level pill badge.
 */
@Composable
private fun TopNavHeader(
    score: Int,
    unlockedBadgesCount: Int,
    onOpenGarden: () -> Unit,
) {
    val level = (score / 40) + 1

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Avatar + Title
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Circular Avatar Frame
            Surface(
                shape = CircleShape,
                color = Color(0xFFE1BEE7),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF9C27B0)),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    ShinobuButterfly(
                        sizeDp = 24.dp,
                        wingColor = Color(0xFF9C27B0),
                        tipColor = Color(0xFF2DD4BF)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = "Insect Hashira",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF4A148C),
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "MULTIPLICATION MASTERY",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9C27B0),
                    letterSpacing = 1.2.sp
                )
            }
        }

        // Right Level & Points Badge
        Surface(
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .clickable(onClick = onOpenGarden)
                .testTag("open_garden_button"),
            shape = RoundedCornerShape(20.dp),
            color = Color.White.copy(alpha = 0.85f),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE1BEE7))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lvl $level",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF7B1FA2)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF9C27B0))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$score pts",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9C27B0)
                )
            }
        }
    }
}

/**
 * Geometric Balance Progress Bar with linear gauge and circular indicator thumb.
 */
@Composable
private fun GeometricProgressBar(
    totalCorrect: Int,
    score: Int,
) {
    val progressPercent = ((totalCorrect % 10) * 10).coerceIn(10, 100)
    val progressFraction = progressPercent / 100f

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(Color(0xFFE1BEE7))
        ) {
            // Filled bar
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressFraction)
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFF9C27B0))
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PROGRESS",
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF9C27B0),
                letterSpacing = 1.sp
            )
            Text(
                text = "$progressPercent% TO NEXT REWARD",
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF9C27B0),
                letterSpacing = 0.5.sp
            )
        }
    }
}

/**
 * Geometric Balance Mode Selector Bar.
 */
@Composable
private fun StudyModeTabRow(
    selectedMode: StudyMode,
    onSelectMode: (StudyMode) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF3E5F5),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE1BEE7)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            StudyMode.entries.forEach { mode ->
                val isSelected = selectedMode == mode
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectMode(mode) }
                        .testTag("mode_tab_${mode.name}"),
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) Color(0xFF9C27B0) else Color.Transparent,
                    shadowElevation = if (isSelected) 3.dp else 0.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = mode.iconText,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = mode.label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                            color = if (isSelected) Color.White else Color(0xFF4A148C)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Bottom Navigation Bar matching the Geometric Balance theme.
 */
@Composable
private fun GeometricBottomNavBar(
    currentMode: StudyMode,
    isGardenOpen: Boolean,
    onSelectMode: (StudyMode) -> Unit,
    onOpenGarden: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        color = Color.White,
        shadowElevation = 10.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3E5F5)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flashcard Tab
            NavBarItem(
                label = "Cards",
                icon = "🎴",
                isSelected = currentMode == StudyMode.FLASHCARD && !isGardenOpen,
                onClick = { onSelectMode(StudyMode.FLASHCARD) }
            )

            // Quiz Tab
            NavBarItem(
                label = "Quiz",
                icon = "⚔️",
                isSelected = currentMode == StudyMode.QUIZ && !isGardenOpen,
                onClick = { onSelectMode(StudyMode.QUIZ) }
            )

            // Keypad Tab
            NavBarItem(
                label = "Keypad",
                icon = "🔢",
                isSelected = currentMode == StudyMode.KEYPAD && !isGardenOpen,
                onClick = { onSelectMode(StudyMode.KEYPAD) }
            )

            // Sanctuary / Garden Tab
            NavBarItem(
                label = "Garden",
                icon = "🌸",
                isSelected = isGardenOpen,
                onClick = onOpenGarden
            )
        }
    }
}

@Composable
private fun NavBarItem(
    label: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFFF3E5F5) else Color.Transparent,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 16.sp
            )
            if (isSelected) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF4A148C)
                )
            }
        }
    }
}

