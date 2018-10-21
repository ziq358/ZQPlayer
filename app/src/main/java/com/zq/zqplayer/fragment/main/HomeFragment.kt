package com.zq.zqplayer.fragment.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.ziq.base.mvp.BaseFragment
import com.ziq.base.mvp.IBasePresenter
import com.zq.customviewlib.HorizontalScrollViewTab
import com.zq.playerlib.ZQPlayer
import com.zq.zqplayer.R
import com.zq.zqplayer.fragment.EmptyFragment
import com.zq.zqplayer.fragment.topnavigation.RecommendFragment
import com.zq.zqplayer.model.TopNavigationItemBean
import java.io.*
import java.util.ArrayList

/**
 * @author wuyanqiang
 * @date 2018/10/15
 */
class HomeFragment : BaseFragment<IBasePresenter>() {

    @BindView(R.id.HorizontalScrollViewTab)
    lateinit var mHorizontalScrollViewTab: HorizontalScrollViewTab
    @BindView(R.id.ViewPager)
    lateinit var mViewPager: ViewPager


    override fun initLayoutResourceId(): Int {
        return R.layout.fragment_home
    }

    override fun initData(view: View, savedInstanceState: Bundle?) {
        var data: ArrayList<TopNavigationItemBean> = arrayListOf()
        data.add(object : TopNavigationItemBean("推荐") {
            override fun getFragment(): Fragment {
                return RecommendFragment()
            }
        })
        data.add(TopNavigationItemBean.getEmptyInstance("王者荣耀"))
        data.add(TopNavigationItemBean.getEmptyInstance( "绝地求生"))
        data.add(TopNavigationItemBean.getEmptyInstance( "LOL"))
        data.add(TopNavigationItemBean.getEmptyInstance( "星秀"))
        data.add(TopNavigationItemBean.getEmptyInstance( "吃鸡手游"))
        data.add(TopNavigationItemBean.getEmptyInstance( "吃喝玩乐"))
        data.add(TopNavigationItemBean.getEmptyInstance( "主机"))
        data.add(TopNavigationItemBean.getEmptyInstance( "CF"))
        data.add(TopNavigationItemBean.getEmptyInstance( "颜值"))
        data.add(TopNavigationItemBean.getEmptyInstance( "二次元"))
        data.add(TopNavigationItemBean.getEmptyInstance( "DNF"))
        data.add(TopNavigationItemBean.getEmptyInstance( "暴雪"))
        data.add(TopNavigationItemBean.getEmptyInstance( "我的世界"))

        var adapter: MyViewPagerAdapter = MyViewPagerAdapter(data, childFragmentManager);
        mViewPager.adapter = adapter

        mHorizontalScrollViewTab.addData(data as List<HorizontalScrollViewTab.ContentItem>?)
        mHorizontalScrollViewTab.setOnHorizontalNavigationSelectListener {
            mViewPager.setCurrentItem(it, false)
        }
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            var isScrolling: Boolean = false
            var tempPosition: Int = 0;
            var currentPostion: Int = 0;
            override fun onPageScrollStateChanged(state: Int) {
                isScrolling = state == ViewPager.SCROLL_STATE_DRAGGING
                Log.d("ziq", String.format("onPageScrollStateChanged %d  ", state) )
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if(isScrolling){
                    tempPosition = position
                    currentPostion = mViewPager.currentItem
                }
                if(tempPosition == position){
                    if(currentPostion == 0){
                        mHorizontalScrollViewTab.setPositionChange(currentPostion, 1, positionOffset)
                    }
                    else if(currentPostion > position){
                        //左滑
                        mHorizontalScrollViewTab.setPositionChange(currentPostion, currentPostion - 1, 1 - positionOffset)
                    }else{
                        //右滑
                        mHorizontalScrollViewTab.setPositionChange(currentPostion, currentPostion + 1, positionOffset)
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                mHorizontalScrollViewTab.setPosition(position)
            }
        })

    }

    class MyViewPagerAdapter(var data:ArrayList<TopNavigationItemBean>, var fm: FragmentManager): FragmentStatePagerAdapter(fm){

        override fun getCount(): Int {
            return data.size
        }

        override fun getItem(position: Int): Fragment {
            return data.get(position).getFragment()
        }

    }

}