package com.zq.zqplayer

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.ziq.base.dagger.component.AppComponent
import com.ziq.base.mvp.BaseActivity
import com.ziq.base.mvp.IBasePresenter
import com.zq.customviewlib.NoScrollViewPager
import com.zq.zqplayer.fragment.main.*

class MainActivity : BaseActivity<IBasePresenter>() {
    override fun initForInject(appComponent: AppComponent?) {
    }

    @BindView(R.id.ViewPager)
    lateinit var viewPager: NoScrollViewPager

    @BindView(R.id.TabLayout)
    lateinit var mTabLayout: TabLayout

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {
        var titleList: ArrayList<String> = arrayListOf()
        var iconList: ArrayList<Int> = arrayListOf()
        var dataList: ArrayList<Fragment> = arrayListOf()
        var home1: HomeFragment = HomeFragment()
        var home2: AmusementFragment = AmusementFragment()
        var home3: SubscribeFragment = SubscribeFragment()
        var home4: DiscoveryFragment = DiscoveryFragment()
        var home5: MineFragment = MineFragment()

        titleList.add("首页")
        titleList.add("娱乐")
        titleList.add("订阅")
        titleList.add("发现")
        titleList.add("我的")

        iconList.add(R.drawable.selector_tab_home)
        iconList.add(R.drawable.selector_tab_amusement)
        iconList.add(R.drawable.selector_tab_subscribe)
        iconList.add(R.drawable.selector_tab_discover)
        iconList.add(R.drawable.selector_tab_mine)

        dataList.add(home1)
        dataList.add(home2)
        dataList.add(home3)
        dataList.add(home4)
        dataList.add(home5)

        var adapter: MainAdapter = MainAdapter(titleList, dataList, supportFragmentManager)
        viewPager.adapter = adapter;
//        viewPager.offscreenPageLimit = 5
//        mTabLayout.setupWithViewPager(viewPager)
        for(i in 0..(titleList.size - 1)){
            val tab :TabLayout.Tab = mTabLayout.newTab()
            val tabContent:View = View.inflate(this, R.layout.main_tab_layout, null)
            tabContent.findViewById<ImageView>(R.id.icon).setBackgroundResource(iconList[i])
            tabContent.findViewById<TextView>(R.id.title).text = titleList[i]
            tab.customView = tabContent
            mTabLayout.addTab(tab)
        }
        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.setCurrentItem(tab?.position!!, false)
            }

        })
    }


    class MainAdapter(var titleList: List<String>, var dataList: List<Fragment>, fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return dataList[position]
        }

        override fun getCount(): Int {
            return dataList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }

    }

}
