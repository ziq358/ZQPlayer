package com.zq.playerlib

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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


    var mSurfaceView: SurfaceView? = null


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

    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back ->{ Toast.makeText(context, "iv_back", Toast.LENGTH_SHORT).show()}
            R.id.tv_clarity ->{Toast.makeText(context, "tv_clarity", Toast.LENGTH_SHORT).show()}
            R.id.iv_setting ->{Toast.makeText(context, "iv_setting", Toast.LENGTH_SHORT).show()}
            R.id.iv_share ->{Toast.makeText(context, "iv_share", Toast.LENGTH_SHORT).show()}
            R.id.iv_barrage ->{Toast.makeText(context, "iv_barrage", Toast.LENGTH_SHORT).show()}
            R.id.iv_star ->{Toast.makeText(context, "iv_star", Toast.LENGTH_SHORT).show()}
            R.id.iv_full_screen ->{Toast.makeText(context, "iv_full_screen", Toast.LENGTH_SHORT).show()}
            else ->{}
        }
    }

    fun setVideoPath(url:String): Unit {

    }

    fun prepare(): Unit {

    }




}