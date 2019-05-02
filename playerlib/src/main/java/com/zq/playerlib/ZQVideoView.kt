package com.zq.playerlib

import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

class ZQVideoView :FrameLayout, View.OnClickListener{


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr){
        init()
    }

    var mTopMenuBar:View? = null
    var mBottomMenuBar:View? = null
    var mLoadingView:View? = null

    var mIvBack:ImageView? = null
    var mTvTitle:TextView? = null
    var mTvClarity:TextView? = null
    var mIvSetting:ImageView? = null
    var mIvShare:ImageView? = null
    var mIvPlay:ImageView? = null
    var mIvBarrage:ImageView? = null
    var mIvStar:ImageView? = null
    var mIvFullScreen:ImageView? = null


    internal var mSurfaceHolder: SurfaceHolder? = null
    var mUrl:String? = null
    var mSurfaceView: SurfaceView? = null
    var player: ZQPlayer? = null

    private var onActionListener:OnActionListener? = null

    private var isMenuShowed:Boolean = true

    private var zqHandler:ZQHandler? = null

    private val STATUS_LOADING:Int = 1
    private val STATUS_PREPAREFINISHED:Int = 2


    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.layout_zq_video_view, this)

        zqHandler = ZQHandler()


        mTopMenuBar = findViewById(R.id.rl_top_bar)
        mBottomMenuBar = findViewById(R.id.rl_bottom_bar)
        mLoadingView = findViewById(R.id.fl_loading)
        mIvBack = findViewById(R.id.iv_back)

        mTvTitle = findViewById(R.id.tv_title)
        mTvClarity = findViewById(R.id.tv_clarity)
        mIvSetting = findViewById(R.id.iv_setting)
        mIvShare = findViewById(R.id.iv_share)

        mIvPlay = findViewById(R.id.iv_play)
        mIvBarrage = findViewById(R.id.iv_barrage)
        mIvStar = findViewById(R.id.iv_star)
        mIvFullScreen = findViewById(R.id.iv_full_screen)

        mSurfaceView = findViewById(R.id.surfaceView)

        mIvBack?.setOnClickListener(this)
        mTvClarity?.setOnClickListener(this)
        mIvSetting?.setOnClickListener(this)
        mIvShare?.setOnClickListener(this)

        mIvPlay?.setOnClickListener(this)
        mIvPlay?.isEnabled = false
        mIvBarrage?.setOnClickListener(this)
        mIvStar?.setOnClickListener(this)
        mIvFullScreen?.setOnClickListener(this)

        mSurfaceView?.setOnClickListener(this)
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

    fun setVideoPath(url:String?): Unit {
        mUrl = url
        initPlayer(mUrl, mSurfaceHolder)
    }

    internal fun initPlayer(url:String?, surfaceHolder: SurfaceHolder?): Unit {
        if(player == null && !TextUtils.isEmpty(url) && surfaceHolder != null){
            player = ZQPlayer()
            player?.setStatusListener(object : StatusListener {
                //回调 在 子线程
                override fun onLoading() {
                    zqHandler?.sendEmptyMessage(STATUS_LOADING)
                }
                override fun onPrepareFinished() {
                    zqHandler?.sendEmptyMessage(STATUS_PREPAREFINISHED)
                }
                override fun onPlaying() {
                }
                override fun onPause() {
                }

                override fun onError(msg: String) {
                    Log.e("ziq", msg)
                }

            })
            player?.init(url!!)
            player?.setSurfaceTarget(surfaceHolder.surface)
        }

    }

    inner class ZQHandler: Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                STATUS_LOADING -> {
                    mLoadingView?.visibility = View.VISIBLE
                    onActionListener?.onLoading()
                }
                STATUS_PREPAREFINISHED -> {
                    mIvPlay?.isEnabled = true
                    mLoadingView?.visibility = View.GONE
                    onActionListener?.onPrepareFinished()
                }
            }
        }
    }


    fun setTitle(title:String): Unit {
        mTvTitle?.text = title
    }

    fun setActionListener(listener:OnActionListener?): Unit {
        onActionListener = listener
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.surfaceView ->{
                onActionListener?.onClick()
            }
            R.id.iv_play ->{
                onActionListener?.onPlayClick()
            }
            R.id.iv_back ->{
                onActionListener?.onBackClick()
            }
            R.id.tv_clarity ->{
                onActionListener?.onClarityClick()
            }
            R.id.iv_setting ->{
                onActionListener?.onSettingClick()
            }
            R.id.iv_share ->{
                onActionListener?.onShareClick()
            }
            R.id.iv_barrage ->{
                onActionListener?.onBarrageClick()
            }
            R.id.iv_star ->{
                onActionListener?.onStarClick()
            }
            R.id.iv_full_screen ->{
                onActionListener?.onFullScreenClick()
            }
            else ->{}
        }
    }



    fun isMenuShowed(): Boolean {
        return isMenuShowed
    }

    fun showController(): Unit {
        isMenuShowed = true
        mTopMenuBar?.visibility = View.VISIBLE
        mBottomMenuBar?.visibility = View.VISIBLE
        mIvBack?.visibility = View.VISIBLE
    }

    fun hideController(): Unit {
        isMenuShowed = false
        mTopMenuBar?.visibility = View.GONE
        mBottomMenuBar?.visibility = View.GONE
        mIvBack?.visibility = View.GONE
    }

    fun isPlaying(): Boolean {
        if(player != null){
            return player!!.isPlaying()
        }
        return false
    }

    fun play(): Unit {
        mIvPlay?.setBackgroundResource(R.drawable.ic_pause)
        player?.play()
    }

    fun pause(): Unit {
        mIvPlay?.setBackgroundResource(R.drawable.ic_play)
        player?.pause()
    }

    fun stop(): Unit {
        player?.stop()
    }

    interface OnActionListener{
        fun onLoading()
        fun onPrepareFinished()
        fun onClick()
        fun onPlayClick()
        fun onBackClick()
        fun onClarityClick()
        fun onSettingClick()
        fun onShareClick()
        fun onBarrageClick()
        fun onStarClick()
        fun onFullScreenClick()
    }



}