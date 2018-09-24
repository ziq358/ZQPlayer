package com.zq.playerlib

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.view.Surface

class ZQPlayer {

    init {
        System.loadLibrary("player-lib")
    }

    external fun play(surface: Surface, surfaceFilter: Surface, path: String, type: Int): Int

    private var audioTrack: AudioTrack? = null
    //    这个方法  是C进行调用  通道数
    fun createTrack(sampleRateInHz: Int, nb_channals: Int) {
        val channaleConfig: Int//通道数
        if (nb_channals == 1) {
            channaleConfig = AudioFormat.CHANNEL_OUT_MONO
        } else if (nb_channals == 2) {
            channaleConfig = AudioFormat.CHANNEL_OUT_STEREO
        } else {
            channaleConfig = AudioFormat.CHANNEL_OUT_MONO
        }
        val buffersize = AudioTrack.getMinBufferSize(sampleRateInHz, channaleConfig, AudioFormat.ENCODING_PCM_16BIT)
        audioTrack = AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz, channaleConfig, AudioFormat.ENCODING_PCM_16BIT, buffersize, AudioTrack.MODE_STREAM)
        audioTrack!!.play()
    }

    //C传入音频数据
    fun playTrack(buffer: ByteArray, lenth: Int) {
        if (audioTrack != null && audioTrack!!.playState == AudioTrack.PLAYSTATE_PLAYING) {
            audioTrack!!.write(buffer, 0, lenth)
        }
    }
}