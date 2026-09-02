package com.example.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ShinobuBorder
import com.example.ui.theme.ShinobuDarkBg
import com.example.ui.theme.ShinobuDeepPurple
import com.example.ui.theme.ShinobuGoldReward
import com.example.ui.theme.ShinobuLavenderLight
import com.example.ui.theme.ShinobuLilac
import com.example.ui.theme.ShinobuMint
import com.example.ui.theme.ShinobuPinkAccent
import com.example.ui.theme.ShinobuTealDark
import com.example.ui.theme.ShinobuVioletPrimary
import com.example.ui.theme.TextDark

/**
 * Bespoke Shinobu Kocho anime chibi avatar vector.
 */
@Composable
fun ShinobuChibiAvatar(
    modifier: Modifier = Modifier,
    sizeDp: Dp = 68.dp,
    isHappy: Boolean = true,
    onClick: () -> Unit = {},
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shinobu_idle")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "chibi_float"
    )

    Box(
        modifier = modifier
            .size(sizeDp)
            .graphicsLayer { translationY = floatOffset }
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ShinobuLavenderLight,
                        ShinobuLilac.copy(alpha = 0.6f),
                        ShinobuVioletPrimary
                    )
                )
            )
            .border(2.dp, Color.White, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(sizeDp)) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f

            // Hair Back (Dark Violet gradient to Purple tips)
            val hairBackPath = Path().apply {
                moveTo(cx - w * 0.35f, cy + h * 0.2f)
                cubicTo(cx - w * 0.45f, cy - h * 0.35f, cx + w * 0.45f, cy - h * 0.35f, cx + w * 0.35f, cy + h * 0.2f)
                cubicTo(cx + w * 0.2f, cy + h * 0.35f, cx - w * 0.2f, cy + h * 0.35f, cx - w * 0.35f, cy + h * 0.2f)
                close()
            }
            drawPath(
                path = hairBackPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1E102E), Color(0xFF3B1556), ShinobuVioletPrimary),
                    startY = cy - h * 0.35f,
                    endY = cy + h * 0.35f
                )
            )

            // Butterfly Hair Accessory on Head (Upper Right)
            val clipCx = cx + w * 0.24f
            val clipCy = cy - h * 0.28f
            val clipWingL = Path().apply {
                moveTo(clipCx, clipCy)
                cubicTo(clipCx - w * 0.15f, clipCy - h * 0.15f, clipCx - w * 0.18f, clipCy + h * 0.05f, clipCx, clipCy)
                close()
            }
            val clipWingR = Path().apply {
                moveTo(clipCx, clipCy)
                cubicTo(clipCx + w * 0.15f, clipCy - h * 0.15f, clipCx + w * 0.18f, clipCy + h * 0.05f, clipCx, clipCy)
                close()
            }
            drawPath(clipWingL, brush = Brush.linearGradient(listOf(ShinobuMint, ShinobuLilac)))
            drawPath(clipWingR, brush = Brush.linearGradient(listOf(ShinobuMint, ShinobuLilac)))
            drawCircle(Color.White, radius = 2.5f, center = Offset(clipCx, clipCy))

            // Face (Soft peach skin)
            drawOval(
                color = Color(0xFFFFECE5),
                topLeft = Offset(cx - w * 0.26f, cy - h * 0.18f),
                size = Size(w * 0.52f, h * 0.46f)
            )

            // Blushes (Wisteria pink)
            drawCircle(
                color = ShinobuPinkAccent.copy(alpha = 0.5f),
                radius = w * 0.06f,
                center = Offset(cx - w * 0.18f, cy + h * 0.08f)
            )
            drawCircle(
                color = ShinobuPinkAccent.copy(alpha = 0.5f),
                radius = w * 0.06f,
                center = Offset(cx + w * 0.18f, cy + h * 0.08f)
            )

            // Eyes (Shinobu's iconic gradient purple anime eyes)
            if (isHappy) {
                // Cheerful arc eyes
                val eyeL = Path().apply {
                    moveTo(cx - w * 0.18f, cy)
                    cubicTo(cx - w * 0.14f, cy - h * 0.07f, cx - w * 0.08f, cy - h * 0.07f, cx - w * 0.04f, cy)
                }
                val eyeR = Path().apply {
                    moveTo(cx + w * 0.04f, cy)
                    cubicTo(cx + w * 0.08f, cy - h * 0.07f, cx + w * 0.14f, cy - h * 0.07f, cx + w * 0.18f, cy)
                }
                drawPath(eyeL, color = ShinobuDeepPurple, style = Stroke(width = 3.5f, cap = StrokeCap.Round))
                drawPath(eyeR, color = ShinobuDeepPurple, style = Stroke(width = 3.5f, cap = StrokeCap.Round))
            } else {
                // Big anime eyes
                drawOval(
                    brush = Brush.verticalGradient(listOf(Color(0xFF23073D), ShinobuVioletPrimary, ShinobuLilac)),
                    topLeft = Offset(cx - w * 0.18f, cy - h * 0.06f),
                    size = Size(w * 0.12f, h * 0.15f)
                )
                drawOval(
                    brush = Brush.verticalGradient(listOf(Color(0xFF23073D), ShinobuVioletPrimary, ShinobuLilac)),
                    topLeft = Offset(cx + w * 0.06f, cy - h * 0.06f),
                    size = Size(w * 0.12f, h * 0.15f)
                )
                // Eye highlights
                drawCircle(Color.White, radius = 2.5f, center = Offset(cx - w * 0.14f, cy - h * 0.03f))
                drawCircle(Color.White, radius = 2.5f, center = Offset(cx + w * 0.10f, cy - h * 0.03f))
            }

            // Gentle smile
            val mouthPath = Path().apply {
                moveTo(cx - w * 0.06f, cy + h * 0.12f)
                cubicTo(cx, cy + h * 0.18f, cx, cy + h * 0.18f, cx + w * 0.06f, cy + h * 0.12f)
            }
            drawPath(mouthPath, color = Color(0xFFC2410C), style = Stroke(width = 2f, cap = StrokeCap.Round))

            // Front Bangs & Side Locks
            val bangs = Path().apply {
                moveTo(cx - w * 0.28f, cy - h * 0.12f)
                cubicTo(cx - w * 0.25f, cy - h * 0.3f, cx + w * 0.25f, cy - h * 0.3f, cx + w * 0.28f, cy - h * 0.12f)
                // Left lock
                lineTo(cx + w * 0.18f, cy - h * 0.02f)
                // Middle parted bangs
                lineTo(cx + w * 0.06f, cy - h * 0.18f)
                lineTo(cx - w * 0.06f, cy - h * 0.18f)
                lineTo(cx - w * 0.18f, cy - h * 0.02f)
                close()
            }
            drawPath(
                path = bangs,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF28103A), ShinobuVioletPrimary, ShinobuLilac),
                    startY = cy - h * 0.3f,
                    endY = cy
                )
            )

            // Butterfly Haori pattern on bottom collar
            val collarL = Path().apply {
                moveTo(cx - w * 0.3f, cy + h * 0.35f)
                lineTo(cx, cy + h * 0.24f)
                lineTo(cx - w * 0.3f, cy + h * 0.48f)
                close()
            }
            val collarR = Path().apply {
                moveTo(cx + w * 0.3f, cy + h * 0.35f)
                lineTo(cx, cy + h * 0.24f)
                lineTo(cx + w * 0.3f, cy + h * 0.48f)
                close()
            }
            drawPath(collarL, brush = Brush.linearGradient(listOf(Color.White, ShinobuMint)))
            drawPath(collarR, brush = Brush.linearGradient(listOf(Color.White, ShinobuMint)))
        }
    }
}

/**
 * Interactive Banner with Shinobu speech bubble and rank badge matching Geometric Balance styling.
 */
@Composable
fun ShinobuSpeechHeader(
    quote: String,
    score: Int,
    streak: Int,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // Main soft green speech card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFE8F5E9),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF81C784))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shinobu Avatar in white rounded-2xl box with purple border
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF9C27B0)),
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(onClick = onAvatarClick)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        ShinobuChibiAvatar(
                            sizeDp = 50.dp,
                            isHappy = true,
                            onClick = onAvatarClick
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Speech text & Author label
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "\"$quote\"",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20),
                            lineHeight = 17.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "- SHINOBU KOCHO",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF4A148C),
                            letterSpacing = 0.5.sp
                        )

                        if (streak > 1) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF9C27B0)
                            ) {
                                Text(
                                    text = "🔥 ${streak}x Streak",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Floating butterfly accent at top-right
        Text(
            text = "🦋",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 2.dp, end = 6.dp)
        )
    }
}
