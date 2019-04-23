package com.zq.zqplayer.mvp.live.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import cn.sharesdk.onekeyshare.OnekeyShare
import com.flyco.tablayout.SlidingTabLayout
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseActivity
import com.zq.playerlib.ZQVideoView
import com.zq.playerlib.service.PlayerItemInfo
import com.zq.playerlib.service.ZQPlayerService
import com.zq.playerlib.service.ZQPlayerServiceBinder
import com.zq.zqplayer.R
import com.zq.zqplayer.mvp.adapter.FragmentViewPagerAdapter
import me.leolin.shortcutbadger.ShortcutBadger


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
        ZQPlayerService.startZQPlayerService(this)
        ZQPlayerService.bindZQPlayerService(this, serviceConnection)
        initViewPager()
        initVideoView()
        ShortcutBadger.removeCount(this)
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
                mZQVideoView.start()
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
                showShare()
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
        var playeriteminfo = PlayerItemInfo()
        playeriteminfo.url = videoPath
        serviceBinder?.showFloatingWindow(playeriteminfo)
        mZQVideoView.stop()
        super.onDestroy()
    }

    var serviceBinder: ZQPlayerServiceBinder? = null
    var serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceBinder = ZQPlayerServiceBinder.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBinder = null
        }
    }


    private fun showShare() {
        val oks = OnekeyShare()
        //关闭sso授权
//        oks.disableSSOWhenAuthorize()

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享")
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn")
        // text是分享文本，所有平台都需要这个字段
        oks.text = "我是分享文本"
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg")//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn")
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本")
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name))
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn")

        // 启动分享GUI
        oks.show(this)
    }

}