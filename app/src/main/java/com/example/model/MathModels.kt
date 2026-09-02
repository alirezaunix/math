package com.example.model

import androidx.compose.ui.graphics.Color

data class MultiplicationProblem(
    val multiplier: Int,
    val multiplicand: Int,
) {
    val answer: Int get() = multiplier * multiplicand
    val promptText: String get() = "$multiplier × $multiplicand"
}

enum class StudyMode(val label: String, val iconText: String) {
    FLASHCARD("Flashcard Flip", "🃏"),
    QUIZ("Butterfly Quiz", "🦋"),
    KEYPAD("Type Answer", "⌨️"),
}

data class ButterflyBadge(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val unlockRequirement: String,
    val isUnlocked: Boolean = false,
)

data class TableProgress(
    val tableNumber: Int,
    val correctCount: Int = 0,
    val totalCount: Int = 0,
) {
    val stars: Int
        get() = when {
            correctCount >= 20 -> 3
            correctCount >= 10 -> 2
            correctCount >= 5 -> 1
            else -> 0
        }
    val percentage: Float
        get() = if (totalCount == 0) 0f else (correctCount.toFloat() / totalCount.toFloat()).coerceIn(0f, 1f)
}

enum class ShinobuRank(
    val title: String,
    val subtitle: String,
    val minPoints: Int,
    val badgeEmoji: String,
) {
    TRAINEE("Butterfly Apprentice", "Beginning the journey of numbers", 0, "🌱"),
    SEEKER("Wisteria Guardian", "Mastering the fundamental tables", 60, "🌸"),
    INSECT_WARRIOR("Insect Breathing Tsuguko", "Dancing gracefully through multiplication", 160, "🗡️"),
    BUTTERFLY_DANCER("Dance of the Butterfly", "Swift and precise calculations", 320, "🦋"),
    HASHIRA_MASTER("Insect Hashira", "Supreme Multiplication Master", 550, "👑");

    companion object {
        fun fromScore(score: Int): ShinobuRank {
            return entries.lastOrNull { score >= it.minPoints } ?: TRAINEE
        }
    }
}

val ShinobuEncouragements = listOf(
    "Moshi Moshi! Splendid calculation! 💜",
    "Dance of the Butterfly: Flawless multiplication! 🦋",
    "Ara ara~ You're becoming a true math Hashira! ✨",
    "Fabulous! Your butterfly wings shine so brightly! 🌸",
    "Insect Breathing: Pure precision! Keep soaring! 💜",
    "Magnificent! Shinobu is so proud of you! 👑",
    "Poison to tricky math! You solved it effortlessly! 🗡️",
    "Wisteria petals bloom for your sharp mind! 🌺"
)

val ShinobuGentleTips = listOf(
    "Don't worry at all! Even Hashira practice every single day! 💜",
    "Let's flip the card and see the butterfly pattern together! 🦋",
    "Take a gentle breath~ You've got this on the next turn! 🌸",
    "Practice makes our butterfly wings stronger! Let's try! ✨"
)
