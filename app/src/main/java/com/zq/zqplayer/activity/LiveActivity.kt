package com.zq.zqplayer.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import butterknife.BindView
import com.ziq.base.mvp.BaseActivity
import com.ziq.base.mvp.IBasePresenter
import com.zq.playerlib.ZQVideoView
import com.zq.zqplayer.R
import java.io.*

/**
 *author: wuyanqiang
 *2018/11/15
 */
class LiveActivity : BaseActivity<IBasePresenter>(){

    companion object {
        const val VEDIO_URL:String = "vedio_url"
        fun openVideo(context: Context?, url:String): Unit {
            val intent: Intent = Intent(context, LiveActivity::class.java)
            intent.putExtra(VEDIO_URL, url)
            context?.startActivity(intent)
        }
    }

    @BindView(R.id.zq_video_view)
    lateinit var mZQVideoView: ZQVideoView
    private var videoPath: String = ""



    override fun initLayoutResourceId(): Int {
        return R.layout.activity_live
    }

    override fun initData(savedInstanceState: Bundle?) {
        if(intent != null){
            if(intent.hasExtra(VEDIO_URL)){
                videoPath = intent.getStringExtra(VEDIO_URL)
                mZQVideoView.setVideoPath(videoPath)
            }
        }
    }

    override fun onPause() {
        mZQVideoView.pause()
        super.onPause()
    }

    override fun onDestroy() {
        mZQVideoView.stop()
        super.onDestroy()
    }



}