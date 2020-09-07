package com.zq.zqplayer.mvp.live.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseActivity
import com.zq.playerlib.ZQVideoView
import com.zq.zqplayer.R
import com.zq.zqplayer.mvp.adapter.FragmentViewPagerAdapter


/**
 *author: wuyanqiang
 *2018/11/15
 */
class LiveActivity : MvpBaseActivity<IBasePresenter>(){


    companion object {
        const val VIDEO_URL:String = "video_url"
        const val VIDEO_TITLE:String = "video_title"
        fun openVideo(context: Context?, url:String, title:String): Unit {
            val intent = Intent(context, LiveActivity::class.java)
            intent.putExtra(VIDEO_URL, url)
            intent.putExtra(VIDEO_TITLE, title)
            context?.startActivity(intent)
        }
    }

    @BindView(R.id.zq_video_view)
    lateinit var mZQVideoView: ZQVideoView
    private var videoPath: String = ""
    private var title: String = ""

    override fun initForInject(appComponent: AppComponent?) {
    }

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_live
    }

    override fun initData(savedInstanceState: Bundle?) {
        initVideoView()
    }

    fun initVideoView(){
        if(intent != null){
            if(intent.hasExtra(VIDEO_URL)){
                videoPath = intent.getStringExtra(VIDEO_URL)
                mZQVideoView.setVideoPath(videoPath)
            }
            if(intent.hasExtra(VIDEO_TITLE)){
                title = intent.getStringExtra(VIDEO_TITLE)
                mZQVideoView.setTitle(title)
            }
        }
        mZQVideoView.setActionListener(object : ZQVideoView.OnActionListener {
            override fun onLoading() {
            }

            override fun onPrepareFinished() {
                mZQVideoView.play()
            }

            override fun onClick() {
                if(mZQVideoView.isMenuShowed()){
                    mZQVideoView.hideController()
                }else{
                    mZQVideoView.showController()
                }
            }

            override fun onPlayClick() {
                if(mZQVideoView.isPlaying()){
                    mZQVideoView.pause()
                }else{
                    mZQVideoView.play()
                }
            }

            override fun onBackClick() {
                finish()
            }

            override fun onClarityClick() {
                Toast.makeText(this@LiveActivity, "onClarityClick", Toast.LENGTH_SHORT).show()
            }

            override fun onSettingClick() {
                Toast.makeText(this@LiveActivity, "onSettingClick", Toast.LENGTH_SHORT).show()
            }

            override fun onShareClick() {
            }

            override fun onBarrageClick() {
                Toast.makeText(this@LiveActivity, "onBarrageClick", Toast.LENGTH_SHORT).show()
            }

            override fun onStarClick() {
                Toast.makeText(this@LiveActivity, "onStarClick", Toast.LENGTH_SHORT).show()
            }

            override fun onFullScreenClick() {
                Toast.makeText(this@LiveActivity, "onFullScreenClick", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onDestroy() {
        if(mZQVideoView.isPlaying()){
            mZQVideoView.showFloatingWindow()
        }
        super.onDestroy()
    }

}