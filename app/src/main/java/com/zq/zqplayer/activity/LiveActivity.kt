package com.zq.zqplayer.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.widget.Button
import android.widget.TextView
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
import com.zq.zqplayer.fragment.main.*
import java.io.*

/**
 *author: wuyanqiang
 *2018/11/15
 */
class LiveActivity : BaseActivity<IBasePresenter>(){


    companion object {
        const val VEDIO_URL:String = "vedio_url"
        fun openVideo(context: Context?, url:String): Unit {
            val intent = Intent(context, LiveActivity::class.java)
            intent.putExtra(VEDIO_URL, url)
            context?.startActivity(intent)
        }
    }

    @BindView(R.id.zq_video_view)
    lateinit var mZQVideoView: ZQVideoView
    private var videoPath: String = ""

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
        if(intent != null){
            if(intent.hasExtra(VEDIO_URL)){
                videoPath = intent.getStringExtra(VEDIO_URL)
                mZQVideoView.setVideoPath(videoPath)
            }
        }

        val dataList: ArrayList<FragmentViewPagerAdapter.Data> = arrayListOf()
        dataList.add(FragmentViewPagerAdapter.Data("聊天", ChatFragment()))
        dataList.add(FragmentViewPagerAdapter.Data("主播", TheHostFragment()))
        dataList.add(FragmentViewPagerAdapter.Data("排行", RankingFragment()))
        dataList.add(FragmentViewPagerAdapter.Data("贵宾", VIPListFragment()))
        val adapter = FragmentViewPagerAdapter(dataList, supportFragmentManager)
        mViewPager.adapter = adapter;
        mSlidingTabLayout.setViewPager(mViewPager)
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