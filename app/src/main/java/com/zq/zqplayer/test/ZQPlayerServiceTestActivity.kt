package com.zq.zqplayer.test

import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import butterknife.BindView
import butterknife.OnClick
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseActivity
import com.zq.playerlib.service.PlayerItemInfo
import com.zq.playerlib.service.StatusListener
import com.zq.playerlib.service.ZQPlayerServiceWrap
import com.zq.zqplayer.R
import com.zq.zqplayer.util.VideoDataUtil

/**
 *@author wuyanqiang
 *@date 2019/1/16
 */
class ZQPlayerServiceTestActivity : MvpBaseActivity<IBasePresenter>() {

    private var zqPlayerServiceWrap: ZQPlayerServiceWrap? = null
    var playeriteminfo = PlayerItemInfo()

    @BindView(R.id.btn_init)
    lateinit var btn_init: Button

    @BindView(R.id.btn_play)
    lateinit var btn_play: Button

    @BindView(R.id.btn_stop)
    lateinit var btn_stop: Button

    @BindView(R.id.btn_play_window)
    lateinit var btn_play_window: Button



    @BindView(R.id.surfaceView)
    lateinit var mSurfaceView:SurfaceView
    var mSurfaceHolder: SurfaceHolder? = null

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_zq_player_service_test
    }

    override fun initForInject(appComponent: AppComponent?) {
    }

    override fun initData(savedInstanceState: Bundle?) {
        zqPlayerServiceWrap = ZQPlayerServiceWrap(this)
        playeriteminfo.url = VideoDataUtil.getTestVideoPath(this.application)
        mSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                mSurfaceHolder = surfaceHolder
                btn_init.isEnabled = true
                btn_play_window.isEnabled = true
            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
                mSurfaceHolder = surfaceHolder
                btn_init.isEnabled = true
                btn_play_window.isEnabled = true
            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
                mSurfaceHolder = null
            }
        })

    }


    @OnClick(R.id.btn_init, R.id.btn_stop, R.id.btn_play, R.id.btn_play_window)
    fun onClick(view: View): Unit {
        when(view.id){
            R.id.btn_init -> {
                mSurfaceView.visibility = View.GONE  // surface 会 重新 建立
                mSurfaceView.visibility = View.VISIBLE
                zqPlayerServiceWrap?.init(playeriteminfo, mSurfaceHolder?.surface!!, object : StatusListener.Stub(){
                    override fun onLoading() {
                    }

                    override fun onPause() {
                    }

                    override fun onStop() {
                        runOnUiThread {
                            btn_play.isEnabled = false
                            btn_stop.isEnabled = true
                        }
                    }

                    override fun onError(msg: String?) {
                    }

                    override fun onPrepareFinished() {
                        runOnUiThread {
                            btn_play.isEnabled = true
                        }
                    }

                    override fun onPlaying() {
                        runOnUiThread {
                            btn_stop.isEnabled = true
                        }
                    }

                })
            }
            R.id.btn_play -> {
                zqPlayerServiceWrap?.play()
            }
            R.id.btn_stop -> {
                zqPlayerServiceWrap?.stop()
            }
            R.id.btn_play_window -> {
                zqPlayerServiceWrap?.showFloatingWindow()
            }
            else -> {}
        }
    }

    override fun onDestroy() {
        zqPlayerServiceWrap?.destroy()
        super.onDestroy()
    }





}