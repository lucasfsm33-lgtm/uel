package com.example.data

import com.example.R
import com.example.model.AudioTrack
import com.example.model.CaptionItem
import com.example.model.StickerItem
import com.example.model.TextOverlay
import com.example.model.VideoClip
import com.example.model.VideoFilter
import com.example.model.VideoProject

object SampleProjects {

    // Realistic audio waveform amplitudes for "Beach day"
    val defaultWaveform = listOf(
        0.25f, 0.4f, 0.65f, 0.85f, 0.5f, 0.7f, 0.95f, 0.6f, 0.35f, 0.55f,
        0.8f, 0.9f, 0.45f, 0.6f, 0.75f, 1.0f, 0.85f, 0.65f, 0.4f, 0.6f,
        0.8f, 0.7f, 0.5f, 0.75f, 0.9f, 0.6f, 0.4f, 0.65f, 0.85f, 0.95f,
        0.7f, 0.5f, 0.8f, 0.9f, 0.6f, 0.45f, 0.7f, 0.85f, 0.5f, 0.3f
    )

    val summerReelProject = VideoProject(
        id = "summer_reel",
        title = "Summer reel",
        clips = listOf(
            VideoClip(
                id = "clip_1",
                name = "Tropical Palm",
                thumbnailRes = R.drawable.thumb_palm,
                durationMs = 7000L,
                filter = VideoFilter.VIBRANT_BLUE
            ),
            VideoClip(
                id = "clip_2",
                name = "Ocean Waves",
                thumbnailRes = R.drawable.thumb_ocean,
                durationMs = 5000L,
                filter = VideoFilter.AQUA_BEACH
            ),
            VideoClip(
                id = "clip_3",
                name = "Golden Sunset",
                thumbnailRes = R.drawable.thumb_sunset,
                durationMs = 6000L,
                filter = VideoFilter.SUNSET_WARM
            ),
            VideoClip(
                id = "clip_4",
                name = "Beach Loungers",
                thumbnailRes = R.drawable.thumb_loungers,
                durationMs = 6000L,
                filter = VideoFilter.NORMAL
            )
        ),
        textOverlays = listOf(
            TextOverlay(
                id = "txt_1",
                text = "Summer reel",
                startTimeMs = 0L,
                durationMs = 18000L,
                colorHex = 0xFFFFD54F
            )
        ),
        audioTrack = AudioTrack(
            id = "audio_1",
            name = "Beach day",
            durationMs = 24000L,
            waveformPoints = defaultWaveform
        ),
        stickers = listOf(
            StickerItem(
                id = "stk_1",
                symbol = "🌴",
                startTimeMs = 2000L,
                durationMs = 8000L,
                xOffsetDp = 100f,
                yOffsetDp = -80f
            )
        ),
        captions = listOf(
            CaptionItem("cap_1", "Escape to paradise 🌊", 0L, 4000L),
            CaptionItem("cap_2", "Sun, sand & endless summer", 4000L, 10000L),
            CaptionItem("cap_3", "Golden hour magic ✨", 10000L, 18000L),
            CaptionItem("cap_4", "Making memories by the sea 🏖️", 18000L, 24000L)
        )
    )

    val cyberpunkProject = VideoProject(
        id = "cyber_night",
        title = "Cyberpunk Night Walk",
        clips = listOf(
            VideoClip(
                id = "c_clip_1",
                name = "Neon Street",
                thumbnailRes = R.drawable.thumb_sunset,
                durationMs = 6000L,
                filter = VideoFilter.CYBERPUNK
            ),
            VideoClip(
                id = "c_clip_2",
                name = "Tokyo Rain",
                thumbnailRes = R.drawable.thumb_ocean,
                durationMs = 8000L,
                filter = VideoFilter.VIBRANT_BLUE
            ),
            VideoClip(
                id = "c_clip_3",
                name = "Night Skyline",
                thumbnailRes = R.drawable.thumb_palm,
                durationMs = 6000L,
                filter = VideoFilter.FILM_NOIR
            )
        ),
        textOverlays = listOf(
            TextOverlay(
                id = "c_txt_1",
                text = "TOKYO 2049",
                startTimeMs = 0L,
                durationMs = 12000L,
                colorHex = 0xFF00E5FF
            )
        ),
        audioTrack = AudioTrack(
            id = "c_audio_1",
            name = "Synthwave Pulse",
            durationMs = 20000L,
            waveformPoints = defaultWaveform.reversed()
        ),
        captions = listOf(
            CaptionItem("c_cap_1", "Lost in the neon glow", 0L, 6000L),
            CaptionItem("c_cap_2", "Midnight vibes in Shinjuku", 6000L, 14000L)
        )
    )

    val coffeeVlogProject = VideoProject(
        id = "coffee_vlog",
        title = "Morning Coffee Vlog",
        clips = listOf(
            VideoClip(
                id = "v_clip_1",
                name = "Grinding Beans",
                thumbnailRes = R.drawable.thumb_loungers,
                durationMs = 4000L,
                filter = VideoFilter.GOLDEN_HOUR
            ),
            VideoClip(
                id = "v_clip_2",
                name = "Pour Over",
                thumbnailRes = R.drawable.thumb_palm,
                durationMs = 5000L,
                filter = VideoFilter.SUNSET_WARM
            ),
            VideoClip(
                id = "v_clip_3",
                name = "First Sip",
                thumbnailRes = R.drawable.thumb_ocean,
                durationMs = 5000L,
                filter = VideoFilter.NORMAL
            )
        ),
        textOverlays = listOf(
            TextOverlay(
                id = "v_txt_1",
                text = "Sunday Routine ☕",
                startTimeMs = 0L,
                durationMs = 10000L,
                colorHex = 0xFFFFE082
            )
        ),
        audioTrack = AudioTrack(
            id = "v_audio_1",
            name = "Lo-Fi Morning",
            durationMs = 14000L,
            waveformPoints = defaultWaveform
        )
    )

    val allProjects = listOf(summerReelProject, cyberpunkProject, coffeeVlogProject)
}
