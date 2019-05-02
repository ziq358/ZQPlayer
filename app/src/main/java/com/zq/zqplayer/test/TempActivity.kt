package com.zq.zqplayer.test

import android.content.Context
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseActivity
import com.zq.playerlib.StatusListener
import com.zq.playerlib.ZQPlayer
import com.zq.zqplayer.R
import java.io.*

class TempActivity : MvpBaseActivity<IBasePresenter>() {
    override fun initForInject(appComponent: AppComponent?) {
    }

    @BindView(R.id.path)
    lateinit var mTvPath: TextView
    @BindView(R.id.play)
    lateinit var mBtnPlay: Button
    @BindView(R.id.surfaceview)
    lateinit var mSurfaceview: SurfaceView
    @BindView(R.id.surfaceview_filter)
    lateinit var mSurfaceviewFilter: SurfaceView

    internal var videoPath: String = ""
    internal var isPlaying: Boolean = false
    internal var mSurfaceHolder: SurfaceHolder? = null
    internal var mSurfaceHolderFilter: SurfaceHolder? = null

    var player: ZQPlayer? = null

    override fun initLayoutResourceId(): Int {
        return R.layout.temp
    }

    override fun initData(savedInstanceState: Bundle?) {
        initData()
        mTvPath.text = videoPath

        mSurfaceview.holder.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                mSurfaceHolder = surfaceHolder
                initPlayer()
            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
                mSurfaceHolder = surfaceHolder
            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
                mSurfaceHolder = null
                Toast.makeText(this@TempActivity, "surfaceDestroyed 1", Toast.LENGTH_SHORT).show()
            }
        })

        mSurfaceviewFilter.holder.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                mSurfaceHolderFilter = surfaceHolder
                initPlayer()
            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
                mSurfaceHolderFilter = surfaceHolder
            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
                mSurfaceHolderFilter = null
                Toast.makeText(this@TempActivity, "surfaceDestroyed 2", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun initPlayer(): Unit {
        if(isSurfaceReady()){
            player = ZQPlayer()
            player?.setStatusListener(object : StatusListener {
                override fun onLoading() {
                    runOnUiThread {
                        mBtnPlay.text = "初始化中。。。"
                        mBtnPlay.isEnabled = false
                    }
                }

                override fun onPrepareFinished() {
                    runOnUiThread {
                        mBtnPlay.text = "播放"
                        mBtnPlay.isEnabled = true
                    }
                }

                override fun onPlaying() {
                    runOnUiThread {
                        mBtnPlay.text = "暂停"
                    }
                }
                override fun onPause() {
                    runOnUiThread {
                        mBtnPlay.text = "播放"
                    }
                }
            })
            player?.playdemo(mSurfaceHolder?.surface!!, mSurfaceHolderFilter?.surface!!, videoPath, -1)
        }
    }

    @OnClick(R.id.play)
    fun onClick(view: View): Unit {
        when(view.id){
            R.id.play -> {
                play()
            }
            else -> {}
        }
    }

    @Synchronized
    private fun isSurfaceReady(): Boolean {
        return mSurfaceHolder != null && mSurfaceHolderFilter != null
    }

    @Synchronized
    private fun play() {
        if (player?.isPlaying()!!) {
            player?.pause()
        } else {
            player?.play()
        }
//            Thread(Runnable { player?.play(mSurfaceHolder!!.surface, mSurfaceHolderFilter!!.surface, videoPath, -1) }).start()
//            Thread(Runnable { player?.play(mSurfaceHolder!!.surface, mSurfaceHolderFilter!!.surface, videoPath, 1) }).start()
    }


    private fun initData() {
        videoPath = getDataDirPath(this, "meidacodec") + File.separator + "gao_bai_qi_qiu.mp4"
        val `in` = BufferedInputStream(resources.openRawResource(R.raw.gao_bai_qi_qiu))
        val out: BufferedOutputStream
        try {
            val outputStream = FileOutputStream(videoPath)
            out = BufferedOutputStream(outputStream)
            val buf = ByteArray(1024)
            var size = `in`.read(buf)
            while (size > 0) {
                out.write(buf, 0, size)
                size = `in`.read(buf)
            }
            `in`.close()
            out.flush()
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getDataDirPath(context: Context, dir: String): String {
        val path = context.externalCacheDir!!.absolutePath + File.separator + dir
        val file = File(path)
        if (!file.exists()) {
            file.mkdir()
        }
        return path
    }

}
