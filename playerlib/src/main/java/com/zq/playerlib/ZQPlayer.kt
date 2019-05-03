package com.zq.playerlib

import android.media.*
import android.util.Log
import android.view.Surface
import com.zq.playerlib.service.StatusListener
import java.nio.ByteBuffer

class ZQPlayer {

    init {
        System.loadLibrary("player-lib")
    }

    private var audioTrack: AudioTrack? = null
    private var surface: Surface? = null
    private var mediaCodec:MediaCodec? = null


    external fun playdemo(surface: Surface, surfaceFilter: Surface, path: String, type: Int): Unit
    //native
    external fun init(path: String)
    fun setSurfaceTarget(surface: Surface?): Unit {
        this.surface = surface
        setSurface(surface!!)
    }
    external fun setSurface(surface: Surface)
    external fun play(): Unit
    external fun pause(): Unit
    external fun stop(): Unit
    external fun isPlaying(): Boolean

    //jni  回调
    fun onLoading(): Unit {
        listener?.onLoading()
    }

    fun onPrepareFinished(): Unit {
        listener?.onPrepareFinished()
    }

    fun onPlaying(): Unit {
        listener?.onPlaying()
    }

    fun onPause(): Unit {
        listener?.onPause()
    }

    fun onStop(): Unit {
        listener?.onStop()
    }

    fun onError(msg: String): Unit {
        Log.e("ziq", msg)
        listener?.onError(msg)
    }

    private var listener: StatusListener? = null
    fun setStatusListener(listener: StatusListener?): Unit {
        this.listener = listener
    }





    // AudioTrack 播放 声音
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

    //视频硬解码
    fun initMediaCodec(mimetype: String, width: Int, height: Int, csd0: ByteArray, csd1: ByteArray) {
        if (surface != null) {
            try {
                Log.e("ziq", "initMediaCodec " + mimetype)
                var mediaFormat:MediaFormat = MediaFormat.createVideoFormat(mimetype, width, height)
                mediaFormat.setInteger(MediaFormat.KEY_WIDTH, width)
                mediaFormat.setInteger(MediaFormat.KEY_HEIGHT, height)
                mediaFormat.setLong(MediaFormat.KEY_MAX_INPUT_SIZE, (width * height).toLong())
                mediaFormat.setByteBuffer("csd-0", ByteBuffer.wrap(csd0))
                mediaFormat.setByteBuffer("csd-1", ByteBuffer.wrap(csd1))
                mediaCodec = MediaCodec.createDecoderByType(mimetype)
                if (surface != null) {
                    mediaCodec?.configure(mediaFormat, surface, null, 0)
                    mediaCodec?.start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendToMediaCodec(bytes: ByteArray?, size: Int, pts: Int) {
        if (bytes != null && mediaCodec != null) {
            try {
                val videoBufferInfo = MediaCodec.BufferInfo()
                val inputBufferIndex:Int? = mediaCodec?.dequeueInputBuffer(10)
                if (inputBufferIndex!! >= 0) {
                    val byteBuffer = mediaCodec?.getInputBuffers()!![inputBufferIndex]
                    byteBuffer.clear()
                    byteBuffer.put(bytes)
                    mediaCodec?.queueInputBuffer(inputBufferIndex, 0, size, pts.toLong(), 0)
                }

                var index = mediaCodec?.dequeueOutputBuffer(videoBufferInfo, 10)
                while (index!! >= 0) {
                    mediaCodec?.releaseOutputBuffer(index, true)
                    index = mediaCodec?.dequeueOutputBuffer(videoBufferInfo, 10)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}