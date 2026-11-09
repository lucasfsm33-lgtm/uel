package com.example.model

data class AiScriptIdea(
    val title: String,
    val genre: String,
    val hook: String,
    val scenes: List<String>,
    val musicSuggestion: String,
    val estimatedDuration: String
)

data class BlueGptMessage(
    val id: String,
    val sender: String, // "BlueGPT" or "User"
    val content: String,
    val timestamp: String,
    val actionLabel: String? = null
)

object BlueGptAssistant {

    val presetScriptPrompts = listOf(
        "Generate a 30s viral Summer Travel Reel with upbeat vibes",
        "Create an aesthetic Coffee Routine morning reel",
        "Cyberpunk cinematic night walk reel script",
        "Quick fitness motivation reel with high energy cuts"
    )

    val viralHooks = listOf(
        "Stop scrolling if you need a vacation right now... ✈️",
        "3 secret beach spots nobody tells you about 🤫",
        "This one video edit will 10x your Instagram reel engagement 🔥",
        "POV: You woke up in paradise 🌊",
        "How to edit videos like a professional CapCut designer 🎬"
    )

    fun generateScriptFromPrompt(prompt: String): AiScriptIdea {
        val lower = prompt.lowercase()
        return when {
            lower.contains("coffee") || lower.contains("morning") -> AiScriptIdea(
                title = "Aesthetic Morning Routine",
                genre = "Lifestyle Vlog",
                hook = "My 5-minute morning ritual that changed everything ☕",
                scenes = listOf(
                    "00:00 - 00:03: Macro close-up of coffee beans grinding",
                    "00:03 - 00:08: Steam rising from fresh pour-over in sunlight",
                    "00:08 - 00:12: Cozy reading corner and first warm sip",
                    "00:12 - 00:15: Daily planner opening with subtle lo-fi beat drop"
                ),
                musicSuggestion = "Chill Lo-Fi Morning Glow (85 BPM)",
                estimatedDuration = "15 seconds"
            )
            lower.contains("cyber") || lower.contains("neon") || lower.contains("night") -> AiScriptIdea(
                title = "Tokyo Neon Midnight Walk",
                genre = "Cinematic Cityscape",
                hook = "Tokyo at 2 AM hits completely differently... 🌃",
                scenes = listOf(
                    "00:00 - 00:05: Rain reflections on asphalt with neon billboard glow",
                    "00:05 - 00:10: High-speed crosswalk hyperlapse with color grade",
                    "00:10 - 00:15: Underpass silhouette with synth wave bass drop",
                    "00:15 - 00:20: Overhead Shibuya crossing drone shot"
                ),
                musicSuggestion = "Retro Synthwave Drive (120 BPM)",
                estimatedDuration = "20 seconds"
            )
            lower.contains("fit") || lower.contains("gym") || lower.contains("workout") -> AiScriptIdea(
                title = "Unstoppable Energy Workout",
                genre = "Fitness Reel",
                hook = "Discipline over motivation. Every single day. ⚡",
                scenes = listOf(
                    "00:00 - 00:04: Chalk cloud clap and barbell load",
                    "00:04 - 00:10: High-speed lifting montage synced to kick drum",
                    "00:10 - 00:16: Heavy drop and intense focus close-up",
                    "00:16 - 00:22: Cool down walkout with bold motivational typography"
                ),
                musicSuggestion = "Phonk Aggressive Bass (135 BPM)",
                estimatedDuration = "22 seconds"
            )
            else -> AiScriptIdea(
                title = "Tropical Summer Paradise",
                genre = "Travel & Adventure",
                hook = "If you needed a sign to book that beach ticket, this is it 🌴",
                scenes = listOf(
                    "00:00 - 00:07: Drone pull-back over swaying coconut palms and turquoise bay",
                    "00:07 - 00:12: Crystal clear wave crashing gently on white sand",
                    "00:12 - 00:18: Warm golden sunset reflection dancing on the water",
                    "00:18 - 00:24: Relaxing in beach loungers with cocktail as text fades out"
                ),
                musicSuggestion = "Tropical House Chill - 'Beach Day' (118 BPM)",
                estimatedDuration = "24 seconds"
            )
        }
    }

    fun getBlueGptResponse(userQuery: String): String {
        val q = userQuery.lowercase()
        return when {
            q.contains("hook") || q.contains("viral") ->
                "💡 **BlueGPT Hook Formula**: Start with a question or high-contrast motion in the first 1.5 seconds. For your current project, try: *'POV: You finally booked the flight to paradise 🌴'*."
            q.contains("music") || q.contains("audio") || q.contains("beat") ->
                "🎵 **Audio Recommendation**: For travel reels, match transitions to the snare hits at seconds 00:07 and 00:12. I've synced your 'Beach day' waveform accordingly!"
            q.contains("filter") || q.contains("color") ->
                "🎨 **Color Grading Tip**: Apply the **'Vibrant Blue'** or **'Sunset Warm'** filter. This deepens the ocean cobalt and makes the sand look golden-warm like CapCut presets."
            q.contains("caption") || q.contains("subtitles") ->
                "💬 **AI Captions**: Animated captions increase watch time by 48%! Use the **'Hormozi'** or **'Neon Cyan'** style with 2-3 words per burst for highest retention."
            q.contains("export") || q.contains("quality") || q.contains("4k") ->
                "🚀 **Export Settings**: For Instagram Reels and TikTok, **1080p at 60fps** is ideal because higher bitrates don't get over-compressed by social media algorithms."
            else ->
                "✨ **BlueGPT Analysis**: Your timeline currently has 4 clips totaling 00:24 with 'Summer reel' text and 'Beach day' music. You can tap any tool below (Edit, Audio, Text, Filters, Effects) to customize your video like CapCut Designer!"
        }
    }
}
