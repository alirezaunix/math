package com.example.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ShinobuBorder
import com.example.ui.theme.ShinobuDeepPurple
import com.example.ui.theme.ShinobuLilac
import com.example.ui.theme.ShinobuMint
import com.example.ui.theme.ShinobuPinkAccent
import com.example.ui.theme.ShinobuTealDark
import com.example.ui.theme.ShinobuVioletPrimary
import com.example.ui.theme.TextDark

/**
 * Animated fluttering Shinobu butterfly in pure Compose Canvas.
 */
@Composable
fun ShinobuButterfly(
    modifier: Modifier = Modifier,
    sizeDp: Dp = 36.dp,
    wingColor: Color = ShinobuLilac,
    tipColor: Color = ShinobuMint,
    isFluttering: Boolean = true,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "butterfly_flutter")
    val wingScaleX by if (isFluttering) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 0.25f,
            animationSpec = infiniteRepeatable(
                animation = tween(280, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "wing_flap"
        )
    } else {
        rememberInfiniteTransition(label = "static").animateFloat(
            initialValue = 1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(1000)),
            label = "static_wing"
        )
    }

    Canvas(
        modifier = modifier
            .size(sizeDp)
            .graphicsLayer {
                scaleX = wingScaleX
            }
    ) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        // Top Left Wing
        val pathTopLeft = Path().apply {
            moveTo(cx, cy)
            cubicTo(cx - w * 0.45f, cy - h * 0.45f, cx - w * 0.5f, cy - h * 0.05f, cx, cy)
            close()
        }
        drawPath(
            path = pathTopLeft,
            brush = Brush.radialGradient(
                colors = listOf(Color.White, wingColor, tipColor, ShinobuTealDark),
                center = Offset(cx - w * 0.25f, cy - h * 0.25f),
                radius = w * 0.45f
            )
        )
        drawPath(
            path = pathTopLeft,
            color = Color.White.copy(alpha = 0.8f),
            style = Stroke(width = 1.2f)
        )

        // Top Right Wing
        val pathTopRight = Path().apply {
            moveTo(cx, cy)
            cubicTo(cx + w * 0.45f, cy - h * 0.45f, cx + w * 0.5f, cy - h * 0.05f, cx, cy)
            close()
        }
        drawPath(
            path = pathTopRight,
            brush = Brush.radialGradient(
                colors = listOf(Color.White, wingColor, tipColor, ShinobuTealDark),
                center = Offset(cx + w * 0.25f, cy - h * 0.25f),
                radius = w * 0.45f
            )
        )
        drawPath(
            path = pathTopRight,
            color = Color.White.copy(alpha = 0.8f),
            style = Stroke(width = 1.2f)
        )

        // Bottom Left Wing
        val pathBotLeft = Path().apply {
            moveTo(cx, cy)
            cubicTo(cx - w * 0.35f, cy + h * 0.1f, cx - w * 0.3f, cy + h * 0.45f, cx, cy + h * 0.2f)
            close()
        }
        drawPath(
            path = pathBotLeft,
            brush = Brush.verticalGradient(
                colors = listOf(wingColor, tipColor),
                startY = cy,
                endY = cy + h * 0.45f
            )
        )

        // Bottom Right Wing
        val pathBotRight = Path().apply {
            moveTo(cx, cy)
            cubicTo(cx + w * 0.35f, cy + h * 0.1f, cx + w * 0.3f, cy + h * 0.45f, cx, cy + h * 0.2f)
            close()
        }
        drawPath(
            path = pathBotRight,
            brush = Brush.verticalGradient(
                colors = listOf(wingColor, tipColor),
                startY = cy,
                endY = cy + h * 0.45f
            )
        )

        // Body
        drawLine(
            color = Color(0xFF2E1065),
            start = Offset(cx, cy - h * 0.25f),
            end = Offset(cx, cy + h * 0.25f),
            strokeWidth = 2.5f,
            cap = StrokeCap.Round
        )

        // Antennae
        drawLine(
            color = ShinobuVioletPrimary,
            start = Offset(cx, cy - h * 0.25f),
            end = Offset(cx - w * 0.15f, cy - h * 0.4f),
            strokeWidth = 1.5f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = ShinobuVioletPrimary,
            start = Offset(cx, cy - h * 0.25f),
            end = Offset(cx + w * 0.15f, cy - h * 0.4f),
            strokeWidth = 1.5f,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Visual grid to explain multiplication intuitively (e.g., 3 x 4 shown as 3 rows of 4 butterflies).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ButterflyMultiplicationVisualizer(
    multiplier: Int,
    multiplicand: Int,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, ShinobuBorder, RoundedCornerShape(16.dp)),
        color = Color(0xFFFAF5FF),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ShinobuButterfly(sizeDp = 20.dp, wingColor = ShinobuLilac)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Butterfly Counter Grid",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = ShinobuVioletPrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                ShinobuButterfly(sizeDp = 20.dp, wingColor = ShinobuPinkAccent)
            }

            Text(
                text = "$multiplier rows of $multiplicand butterflies = ${multiplier * multiplicand}",
                fontSize = 12.sp,
                color = TextDark.copy(alpha = 0.75f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Cap display if dimensions are very large to fit comfortably
            val displayRows = multiplier.coerceAtMost(6)
            val displayCols = multiplicand.coerceAtMost(8)

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (r in 1..displayRows) {
                    Row(
                        modifier = Modifier
                            .background(
                                color = if (r % 2 == 0) ShinobuLilac.copy(alpha = 0.15f) else ShinobuMint.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "R$r:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ShinobuDeepPurple
                        )
                        for (c in 1..displayCols) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .background(Color.White, CircleShape)
                                    .border(0.5.dp, ShinobuLilac, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🦋",
                                    fontSize = 11.sp
                                )
                            }
                        }
                        if (multiplicand > displayCols) {
                            Text(text = "+${multiplicand - displayCols}", fontSize = 10.sp, color = TextDark)
                        }
                    }
                }
                if (multiplier > displayRows) {
                    Text(
                        text = "... and ${multiplier - displayRows} more rows",
                        fontSize = 11.sp,
                        color = ShinobuVioletPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
