package com.example.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MultiplicationProblem
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.PracticeOrange
import com.example.ui.theme.ShinobuBorder
import com.example.ui.theme.ShinobuCardBg
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
 * 3D Flippable Flashcard with Shinobu Kocho aesthetic.
 */
@Composable
fun FlippableMathCard(
    problem: MultiplicationProblem,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    onCorrectAnswer: () -> Unit,
    onPracticeAgain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Rotation animation between 0 and 180 degrees
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing),
        label = "card_flip_rotation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(390.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 14f * density
            }
            .shadow(12.dp, RoundedCornerShape(36.dp))
            .testTag("flashcard_item"),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        if (rotation <= 90f) {
            // FRONT OF CARD
            CardFront(
                problem = problem,
                onFlip = onFlip
            )
        } else {
            // BACK OF CARD (Mirrored back so text isn't reversed)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f }
            ) {
                CardBack(
                    problem = problem,
                    onCorrectAnswer = onCorrectAnswer,
                    onPracticeAgain = onPracticeAgain,
                    onFlipBack = onFlip
                )
            }
        }
    }
}

@Composable
private fun CardFront(
    problem: MultiplicationProblem,
    onFlip: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .border(3.5.dp, Color(0xFF9C27B0), RoundedCornerShape(36.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onFlip
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        // Geometric background dot grid & ambient blurred glow orbs
        GeometricCardBackground()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(vertical = 8.dp)
        ) {
            // Card Question Header with butterfly
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShinobuButterfly(
                    sizeDp = 28.dp,
                    wingColor = Color(0xFFE1BEE7),
                    tipColor = Color(0xFF9C27B0)
                )

                Text(
                    text = "QUESTION ${problem.multiplier}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF9C27B0),
                    letterSpacing = 1.sp
                )

                ShinobuButterfly(
                    sizeDp = 28.dp,
                    wingColor = Color(0xFFE1BEE7),
                    tipColor = Color(0xFF9C27B0)
                )
            }

            // Big Multiplication Equation
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${problem.multiplier}",
                        fontSize = 68.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF4A148C)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = "×",
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9C27B0)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = "${problem.multiplicand}",
                        fontSize = 68.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF4A148C)
                    )
                }

                Text(
                    text = "= ?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF9C27B0)
                )
            }

            // Tap to Flip Pill Button
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF9C27B0),
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(20.dp))
                    .clickable(onClick = onFlip)
                    .testTag("flip_card_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Flip to reveal",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tap to Flip Answer ↻",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Bottom Geometric Balance Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF9C27B0).copy(alpha = 0.3f))
                )
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF9C27B0))
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF9C27B0).copy(alpha = 0.3f))
                )
            }
        }
    }
}

@Composable
private fun CardBack(
    problem: MultiplicationProblem,
    onCorrectAnswer: () -> Unit,
    onPracticeAgain: () -> Unit,
    onFlipBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .border(3.5.dp, Color(0xFF9C27B0), RoundedCornerShape(36.dp))
            .padding(18.dp)
    ) {
        // Geometric Background
        GeometricCardBackground()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${problem.multiplier} × ${problem.multiplicand}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF4A148C)
                )
                Text(
                    text = "ANSWER REVEALED ✨",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF9C27B0),
                    letterSpacing = 0.5.sp
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF3E5F5),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onFlipBack)
                ) {
                    Text(
                        text = "Flip ↻",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7B1FA2),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Big Answer Centerpiece
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Text(
                    text = "${problem.answer}",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF4A148C)
                )

                Text(
                    text = "${problem.multiplier} groups of ${problem.multiplicand} = ${problem.answer}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9C27B0)
                )
            }

            // Visual Dot Grid Helper
            ButterflyMultiplicationVisualizer(
                multiplier = problem.multiplier,
                multiplicand = problem.multiplicand,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            // Tactile 3D Action Buttons (Practice & Win)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Practice Again button (Tactile bottom border)
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .shadow(2.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(onClick = onPracticeAgain)
                        .testTag("practice_again_button"),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE1BEE7))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawBottomBorder(Color(0xFFE1BEE7), 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Practice again",
                                tint = Color(0xFF4A148C),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Practice 🔄",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4A148C)
                            )
                        }
                    }
                }

                // Correct / Win Advance button (Tactile bottom border)
                Surface(
                    modifier = Modifier
                        .weight(1.3f)
                        .height(52.dp)
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(onClick = onCorrectAnswer)
                        .testTag("win_advance_button"),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF9C27B0)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawBottomBorder(Color(0xFF7B1FA2), 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "I got it right",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "I Knew It! ✨",
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
}

/**
 * Geometric background texture with radial dot grid and ambient colored orbs.
 */
@Composable
private fun GeometricCardBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Top right soft purple ambient glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFE1BEE7).copy(alpha = 0.45f), Color.Transparent),
                center = Offset(w + 10f, -10f),
                radius = 160f
            ),
            radius = 160f,
            center = Offset(w + 10f, -10f)
        )

        // Bottom left soft cyan ambient glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFB2EBF2).copy(alpha = 0.45f), Color.Transparent),
                center = Offset(-10f, h + 10f),
                radius = 160f
            ),
            radius = 160f,
            center = Offset(-10f, h + 10f)
        )

        // Dotted grid texture (20px spacing)
        val step = 32f
        var x = 16f
        while (x < w) {
            var y = 16f
            while (y < h) {
                drawCircle(
                    color = Color(0xFF9C27B0).copy(alpha = 0.08f),
                    radius = 1.6f,
                    center = Offset(x, y)
                )
                y += step
            }
            x += step
        }
    }
}

private fun Modifier.drawBottomBorder(color: Color, heightDp: androidx.compose.ui.unit.Dp): Modifier =
    this.then(
        Modifier.background(
            Brush.verticalGradient(
                0.90f to Color.Transparent,
                1.0f to color
            )
        )
    )

/**
 * Geometric butterfly wing pattern inspired by Shinobu's haori.
 */
@Composable
fun HaoriBorderCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val segmentW = w / 8f

        for (i in 0 until 8) {
            val startX = i * segmentW
            val endX = startX + segmentW
            val path = Path().apply {
                moveTo(startX, 0f)
                cubicTo(startX + segmentW * 0.5f, h, endX - segmentW * 0.2f, h, endX, 0f)
            }
            drawPath(
                path = path,
                color = if (i % 2 == 0) ShinobuMint.copy(alpha = 0.6f) else ShinobuLilac.copy(alpha = 0.6f),
                style = Stroke(width = 2.5f, cap = StrokeCap.Round)
            )
        }
    }
}
