package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.SampleProjects
import com.example.model.AiScriptIdea
import com.example.model.BlueGptAssistant
import com.example.model.BlueGptMessage
import com.example.model.VideoProject
import com.example.ui.theme.BlueAccentCyan
import com.example.ui.theme.BlueBackground
import com.example.ui.theme.BluePrimaryDark
import com.example.ui.theme.BlueSurface
import com.example.ui.theme.BlueSurfaceLight
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.TextOnGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.EditorViewModel

@Composable
fun BlueGptHubScreen(
    viewModel: EditorViewModel,
    onBackToEditor: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBackToEditor() }

    val uiState = viewModel.uiState.value
    var promptInput by remember { mutableStateOf("") }
    var chatQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BlueBackground)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BluePrimaryDark)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackToEditor,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("btn_back_to_editor")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Editor",
                    tint = TextPrimary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // BlueGPT App Icon Logo
            Image(
                painter = painterResource(id = R.drawable.ic_bluegpt_logo),
                contentDescription = "BlueGPT Logo",
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "BlueGPT",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0x3300E5FF))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "AI DESIGNER",
                            color = BlueAccentCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = "AI Video Director & CapCut Assistant",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(6.dp)) }

            // 1. AI Script to Video Generator Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BlueSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3300E5FF)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = BlueAccentCyan)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Prompt to Video Script",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Describe your idea and BlueGPT will plan scenes, hooks, music & duration.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )

                        // Quick Suggestion Chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("🌴 Summer Reel", "☕ Coffee Vlog", "⚡ Gym Motivation").forEach { chip ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(BlueSurfaceLight)
                                        .clickable {
                                            promptInput = when (chip) {
                                                "🌴 Summer Reel" -> "Generate a 30s viral Summer Travel Reel with upbeat vibes"
                                                "☕ Coffee Vlog" -> "Aesthetic morning coffee routine vlog"
                                                else -> "High energy fitness motivation workout reel"
                                            }
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(chip, color = Color.White, fontSize = 11.sp)
                                }
                            }
                        }

                        OutlinedTextField(
                            value = promptInput,
                            onValueChange = { promptInput = it },
                            placeholder = { Text("e.g., 30s summer beach travel reel with sunset...", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = BlueAccentCyan,
                                unfocusedBorderColor = BlueSurfaceLight
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (promptInput.isNotBlank()) {
                                    viewModel.generateAiScript(promptInput)
                                } else {
                                    viewModel.generateAiScript("Summer Travel Paradise")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldYellow, contentColor = TextOnGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_generate_script")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TextOnGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Generate AI Video Script", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        // Generated Script Preview if present
                        uiState.aiScriptGenerated?.let { script ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(BluePrimaryDark)
                                    .border(1.dp, GoldYellow, RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(script.title, color = GoldYellow, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("🪝 Hook: ${script.hook}", color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                                    Text("🎵 Music: ${script.musicSuggestion}", color = BlueAccentCyan, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    script.scenes.forEach { scene ->
                                        Text("• $scene", color = TextSecondary, fontSize = 11.sp)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            viewModel.updateMainText(script.title)
                                            onBackToEditor()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = BlueAccentCyan, contentColor = Color.Black),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text("Apply to Editor", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 2. Project Library Switcher
            item {
                Text(
                    text = "📁 My Video Projects",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(SampleProjects.allProjects) { proj ->
                val isCurrent = proj.id == uiState.currentProject.id
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isCurrent) BlueSurfaceLight else BlueSurface),
                    border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.5.dp, GoldYellow) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.loadProject(proj)
                            onBackToEditor()
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BluePrimaryDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Movie, contentDescription = null, tint = if (isCurrent) GoldYellow else TextSecondary)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = proj.title,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${proj.clips.size} clips • ${formatTimestamp(proj.totalDurationMs)}",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        if (isCurrent) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(GoldYellow)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("ACTIVE", color = TextOnGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 3. BlueGPT Interactive Chat Assistant
            item {
                Text(
                    text = "💬 BlueGPT Video Assistant Chat",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                )
            }

            items(uiState.blueGptMessages) { msg ->
                val isAi = msg.sender == "BlueGPT"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = if (isAi) Arrangement.Start else Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isAi) BlueSurface else BlueAccentCyan)
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isAi) {
                                    Icon(Icons.Default.SmartToy, contentDescription = null, tint = BlueAccentCyan, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = msg.sender,
                                    color = if (isAi) BlueAccentCyan else Color.Black,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = msg.content,
                                color = if (isAi) Color.White else Color.Black,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Chat input row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = chatQuery,
                        onValueChange = { chatQuery = it },
                        placeholder = { Text("Ask BlueGPT (e.g. viral hooks, music sync)...", color = TextSecondary, fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = BlueAccentCyan,
                            unfocusedBorderColor = BlueSurfaceLight
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (chatQuery.isNotBlank()) {
                                viewModel.sendBlueGptQuery(chatQuery)
                                chatQuery = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(BlueAccentCyan)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.Black
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}
