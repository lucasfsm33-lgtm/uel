package com.example.model

import androidx.annotation.DrawableRes
import com.example.R

enum class VideoFilter(val label: String, val description: String) {
    NORMAL("Normal", "Original natural colors"),
    VIBRANT_BLUE("Vibrant Blue", "Boosts ocean blues & sky contrast"),
    SUNSET_WARM("Sunset Warm", "Golden highlights and warm tones"),
    AQUA_BEACH("Aqua Beach", "Tropical turquoise water grade"),
    GOLDEN_HOUR("Golden Hour", "Soft amber cinematic sun rays"),
    FILM_NOIR("Film Noir", "High contrast black and white"),
    CYBERPUNK("Cyber Neon", "Blue and magenta neon aesthetic")
}

enum class VideoEffect(val label: String, val icon: String) {
    NONE("None", "✨"),
    CINEMATIC_GLOW("Glow", "🌟"),
    LENS_FLARE("Lens Flare", "☀️"),
    VHS_RETRO("VHS 90s", "📼"),
    DREAMY_BLUR("Dreamy", "💫"),
    ZOOM_BURST("Zoom In", "🔍"),
    FLASH_BEAT("Beat Flash", "⚡")
}

enum class CaptionStyle(val label: String) {
    HORMOZI("Bold Hormozi"),
    NEON_BLUE("Neon Cyan"),
    MINIMAL_WHITE("Minimal White"),
    YELLOW_POP("Yellow Pop")
}

data class VideoClip(
    val id: String,
    val name: String,
    @DrawableRes val thumbnailRes: Int,
    val durationMs: Long,
    val speed: Float = 1.0f,
    val volume: Float = 1.0f,
    val filter: VideoFilter = VideoFilter.NORMAL,
    val effect: VideoEffect = VideoEffect.NONE
)

data class TextOverlay(
    val id: String,
    val text: String,
    val startTimeMs: Long,
    val durationMs: Long,
    val fontStyle: String = "Bold Sans",
    val colorHex: Long = 0xFFFFD54F,
    val animation: String = "Pop In"
)

data class AudioTrack(
    val id: String,
    val name: String,
    val durationMs: Long,
    val volume: Float = 0.85f,
    val isMuted: Boolean = false,
    val waveformPoints: List<Float> = emptyList()
)

data class StickerItem(
    val id: String,
    val symbol: String,
    val startTimeMs: Long,
    val durationMs: Long,
    val xOffsetDp: Float = 0f,
    val yOffsetDp: Float = 0f
)

data class CaptionItem(
    val id: String,
    val text: String,
    val startTimeMs: Long,
    val endTimeMs: Long
)

data class VideoProject(
    val id: String,
    val title: String,
    val clips: List<VideoClip>,
    val textOverlays: List<TextOverlay>,
    val audioTrack: AudioTrack?,
    val stickers: List<StickerItem> = emptyList(),
    val captions: List<CaptionItem> = emptyList()
) {
    val totalDurationMs: Long
        get() = clips.sumOf { (it.durationMs / it.speed).toLong() }
}
