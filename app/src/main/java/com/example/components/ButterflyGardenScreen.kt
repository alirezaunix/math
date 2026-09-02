package com.example.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.ButterflyBadge
import com.example.model.ShinobuRank
import com.example.sound.ShinobuSoundPlayer
import com.example.ui.theme.ShinobuGoldReward

/**
 * Shinobu's Butterfly Garden: Reward and collection sanctuary in Geometric Balance theme.
 */
@Composable
fun ButterflyGardenScreen(
    score: Int,
    bestStreak: Int,
    totalCorrect: Int,
    badges: List<ButterflyBadge>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rank = ShinobuRank.fromScore(score)
    var selectedBadgeForDetail by remember { mutableStateOf<ButterflyBadge?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F2FF))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ShinobuButterfly(
                        sizeDp = 30.dp,
                        wingColor = Color(0xFFE1BEE7),
                        tipColor = Color(0xFF9C27B0)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Butterfly Sanctuary 🌸",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF4A148C)
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE1BEE7)),
                    modifier = Modifier.size(38.dp).clickable(onClick = onClose)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close garden",
                            tint = Color(0xFF4A148C),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Rank & Level Showcase Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                color = Color(0xFF4A148C),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF9C27B0))
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF4A148C), Color(0xFF6A1B9A), Color(0xFF7B1FA2))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ShinobuChibiAvatar(
                            sizeDp = 64.dp,
                            isHappy = true,
                            onClick = { ShinobuSoundPlayer.playCorrectChime() }
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = rank.badgeEmoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = rank.title,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFE8F5E9)
                                )
                            }
                            Text(
                                text = rank.subtitle,
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Score & next rank progress
                            val nextRank = ShinobuRank.entries.getOrNull(rank.ordinal + 1)
                            val progressFraction = if (nextRank != null) {
                                val span = nextRank.minPoints - rank.minPoints
                                val currentInSpan = score - rank.minPoints
                                (currentInSpan.toFloat() / span.toFloat()).coerceIn(0f, 1f)
                            } else {
                                1f
                            }

                            LinearProgressIndicator(
                                progress = { progressFraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFFFFD54F),
                                trackColor = Color.White.copy(alpha = 0.25f),
                                strokeCap = StrokeCap.Round
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "★ $score Points",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD54F)
                                )
                                Text(
                                    text = if (nextRank != null) "Next: ${nextRank.minPoints} pts" else "Max Rank!",
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Stats Row (Total Correct, Best Streak, Charms Unlocked)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Solved",
                    value = "$totalCorrect",
                    emoji = "✨",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Best Streak",
                    value = "${bestStreak}x",
                    emoji = "🔥",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Charms",
                    value = "${badges.count { it.isUnlocked }}/${badges.size}",
                    emoji = "🦋",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Shinobu Butterfly Badges & Charms:",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF4A148C),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 12 Badges Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(badges) { badge ->
                    BadgeGridItem(
                        badge = badge,
                        onClick = {
                            selectedBadgeForDetail = badge
                            if (badge.isUnlocked) {
                                ShinobuSoundPlayer.playRewardFanfare()
                            }
                        }
                    )
                }
            }
        }

        // Badge Detail Modal Dialog
        selectedBadgeForDetail?.let { badge ->
            BadgeDetailDialog(
                badge = badge,
                onDismiss = { selectedBadgeForDetail = null }
            )
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    emoji: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE1BEE7))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 16.sp)
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF4A148C)
            )
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9C27B0)
            )
        }
    }
}

@Composable
private fun BadgeGridItem(
    badge: ButterflyBadge,
    onClick: () -> Unit,
) {
    val isUnlocked = badge.isUnlocked
    val bgBrush = if (isUnlocked) {
        Brush.verticalGradient(listOf(Color.White, Color(0xFFF3E5F5)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFEDE7F6), Color(0xFFE1BEE7).copy(alpha = 0.5f)))
    }

    Surface(
        modifier = Modifier
            .shadow(if (isUnlocked) 3.dp else 1.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .testTag("badge_item_${badge.id}"),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isUnlocked) 2.dp else 1.5.dp,
            color = if (isUnlocked) Color(0xFF9C27B0) else Color(0xFFE1BEE7)
        )
    ) {
        Column(
            modifier = Modifier
                .background(bgBrush)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (isUnlocked) Color(0xFFF8F2FF) else Color(0xFFE1BEE7),
                        CircleShape
                    )
                    .border(
                        1.5.dp,
                        if (isUnlocked) Color(0xFF9C27B0) else Color(0xFFCE93D8),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Text(text = badge.emoji, fontSize = 22.sp)
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color(0xFF7B1FA2),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = badge.title,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isUnlocked) Color(0xFF4A148C) else Color(0xFF7B1FA2).copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun BadgeDetailDialog(
    badge: ButterflyBadge,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            shadowElevation = 12.dp,
            border = androidx.compose.foundation.BorderStroke(3.5.dp, Color(0xFF9C27B0)),
            modifier = Modifier.fillMaxWidth(0.92f)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Butterfly Icon
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            if (badge.isUnlocked) Color(0xFFF3E5F5) else Color(0xFFEDE7F6),
                            CircleShape
                        )
                        .border(2.5.dp, if (badge.isUnlocked) Color(0xFF9C27B0) else Color(0xFFCE93D8), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (badge.isUnlocked) badge.emoji else "🔒",
                        fontSize = 36.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = badge.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF4A148C),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (badge.isUnlocked) "✨ Unlocked Charm ✨" else "🔒 Locked Charm",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (badge.isUnlocked) Color(0xFF9C27B0) else Color(0xFF7B1FA2)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = badge.description,
                    fontSize = 13.sp,
                    color = Color(0xFF4A148C),
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF8F2FF),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE1BEE7)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "How to Unlock: ${badge.unlockRequirement}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9C27B0),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF9C27B0),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable(onClick = onDismiss)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (badge.isUnlocked) "Moshi Moshi! 💜" else "I will earn this! 🦋",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

