package com.zq.playerlib

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.AsyncTask
import android.util.Log
import android.view.Surface

class ZQPlayer {

    init {
        System.loadLibrary("player-lib")
    }


    interface StatusListener{
        fun onLoading(): Unit {

        }

        fun onError(msg: String): Unit {

        }

        fun onPrepareFinished(): Unit {

        }
    }



    private var listener: StatusListener? = null

    fun setStatusListener(listener: StatusListener?): Unit {
        this.listener = listener;
    }


    //native
    external fun prepare(path: String): Int
    external fun start(): Unit
    external fun play(surface: Surface, surfaceFilter: Surface, path: String, type: Int): Unit

    //jni  回调
    fun onLoading(): Unit {
        listener?.onLoading();
    }

    fun onError(msg: String): Unit {
        listener?.onError(msg);
    }

    fun onPrepareFinished(): Unit {
        listener?.onPrepareFinished();
    }

    // AudioTrack 播放 声音
    private var audioTrack: AudioTrack? = null

    fun initAudioTrack(sampleRateInHz: Int, nb_channals: Int) {
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

    fun sendDataToAudioTrack(buffer: ByteArray, lenth: Int) {
        if (audioTrack != null && audioTrack!!.playState == AudioTrack.PLAYSTATE_PLAYING) {
            audioTrack!!.write(buffer, 0, lenth)
        }
    }


}