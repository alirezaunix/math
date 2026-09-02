package com.example.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.ButterflyBadge
import com.example.model.MultiplicationProblem
import com.example.model.ShinobuEncouragements
import com.example.model.ShinobuGentleTips
import com.example.model.ShinobuRank
import com.example.model.StudyMode
import com.example.model.TableProgress
import com.example.sound.ShinobuSoundPlayer
import com.example.ui.theme.ShinobuGoldReward
import com.example.ui.theme.ShinobuLilac
import com.example.ui.theme.ShinobuMint
import com.example.ui.theme.ShinobuPinkAccent
import com.example.ui.theme.ShinobuVioletPrimary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class MathUiState(
    val currentProblem: MultiplicationProblem = MultiplicationProblem(2, 4),
    val isCardFlipped: Boolean = false,
    val studyMode: StudyMode = StudyMode.FLASHCARD,
    val selectedTable: Int = 2, // Default table of 2 for beginner 10yo friendly start
    val quizOptions: List<Int> = listOf(6, 8, 10, 12),
    val selectedQuizOption: Int? = null,
    val isQuizAnswerChecked: Boolean = false,
    val keypadInput: String = "",
    val score: Int = 0,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val totalCorrect: Int = 0,
    val tableProgress: Map<Int, TableProgress> = (1..12).associateWith { TableProgress(it) },
    val badges: List<ButterflyBadge> = defaultBadges,
    val shinobuQuote: String = "Moshi Moshi! Let's master multiplication with butterfly grace! 💜",
    val celebrationTrigger: Long = 0L,
    val lastPointsEarned: Int = 10,
    val isGardenOpen: Boolean = false,
)

private val defaultBadges = listOf(
    ButterflyBadge(
        id = "first_flutter",
        title = "First Flutter",
        description = "Solved your very first multiplication problem with Shinobu!",
        emoji = "🦋",
        primaryColor = ShinobuLilac,
        secondaryColor = ShinobuMint,
        unlockRequirement = "Solve 1 problem correctly",
        isUnlocked = false
    ),
    ButterflyBadge(
        id = "wisteria_spark",
        title = "Wisteria Spark",
        description = "Achieved a 3x calculation combo!",
        emoji = "🌸",
        primaryColor = ShinobuPinkAccent,
        secondaryColor = ShinobuLilac,
        unlockRequirement = "Get a 3x streak",
        isUnlocked = false
    ),
    ButterflyBadge(
        id = "table_2_master",
        title = "Double Butterfly Wings",
        description = "Mastered the 2x multiplication table!",
        emoji = "✨",
        primaryColor = ShinobuMint,
        secondaryColor = ShinobuVioletPrimary,
        unlockRequirement = "Solve 5 problems in Table 2",
        isUnlocked = false
    ),
    ButterflyBadge(
        id = "table_3_master",
        title = "Triple Poison Stinger",
        description = "Mastered the 3x multiplication table!",
        emoji = "🗡️",
        primaryColor = ShinobuVioletPrimary,
        secondaryColor = ShinobuPinkAccent,
        unlockRequirement = "Solve 5 problems in Table 3",
        isUnlocked = false
    ),
    ButterflyBadge(
        id = "table_5_master",
        title = "Golden Haori Bloom",
        description = "Mastered the easy 5x counting jump!",
        emoji = "⭐",
        primaryColor = ShinobuGoldReward,
        secondaryColor = ShinobuLilac,
        unlockRequirement = "Solve 5 problems in Table 5",
        isUnlocked = false
    ),
    ButterflyBadge(
        id = "streak_5",
        title = "Dance of the Butterfly",
        description = "Incredible 5 in a row streak without a single mistake!",
        emoji = "💜",
        primaryColor = ShinobuLilac,
        secondaryColor = ShinobuMint,
        unlockRequirement = "Get a 5x streak",
        isUnlocked = false
    ),
    ButterflyBadge(
        id = "table_10_master",
        title = "Decuple Diamond",
        description = "Mastered the 10x multiplication table!",
        emoji = "💎",
        primaryColor = Color(0xFF38BDF8),
        secondaryColor = ShinobuLilac,
        unlockRequirement = "Solve 5 problems in Table 10",
        isUnlocked = false
    ),
    ButterflyBadge(
        id = "table_7_master",
        title = "Mystic Lucky Seven",
        description = "Mastered the tricky 7x multiplication table!",
        emoji = "🌙",
        primaryColor = Color(0xFFA855F7),
        secondaryColor = ShinobuGoldReward,
        unlockRequirement = "Solve 5 problems in Table 7",
        isUnlocked = false
    ),
    ButterflyBadge(
        id = "streak_10",
        title = "Hashira Swiftness",
        description = "Unstoppable 10x streak! Calculations at lightning speed!",
        emoji = "⚡",
        primaryColor = ShinobuGoldReward,
        secondaryColor = ShinobuVioletPrimary,
        unlockRequirement = "Get a 10x streak",
        isUnlocked = false
    ),
    ButterflyBadge(
        id = "table_12_master",
        title = "Grand Wisteria Crown",
        description = "Mastered the 12x multiplication table!",
        emoji = "👑",
        primaryColor = ShinobuGoldReward,
        secondaryColor = ShinobuMint,
        unlockRequirement = "Solve 5 problems in Table 12",
        isUnlocked = false
    ),
    ButterflyBadge(
        id = "math_scholar_25",
        title = "Butterfly Scholar",
        description = "Solved 25 total multiplication questions correctly!",
        emoji = "📜",
        primaryColor = ShinobuVioletPrimary,
        secondaryColor = ShinobuLilac,
        unlockRequirement = "Solve 25 total problems",
        isUnlocked = false
    ),
    ButterflyBadge(
        id = "hashira_supreme",
        title = "Insect Hashira Supreme",
        description = "Reached 500+ Butterfly Points and true mastery!",
        emoji = "🏆",
        primaryColor = ShinobuGoldReward,
        secondaryColor = Color(0xFFFB7185),
        unlockRequirement = "Reach 500 Butterfly Points",
        isUnlocked = false
    )
)

class MathViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MathUiState())
    val uiState: StateFlow<MathUiState> = _uiState.asStateFlow()

    init {
        generateNewProblem(_uiState.value.selectedTable)
    }

    fun flipCard() {
        _uiState.update { it.copy(isCardFlipped = !it.isCardFlipped) }
        ShinobuSoundPlayer.playCardFlip()
    }

    fun handleCorrectAnswer() {
        val currentProblem = _uiState.value.currentProblem
        val newStreak = _uiState.value.streak + 1
        val newBestStreak = maxOf(_uiState.value.bestStreak, newStreak)
        val points = calculatePoints(newStreak)
        val newScore = _uiState.value.score + points
        val newTotalCorrect = _uiState.value.totalCorrect + 1

        // Update table progress
        val tableNum = currentProblem.multiplier
        val existingProgress = _uiState.value.tableProgress[tableNum] ?: TableProgress(tableNum)
        val updatedProgress = existingProgress.copy(
            correctCount = existingProgress.correctCount + 1,
            totalCount = existingProgress.totalCount + 1
        )
        val updatedMap = _uiState.value.tableProgress.toMutableMap()
        updatedMap[tableNum] = updatedProgress

        // Pick encouraging quote
        val randomQuote = ShinobuEncouragements.random()

        _uiState.update { state ->
            val updatedState = state.copy(
                score = newScore,
                streak = newStreak,
                bestStreak = newBestStreak,
                totalCorrect = newTotalCorrect,
                tableProgress = updatedMap,
                shinobuQuote = randomQuote,
                celebrationTrigger = System.currentTimeMillis(),
                lastPointsEarned = points,
                isCardFlipped = false,
                isQuizAnswerChecked = false,
                selectedQuizOption = null,
                keypadInput = ""
            )
            checkBadgeUnlocks(updatedState)
        }

        ShinobuSoundPlayer.playCorrectChime()

        // Advance to next problem smoothly
        viewModelScope.launch {
            generateNewProblem(_uiState.value.selectedTable)
        }
    }

    fun handlePracticeAgain() {
        val currentProblem = _uiState.value.currentProblem
        val tableNum = currentProblem.multiplier
        val existingProgress = _uiState.value.tableProgress[tableNum] ?: TableProgress(tableNum)
        val updatedProgress = existingProgress.copy(
            totalCount = existingProgress.totalCount + 1
        )
        val updatedMap = _uiState.value.tableProgress.toMutableMap()
        updatedMap[tableNum] = updatedProgress

        val tipQuote = ShinobuGentleTips.random()

        _uiState.update { state ->
            state.copy(
                streak = 0,
                tableProgress = updatedMap,
                shinobuQuote = tipQuote,
                isCardFlipped = false,
                isQuizAnswerChecked = false,
                selectedQuizOption = null,
                keypadInput = ""
            )
        }

        viewModelScope.launch {
            generateNewProblem(_uiState.value.selectedTable)
        }
    }

    fun selectQuizOption(option: Int) {
        val isCorrect = option == _uiState.value.currentProblem.answer
        _uiState.update {
            it.copy(
                selectedQuizOption = option,
                isQuizAnswerChecked = true
            )
        }

        if (isCorrect) {
            handleCorrectAnswer()
        } else {
            handlePracticeAgain()
        }
    }

    fun onKeypadDigit(digit: String) {
        if (_uiState.value.keypadInput.length < 4) {
            _uiState.update { it.copy(keypadInput = it.keypadInput + digit) }
        }
    }

    fun onKeypadDelete() {
        if (_uiState.value.keypadInput.isNotEmpty()) {
            _uiState.update { it.copy(keypadInput = it.keypadInput.dropLast(1)) }
        }
    }

    fun onKeypadSubmit() {
        val entered = _uiState.value.keypadInput.toIntOrNull() ?: return
        if (entered == _uiState.value.currentProblem.answer) {
            handleCorrectAnswer()
        } else {
            handlePracticeAgain()
        }
    }

    fun selectTable(table: Int) {
        _uiState.update {
            it.copy(
                selectedTable = table,
                isCardFlipped = false,
                isQuizAnswerChecked = false,
                selectedQuizOption = null,
                keypadInput = ""
            )
        }
        generateNewProblem(table)
    }

    fun setStudyMode(mode: StudyMode) {
        _uiState.update {
            it.copy(
                studyMode = mode,
                isCardFlipped = false,
                isQuizAnswerChecked = false,
                selectedQuizOption = null,
                keypadInput = ""
            )
        }
    }

    fun toggleGarden(open: Boolean) {
        _uiState.update { it.copy(isGardenOpen = open) }
    }

    fun triggerRandomShinobuQuote() {
        _uiState.update {
            it.copy(shinobuQuote = ShinobuEncouragements.random())
        }
        ShinobuSoundPlayer.playRewardFanfare()
    }

    private fun generateNewProblem(selectedTable: Int) {
        val mult1 = if (selectedTable == 0) Random.nextInt(1, 13) else selectedTable
        val mult2 = Random.nextInt(1, 13)
        val problem = MultiplicationProblem(mult1, mult2)

        // Generate 4 plausible distinct quiz options
        val correct = problem.answer
        val optionsSet = mutableSetOf(correct)
        while (optionsSet.size < 4) {
            val offset = Random.nextInt(-4, 5) * mult1
            val fake = if (offset != 0 && correct + offset > 0) correct + offset else Random.nextInt(1, 144)
            optionsSet.add(fake)
        }
        val optionsList = optionsSet.toList().shuffled()

        _uiState.update {
            it.copy(
                currentProblem = problem,
                quizOptions = optionsList,
                isCardFlipped = false,
                isQuizAnswerChecked = false,
                selectedQuizOption = null,
                keypadInput = ""
            )
        }
    }

    private fun calculatePoints(streak: Int): Int {
        return when {
            streak >= 10 -> 30
            streak >= 5 -> 20
            streak >= 3 -> 15
            else -> 10
        }
    }

    private fun checkBadgeUnlocks(state: MathUiState): MathUiState {
        val updatedBadges = state.badges.map { badge ->
            if (badge.isUnlocked) {
                badge
            } else {
                val shouldUnlock = when (badge.id) {
                    "first_flutter" -> state.totalCorrect >= 1
                    "wisteria_spark" -> state.bestStreak >= 3
                    "table_2_master" -> (state.tableProgress[2]?.correctCount ?: 0) >= 5
                    "table_3_master" -> (state.tableProgress[3]?.correctCount ?: 0) >= 5
                    "table_5_master" -> (state.tableProgress[5]?.correctCount ?: 0) >= 5
                    "streak_5" -> state.bestStreak >= 5
                    "table_10_master" -> (state.tableProgress[10]?.correctCount ?: 0) >= 5
                    "table_7_master" -> (state.tableProgress[7]?.correctCount ?: 0) >= 5
                    "streak_10" -> state.bestStreak >= 10
                    "table_12_master" -> (state.tableProgress[12]?.correctCount ?: 0) >= 5
                    "math_scholar_25" -> state.totalCorrect >= 25
                    "hashira_supreme" -> state.score >= 500
                    else -> false
                }
                if (shouldUnlock) {
                    badge.copy(isUnlocked = true)
                } else {
                    badge
                }
            }
        }
        return state.copy(badges = updatedBadges)
    }
}
