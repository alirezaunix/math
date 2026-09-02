package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TableProgress
import com.example.ui.theme.ShinobuBorder
import com.example.ui.theme.ShinobuDeepPurple
import com.example.ui.theme.ShinobuGoldReward
import com.example.ui.theme.ShinobuLavenderLight
import com.example.ui.theme.ShinobuLilac
import com.example.ui.theme.ShinobuMint
import com.example.ui.theme.ShinobuVioletPrimary
import com.example.ui.theme.TextDark

/**
 * Scrollable bar for choosing multiplication table (1 to 12 + All Mixed).
 */
@Composable
fun TableSelectorBar(
    selectedTable: Int, // 0 = Mixed All, 1..12 = Specific Table
    tableProgressMap: Map<Int, TableProgress>,
    onSelectTable: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Select Multiplication Table:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ShinobuDeepPurple
            )
            Text(
                text = if (selectedTable == 0) "🌟 All Mixed (Hashira Trial)" else "🦋 Table of $selectedTable",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = ShinobuVioletPrimary
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Mixed All Option
            TablePill(
                label = "All ★",
                subLabel = "Mixed",
                isSelected = selectedTable == 0,
                stars = 3,
                onClick = { onSelectTable(0) }
            )

            // Tables 1 to 12
            for (t in 1..12) {
                val progress = tableProgressMap[t]
                val stars = progress?.stars ?: 0
                TablePill(
                    label = "$t ×",
                    subLabel = if (stars > 0) "★".repeat(stars) else "Table $t",
                    isSelected = selectedTable == t,
                    stars = stars,
                    onClick = { onSelectTable(t) }
                )
            }
        }
    }
}

@Composable
private fun TablePill(
    label: String,
    subLabel: String,
    isSelected: Boolean,
    stars: Int,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .shadow(if (isSelected) 4.dp else 1.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("table_pill_$label"),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color(0xFF9C27B0) else Color.White,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.5.dp,
            color = if (isSelected) Color(0xFF7B1FA2) else Color(0xFFE1BEE7)
        )
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = label,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSelected) Color.White else Color(0xFF4A148C)
                )
                Text(
                    text = subLabel,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color(0xFFE1BEE7) else if (stars > 0) ShinobuGoldReward else Color(0xFF9C27B0)
                )
            }
        }
    }
}
