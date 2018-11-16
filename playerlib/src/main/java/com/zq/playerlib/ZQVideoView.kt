package com.zq.playerlib

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout

class ZQVideoView :FrameLayout{

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr){
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.layout_zq_video_view, this)
    }
}