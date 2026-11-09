package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BlueAccentCyan
import com.example.ui.theme.BlueBackground
import com.example.ui.theme.BluePrimaryDark
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.TextOnGold
import com.example.ui.theme.TextPrimary

@Composable
fun TopEditorBar(
    selectedResolution: String,
    onResolutionChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    onExportClick: () -> Unit,
    onBlueGptClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedDropdown by remember { mutableStateOf(false) }
    val resolutions = listOf("720p", "1080p", "2K", "4K")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(BlueBackground)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Close / Exit Button
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .testTag("btn_close_editor")
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close or Menu",
                tint = TextPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        // Center: Resolution Dropdown Pill & BlueGPT AI trigger
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Resolution dropdown pill (Mockup: "1080p v")
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x33001E50))
                        .clickable { expandedDropdown = true }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("pill_resolution_selector"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedResolution,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Resolution",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                DropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false },
                    modifier = Modifier.background(BluePrimaryDark)
                ) {
                    resolutions.forEach { res ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = res,
                                    color = if (res == selectedResolution) BlueAccentCyan else TextPrimary,
                                    fontWeight = if (res == selectedResolution) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                onResolutionChange(res)
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }

            // BlueGPT AI Quick Trigger Button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x2600E5FF))
                    .clickable(onClick = onBlueGptClick)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("btn_bluegpt_top"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "BlueGPT Assistant",
                    tint = BlueAccentCyan,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "BlueGPT",
                    color = BlueAccentCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Right: Golden Yellow Export Button (Mockup: "Export")
        Button(
            onClick = onExportClick,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldYellow,
                contentColor = TextOnGold
            ),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 6.dp),
            modifier = Modifier
                .height(36.dp)
                .testTag("btn_export")
        ) {
            Text(
                text = "Export",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
