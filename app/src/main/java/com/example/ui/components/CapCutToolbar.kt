package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BlueAccentCyan
import com.example.ui.theme.BluePrimaryDark
import com.example.ui.theme.TextPrimary
import com.example.viewmodel.ActiveTool

data class ToolItem(
    val tool: ActiveTool,
    val label: String,
    val icon: ImageVector,
    val testTag: String
)

@Composable
fun CapCutToolbar(
    activeTool: ActiveTool,
    onToolClick: (ActiveTool) -> Unit,
    modifier: Modifier = Modifier
) {
    val tools = listOf(
        ToolItem(ActiveTool.EDIT, "Edit", Icons.Default.ContentCut, "tool_edit"),
        ToolItem(ActiveTool.AUDIO, "Audio", Icons.Default.MusicNote, "tool_audio"),
        ToolItem(ActiveTool.TEXT, "Text", Icons.Default.Title, "tool_text"),
        ToolItem(ActiveTool.STICKERS, "Stickers", Icons.Default.Mood, "tool_stickers"),
        ToolItem(ActiveTool.EFFECTS, "Effects", Icons.Default.Star, "tool_effects"),
        ToolItem(ActiveTool.FILTERS, "Filters", Icons.Default.ColorLens, "tool_filters"),
        ToolItem(ActiveTool.CAPTIONS, "Captions", Icons.Default.ClosedCaption, "tool_captions")
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(BluePrimaryDark)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tools.forEach { item ->
            val isSelected = activeTool == item.tool
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) Color(0x3300E5FF) else Color.Transparent)
                    .clickable { onToolClick(item.tool) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag(item.testTag),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (isSelected) BlueAccentCyan else TextPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.label,
                    color = if (isSelected) BlueAccentCyan else TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
