package com.example.sound

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

object ShinobuSoundPlayer {
    private val scope = CoroutineScope(Dispatchers.Default)

    fun playCorrectChime() {
        scope.launch {
            // Ascending cheerful sparkle: E5 (659Hz) -> G#5 (830Hz) -> B5 (987Hz) -> E6 (1318Hz)
            playTones(listOf(659.25, 830.61, 987.77, 1318.51), durationMs = 90)
        }
    }

    fun playRewardFanfare() {
        scope.launch {
            // Victory chord progression: C5 -> E5 -> G5 -> C6 -> E6
            playTones(listOf(523.25, 659.25, 783.99, 1046.50, 1318.51), durationMs = 120)
        }
    }

    fun playCardFlip() {
        scope.launch {
            // Gentle swish pop
            playTones(listOf(440.0, 554.37), durationMs = 50)
        }
    }

    private fun playTones(freqs: List<Double>, durationMs: Int) {
        try {
            val sampleRate = 44100
            val totalSamples = (sampleRate * (durationMs / 1000.0) * freqs.size).toInt()
            val samplesPerTone = (sampleRate * (durationMs / 1000.0)).toInt()
            val buffer = ShortArray(totalSamples)

            var globalIdx = 0
            for (freq in freqs) {
                for (i in 0 until samplesPerTone) {
                    val angle = 2.0 * Math.PI * i / (sampleRate / freq)
                    // Apply smooth envelope to avoid clicking
                    val envelope = if (i < samplesPerTone * 0.15) {
                        i / (samplesPerTone * 0.15)
                    } else {
                        1.0 - (i - samplesPerTone * 0.15) / (samplesPerTone * 0.85)
                    }
                    val sampleValue = (sin(angle) * Short.MAX_VALUE * 0.35 * envelope).toInt().toShort()
                    if (globalIdx < totalSamples) {
                        buffer[globalIdx++] = sampleValue
                    }
                }
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            Thread.sleep(buffer.size * 1000L / sampleRate + 50)
            audioTrack.release()
        } catch (_: Exception) {
            // Audio error non-blocking fallback
        }
    }
}
