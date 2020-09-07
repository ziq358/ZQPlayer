package com.zq.zqplayer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.google.android.material.tabs.TabLayout
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseActivity
import com.zq.customviewlib.NoScrollViewPager
import com.zq.zqplayer.mvp.adapter.FragmentViewPagerAdapter
import com.zq.zqplayer.mvp.main.ui.*
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class MainActivity : MvpBaseActivity<IBasePresenter>() {


    companion object {
        var isForeground = false
        const val MESSAGE_RECEIVED_ACTION = "com.epro.g3.yuanyi.doctor.MESSAGE_RECEIVED_ACTION"
        const val KEY_TITLE = "title"
        const val KEY_MESSAGE = "message"
        const val KEY_EXTRAS = "extras"
    }

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
        var iconList: ArrayList<Int> = arrayListOf()
        iconList.add(R.drawable.selector_tab_home)
        iconList.add(R.drawable.selector_tab_amusement)
        iconList.add(R.drawable.selector_tab_subscribe)
        iconList.add(R.drawable.selector_tab_discover)
        iconList.add(R.drawable.selector_tab_mine)

        var dataList: ArrayList<FragmentViewPagerAdapter.Data> = arrayListOf()
        dataList.add(FragmentViewPagerAdapter.Data("首页", HomeFragment()))
        dataList.add(FragmentViewPagerAdapter.Data("娱乐", AmusementFragment()))
        dataList.add(FragmentViewPagerAdapter.Data("订阅", SubscribeFragment()))
        dataList.add(FragmentViewPagerAdapter.Data("发现", DiscoveryFragment()))
        dataList.add(FragmentViewPagerAdapter.Data("我的", MineFragment()))

        var adapter: FragmentViewPagerAdapter = FragmentViewPagerAdapter(dataList, supportFragmentManager)
        viewPager.adapter = adapter;
//        viewPager.offscreenPageLimit = 5
//        mTabLayout.setupWithViewPager(viewPager)
        for(i in 0 until dataList.size){
            val tab : TabLayout.Tab = mTabLayout.newTab()
            val tabContent:View = View.inflate(this, R.layout.main_tab_layout, null)
            tabContent.findViewById<ImageView>(R.id.icon).setBackgroundResource(iconList[i])
            tabContent.findViewById<TextView>(R.id.title).text = dataList[i].title
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

        //协程
        loadData()
        loadData2()
    }

    //协程
    private val uiContext: CoroutineContext = Dispatchers.Main
    private val bgContext: CoroutineContext = Dispatchers.Default

    private fun loadData() = GlobalScope.launch(uiContext, CoroutineStart.DEFAULT) {
        //        view.showLoading() // ui thread
        Log.d("ziq", String.format("GlobalScope.launch1 %s  ", Thread.currentThread()) )

        val task = async(bgContext) {
            delay(TimeUnit.SECONDS.toMillis(1))
            Log.d("ziq", String.format("async %s  ", Thread.currentThread()) )

        }

        // non ui thread, suspend until the task is finished or return null in 2 sec
        val result = withTimeoutOrNull(TimeUnit.SECONDS.toMillis(2) ){ task.await() }
        Log.d("ziq", String.format("GlobalScope.launch2 %s  ", Thread.currentThread()) )

    }

    private fun loadData2() = runBlocking { // this: CoroutineScope
        launch {
            doWorld()
        }
        println("launch is over")
        //阻塞线程 结构化的并发
        coroutineScope { // 创建一个协程作用域
            launch {
                delay(500L)
                println("Task from nested launch")
            }

            delay(100L)
            println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
        }

        println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
    }

    // 挂起函数
    private suspend fun doWorld() {
        delay(200L)
        println("Task from runBlocking")
    }
}
