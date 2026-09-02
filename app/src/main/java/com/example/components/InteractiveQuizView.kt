package com.example.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.ShinobuVioletPrimary
import com.example.ui.theme.TextDark

/**
 * Geometric Balance 4-Choice Quick Quiz Card with tactile 3D buttons.
 */
@Composable
fun InteractiveQuizCard(
    problem: MultiplicationProblem,
    options: List<Int>,
    onSelectOption: (Int) -> Unit,
    selectedOption: Int?,
    isAnswerChecked: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(36.dp)),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(3.5.dp, Color(0xFF9C27B0), RoundedCornerShape(36.dp))
                .padding(20.dp)
        ) {
            // Geometric dot background
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height

                // Ambient glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFE1BEE7).copy(alpha = 0.35f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(w, 0f),
                        radius = 140f
                    ),
                    radius = 140f,
                    center = androidx.compose.ui.geometry.Offset(w, 0f)
                )

                val step = 32f
                var x = 16f
                while (x < w) {
                    var y = 16f
                    while (y < h) {
                        drawCircle(
                            color = Color(0xFF9C27B0).copy(alpha = 0.07f),
                            radius = 1.5f,
                            center = androidx.compose.ui.geometry.Offset(x, y)
                        )
                        y += step
                    }
                    x += step
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShinobuButterfly(
                        sizeDp = 26.dp,
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
                        sizeDp = 26.dp,
                        wingColor = Color(0xFFE1BEE7),
                        tipColor = Color(0xFF9C27B0)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Math Question Display in Geometric Box
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp)),
                    color = Color(0xFFF8F2FF),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE1BEE7))
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${problem.multiplier}",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF4A148C)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "×",
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF9C27B0)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "${problem.multiplicand}",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF4A148C)
                            )
                        }

                        Text(
                            text = "Tap the correct butterfly answer:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9C27B0)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 4 Tactile Option Buttons in a 2x2 grid
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (rowIdx in 0 until 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            for (colIdx in 0 until 2) {
                                val optionIdx = rowIdx * 2 + colIdx
                                if (optionIdx < options.size) {
                                    val optionVal = options[optionIdx]
                                    val isSelected = selectedOption == optionVal
                                    val isCorrect = optionVal == problem.answer

                                    val btnBg = when {
                                        isAnswerChecked && isCorrect -> CorrectGreen
                                        isAnswerChecked && isSelected && !isCorrect -> PracticeOrange
                                        isSelected -> Color(0xFF9C27B0)
                                        else -> Color.White
                                    }

                                    val bottomBorderColor = when {
                                        isAnswerChecked && isCorrect -> Color(0xFF047857)
                                        isAnswerChecked && isSelected && !isCorrect -> Color(0xFFB45309)
                                        isSelected -> Color(0xFF7B1FA2)
                                        else -> Color(0xFFE1BEE7)
                                    }

                                    val textColor = when {
                                        isAnswerChecked && (isCorrect || isSelected) -> Color.White
                                        isSelected -> Color.White
                                        else -> Color(0xFF4A148C)
                                    }

                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(58.dp)
                                            .shadow(if (isSelected) 4.dp else 2.dp, RoundedCornerShape(16.dp))
                                            .clip(RoundedCornerShape(16.dp))
                                            .clickable(enabled = !isAnswerChecked) {
                                                onSelectOption(optionVal)
                                            }
                                            .testTag("quiz_option_$optionVal"),
                                        color = btnBg,
                                        shape = RoundedCornerShape(16.dp),
                                        border = androidx.compose.foundation.BorderStroke(
                                            width = if (isSelected) 2.dp else 1.5.dp,
                                            color = if (isSelected) Color(0xFF7B1FA2) else Color(0xFFE1BEE7)
                                        )
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Brush.verticalGradient(
                                                        0.88f to Color.Transparent,
                                                        1.0f to bottomBorderColor
                                                    )
                                                )
                                                .padding(horizontal = 12.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = if (isAnswerChecked && isCorrect) "✨" else "🦋",
                                                    fontSize = 16.sp
                                                )
                                                Text(
                                                    text = "$optionVal",
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = textColor
                                                )
                                                if (isAnswerChecked && isCorrect) {
                                                    Icon(
                                                        imageVector = Icons.Default.CheckCircle,
                                                        contentDescription = "Correct",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                } else {
                                                    Spacer(modifier = Modifier.width(16.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Keypad entry card for typing numbers directly with Geometric Balance styling.
 */
@Composable
fun InteractiveKeypadCard(
    problem: MultiplicationProblem,
    inputBuffer: String,
    onDigitPress: (String) -> Unit,
    onDeletePress: () -> Unit,
    onSubmitPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(36.dp)),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(3.5.dp, Color(0xFF9C27B0), RoundedCornerShape(36.dp))
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${problem.multiplier} × ${problem.multiplicand} = ",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF4A148C)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Input display box
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp)
                    .background(Color(0xFFF8F2FF), RoundedCornerShape(16.dp))
                    .border(2.dp, Color(0xFF9C27B0), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (inputBuffer.isEmpty()) "?" else inputBuffer,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Black,
                    color = if (inputBuffer.isEmpty()) Color(0xFFE1BEE7) else Color(0xFF4A148C)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Number Pad Grid (1-9, C, 0, Submit) with tactile 3D buttons
            val digits = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("⌫", "0", "✓")
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                for (row in digits) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (key in row) {
                            val isSubmit = key == "✓"
                            val isDelete = key == "⌫"
                            val btnBg = when {
                                isSubmit -> Color(0xFF9C27B0)
                                isDelete -> Color(0xFFF3E5F5)
                                else -> Color.White
                            }
                            val bottomBorderColor = when {
                                isSubmit -> Color(0xFF7B1FA2)
                                isDelete -> Color(0xFFE1BEE7)
                                else -> Color(0xFFE1BEE7)
                            }
                            val textColor = when {
                                isSubmit -> Color.White
                                isDelete -> Color(0xFF4A148C)
                                else -> Color(0xFF4A148C)
                            }

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .shadow(2.dp, RoundedCornerShape(14.dp))
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable {
                                        when (key) {
                                            "⌫" -> onDeletePress()
                                            "✓" -> onSubmitPress()
                                            else -> onDigitPress(key)
                                        }
                                    }
                                    .testTag("keypad_btn_$key"),
                                shape = RoundedCornerShape(14.dp),
                                color = btnBg,
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE1BEE7))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                0.86f to Color.Transparent,
                                                1.0f to bottomBorderColor
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = key,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = textColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
