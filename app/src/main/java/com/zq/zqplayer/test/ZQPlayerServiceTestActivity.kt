package com.zq.zqplayer.test

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import butterknife.BindView
import butterknife.OnClick
import com.ziq.base.mvp.BaseActivity
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.dagger.component.AppComponent
import com.zq.playerlib.service.PlayerItemInfo
import com.zq.playerlib.service.ZQPlayerService
import com.zq.playerlib.service.ZQPlayerServiceBinder
import com.zq.zqplayer.R
import com.zq.zqplayer.mvp.live.ui.LiveActivity

/**
 *@author wuyanqiang
 *@date 2019/1/16
 */
class ZQPlayerServiceTestActivity : BaseActivity<IBasePresenter>() {

    var serviceBinder: ZQPlayerServiceBinder? = null
    private var videoPath: String = ""
    var playeriteminfo = PlayerItemInfo()

    @BindView(R.id.btn_play_window)
    lateinit var btn_play_window: Button

    @BindView(R.id.btn_play)
    lateinit var btn_play: Button
    @BindView(R.id.surfaceView)
    lateinit var mSurfaceView:SurfaceView
    var mSurfaceHolder: SurfaceHolder? = null

    companion object {
        const val VIDEO_URL:String = "video_url"
        const val VIDEO_TITLE:String = "video_title"
        fun start(context: Context?, url:String): Unit {
            val intent = Intent(context, ZQPlayerServiceTestActivity::class.java)
            intent.putExtra(VIDEO_URL, url)
            context?.startActivity(intent)
        }
    }


    override fun initLayoutResourceId(): Int {
        return R.layout.activity_zq_player_service_test
    }

    override fun initForInject(appComponent: AppComponent?) {
    }

    override fun initData(savedInstanceState: Bundle?) {
        videoPath = intent.getStringExtra(LiveActivity.VIDEO_URL)
        playeriteminfo.url = videoPath
        mSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                mSurfaceHolder = surfaceHolder
                btn_play.isEnabled = true
                btn_play_window.isEnabled = true
            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
                mSurfaceHolder = surfaceHolder
                btn_play.isEnabled = true
                btn_play_window.isEnabled = true
            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
                mSurfaceHolder = null
            }
        })

    }


    @OnClick(R.id.btn_start, R.id.btn_stop, R.id.btn_bind, R.id.btn_unbind,R.id.btn_play, R.id.btn_play_window)
    fun onClick(view: View): Unit {
        when(view.id){
            R.id.btn_start -> {
                Log.d("ziq", "btn_start")
                mSurfaceView.visibility = View.GONE  // surface 会 重新 建立
                mSurfaceView.visibility = View.VISIBLE
                ZQPlayerService.startZQPlayerService(this)
            }
            R.id.btn_stop -> {
                Log.d("ziq", "btn_stop")
                ZQPlayerService.stopZQPlayerService(this)
            }
            R.id.btn_bind -> {
                Log.d("ziq", "btn_bind")
                ZQPlayerService.bindZQPlayerService(this, serviceConnection)
            }
            R.id.btn_unbind -> {
                Log.d("ziq", "btn_unbind")
                ZQPlayerService.unbindZQPlayerService(this, serviceConnection)
            }
            R.id.btn_play -> {
                serviceBinder?.initPlayer(playeriteminfo, mSurfaceHolder?.surface)
            }
            R.id.btn_play_window -> {
                serviceBinder?.showFloatingWindow(playeriteminfo)
            }
            else -> {}
        }
    }

    override fun onDestroy() {
        ZQPlayerService.unbindZQPlayerService(this, serviceConnection)
        super.onDestroy()
    }

    var serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceBinder = ZQPlayerServiceBinder.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBinder = null
        }
    }

}