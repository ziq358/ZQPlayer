package com.zq.playerlib

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class ZQVideoView :FrameLayout, View.OnClickListener{


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr){
        init()
    }

    var mIvBack:ImageView? = null
    var mTvTitle:TextView? = null
    var mTvClarity:TextView? = null
    var mIvSetting:ImageView? = null
    var mIvShare:ImageView? = null
    var mIvBarrage:ImageView? = null
    var mIvStar:ImageView? = null
    var mIvFullScreen:ImageView? = null


    internal var mSurfaceHolder: SurfaceHolder? = null
    var mUrl:String? = null
    var mSurfaceView: SurfaceView? = null
    var player: ZQPlayer? = null

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.layout_zq_video_view, this)

        mIvBack = findViewById(R.id.iv_back)
        mTvTitle = findViewById(R.id.tv_title)
        mTvClarity = findViewById(R.id.tv_clarity)
        mIvSetting = findViewById(R.id.iv_setting)
        mIvShare = findViewById(R.id.iv_share)
        mIvBarrage = findViewById(R.id.iv_barrage)
        mIvStar = findViewById(R.id.iv_star)
        mIvFullScreen = findViewById(R.id.iv_full_screen)

        mSurfaceView = findViewById(R.id.surfaceView)

        mIvBack?.setOnClickListener(this)
        mTvClarity?.setOnClickListener(this)
        mIvSetting?.setOnClickListener(this)
        mIvShare?.setOnClickListener(this)
        mIvBarrage?.setOnClickListener(this)
        mIvStar?.setOnClickListener(this)
        mIvFullScreen?.setOnClickListener(this)


        mSurfaceView?.holder?.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                mSurfaceHolder = surfaceHolder
                initPlayer(mUrl, mSurfaceHolder)
            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
                mSurfaceHolder = surfaceHolder
                initPlayer(mUrl, mSurfaceHolder)
            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
                mSurfaceHolder = null
            }
        })

    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back ->{
                Toast.makeText(context, "iv_back", Toast.LENGTH_SHORT).show()}
            R.id.tv_clarity ->{Toast.makeText(context, "tv_clarity", Toast.LENGTH_SHORT).show()}
            R.id.iv_setting ->{Toast.makeText(context, "iv_setting", Toast.LENGTH_SHORT).show()}
            R.id.iv_share ->{Toast.makeText(context, "iv_share", Toast.LENGTH_SHORT).show()}
            R.id.iv_barrage ->{Toast.makeText(context, "iv_barrage", Toast.LENGTH_SHORT).show()}
            R.id.iv_star ->{Toast.makeText(context, "iv_star", Toast.LENGTH_SHORT).show()}
            R.id.iv_full_screen ->{Toast.makeText(context, "iv_full_screen", Toast.LENGTH_SHORT).show()}
            else ->{}
        }
    }

    fun setVideoPath(url:String?): Unit {
        mUrl = url
        initPlayer(mUrl, mSurfaceHolder)
    }

    internal fun initPlayer(url:String?, surfaceHolder: SurfaceHolder?): Unit {
        if(player == null && !TextUtils.isEmpty(url) && surfaceHolder != null){
            player = ZQPlayer()
            player?.setStatusListener(object : ZQPlayer.StatusListener {
                override fun onLoading() {
//                runOnUiThread {
//                    mBtnPlay.text = "初始化中。。。"
//                    mBtnPlay.isEnabled = false
//                }
                }

                override fun onPrepareFinished() {
                    player?.start()
//                runOnUiThread {
//                    mBtnPlay.text = "播放"
//                    mBtnPlay.isEnabled = true
//                }
                }

                override fun onPlaying() {
//                runOnUiThread {
//                    mBtnPlay.text = "暂停"
//                }
                }
                override fun onPause() {
//                runOnUiThread {
//                    mBtnPlay.text = "播放"
//                }
                }
            })
            player?.prepare(url!!)
            player?.setSurfsce(surfaceHolder.surface)
        }

    }

    fun start(): Unit {
        player?.start()
    }

    fun pause(): Unit {
        player?.pause()
    }

    fun stop(): Unit {
        player?.stop()
    }



}