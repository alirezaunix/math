package com.example.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ShinobuDeepPurple
import com.example.ui.theme.ShinobuGoldReward
import com.example.ui.theme.ShinobuLavenderLight
import com.example.ui.theme.ShinobuLilac
import com.example.ui.theme.ShinobuMint
import com.example.ui.theme.ShinobuPinkAccent
import com.example.ui.theme.ShinobuVioletPrimary
import kotlinx.coroutines.delay
import kotlin.random.Random

data class Particle(
    val id: Int,
    val startX: Float,
    val startY: Float,
    val targetX: Float,
    val targetY: Float,
    val size: Float,
    val color: Color,
    val isButterfly: Boolean,
)

/**
 * Full-screen celebration overlay when answering correctly or mastering a table.
 */
@Composable
fun CelebrationOverlay(
    trigger: Long,
    pointsEarned: Int,
    streak: Int,
    isMastery: Boolean = false,
    modifier: Modifier = Modifier,
) {
    if (trigger <= 0L) return

    val progress = remember(trigger) { Animatable(0f) }
    val particles = remember(trigger) {
        val list = mutableListOf<Particle>()
        val colors = listOf(ShinobuLilac, ShinobuMint, ShinobuPinkAccent, ShinobuVioletPrimary, ShinobuGoldReward, Color.White)
        for (i in 0 until 28) {
            val angle = Random.nextDouble(0.0, 2.0 * Math.PI)
            val distance = Random.nextFloat() * 400f + 120f
            list.add(
                Particle(
                    id = i,
                    startX = 0.5f,
                    startY = 0.45f,
                    targetX = (0.5f + (Math.cos(angle) * distance / 1000f)).toFloat(),
                    targetY = (0.45f + (Math.sin(angle) * distance / 1000f) - 0.2f).toFloat(),
                    size = Random.nextFloat() * 16f + 8f,
                    color = colors[Random.nextInt(colors.size)],
                    isButterfly = i % 3 == 0
                )
            )
        }
        list
    }

    LaunchedEffect(trigger) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing)
        )
    }

    if (progress.value < 1f) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = (1f - progress.value).coerceIn(0f, 1f)
                },
            contentAlignment = Alignment.Center
        ) {
            // Particle Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val t = progress.value

                particles.forEach { p ->
                    val curX = (p.startX + (p.targetX - p.startX) * t) * w
                    val curY = (p.startY + (p.targetY - p.startY) * t) * h

                    if (p.isButterfly) {
                        // Draw mini butterfly particle
                        val s = p.size * (1f - t * 0.4f)
                        val wingL = Path().apply {
                            moveTo(curX, curY)
                            cubicTo(curX - s, curY - s, curX - s * 1.2f, curY + s * 0.2f, curX, curY)
                            close()
                        }
                        val wingR = Path().apply {
                            moveTo(curX, curY)
                            cubicTo(curX + s, curY - s, curX + s * 1.2f, curY + s * 0.2f, curX, curY)
                            close()
                        }
                        drawPath(wingL, color = p.color)
                        drawPath(wingR, color = p.color)
                    } else {
                        // Sparkling star particle
                        drawCircle(
                            color = p.color,
                            radius = p.size * (1f - t * 0.5f) / 2f,
                            center = Offset(curX, curY)
                        )
                    }
                }
            }

            // Reward floating banner
            Surface(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = (1f + (1f - progress.value) * 0.3f)
                        scaleY = (1f + (1f - progress.value) * 0.3f)
                        translationY = -progress.value * 90f
                    }
                    .shadow(12.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                color = ShinobuDeepPurple
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                listOf(ShinobuDeepPurple, ShinobuVioletPrimary, ShinobuDeepPurple)
                            )
                        )
                        .border(1.5.dp, ShinobuMint, RoundedCornerShape(24.dp))
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isMastery) "👑" else "🦋",
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = if (isMastery) "Table Mastered!" else if (streak > 2) "${streak}x Streak! Awesome!" else "Brilliant!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ShinobuMint
                            )
                            Text(
                                text = "+$pointsEarned Butterfly Points",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ShinobuGoldReward
                            )
                        }
                    }
                }
            }
        }
    }
}
