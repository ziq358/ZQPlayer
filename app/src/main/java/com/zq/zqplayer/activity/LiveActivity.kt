package com.zq.zqplayer.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.widget.Toast
import butterknife.BindView
import com.flyco.tablayout.SlidingTabLayout
import com.ziq.base.dagger.component.AppComponent
import com.ziq.base.mvp.BaseActivity
import com.ziq.base.mvp.IBasePresenter
import com.zq.playerlib.ZQVideoView
import com.zq.zqplayer.R
import com.zq.zqplayer.adapter.FragmentViewPagerAdapter
import com.zq.zqplayer.fragment.live.ChatFragment
import com.zq.zqplayer.fragment.live.RankingFragment
import com.zq.zqplayer.fragment.live.TheHostFragment
import com.zq.zqplayer.fragment.live.VIPListFragment
import kotlinx.android.synthetic.main.item_chat.*

/**
 *author: wuyanqiang
 *2018/11/15
 */
class LiveActivity : BaseActivity<IBasePresenter>(){


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

    @BindView(R.id.tablayout)
    lateinit var mSlidingTabLayout: SlidingTabLayout
    @BindView(R.id.viewpager)
    lateinit var mViewPager: ViewPager

    override fun initForInject(appComponent: AppComponent?) {
    }

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_live
    }

    override fun initData(savedInstanceState: Bundle?) {
        initViewPager()
        initVideoView()
    }

    fun initViewPager(): Unit {
        val dataList: ArrayList<FragmentViewPagerAdapter.Data> = arrayListOf()
        dataList.add(FragmentViewPagerAdapter.Data("聊天", ChatFragment()))
        dataList.add(FragmentViewPagerAdapter.Data("主播", TheHostFragment()))
        dataList.add(FragmentViewPagerAdapter.Data("排行", RankingFragment()))
        dataList.add(FragmentViewPagerAdapter.Data("贵宾", VIPListFragment()))
        val adapter = FragmentViewPagerAdapter(dataList, supportFragmentManager)
        mViewPager.adapter = adapter;
        mSlidingTabLayout.setViewPager(mViewPager)
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
                    mZQVideoView.start()
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
                Toast.makeText(this@LiveActivity, "onShareClick", Toast.LENGTH_SHORT).show()
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


    override fun onPause() {
        mZQVideoView.pause()
        super.onPause()
    }

    override fun onDestroy() {
        mZQVideoView.stop()
        super.onDestroy()
    }



}