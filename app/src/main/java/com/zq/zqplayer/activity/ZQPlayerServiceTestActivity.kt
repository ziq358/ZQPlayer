package com.zq.zqplayer.activity

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import butterknife.OnClick
import com.ziq.base.dagger.component.AppComponent
import com.ziq.base.mvp.BaseActivity
import com.ziq.base.mvp.IBasePresenter
import com.zq.playerlib.service.PlayerItemInfo
import com.zq.playerlib.service.ZQPlayerService
import com.zq.playerlib.service.ZQPlayerServiceBinder
import com.zq.zqplayer.R

/**
 *@author wuyanqiang
 *@date 2019/1/16
 */
class ZQPlayerServiceTestActivity : BaseActivity<IBasePresenter>() {



    override fun initLayoutResourceId(): Int {
        return R.layout.activity_zq_player_service_test
    }

    override fun initForInject(appComponent: AppComponent?) {
    }

    override fun initData(savedInstanceState: Bundle?) {
    }


    @OnClick(R.id.btn_start, R.id.btn_stop, R.id.btn_bind, R.id.btn_unbind)
    fun onClick(view: View): Unit {
        when(view.id){
            R.id.btn_start -> {
                Log.d("ziq", "btn_start")
                ZQPlayerService.startZQPlayerService(this)
            }
            R.id.btn_stop -> {
                Log.d("ziq", "btn_stop")
            }
            R.id.btn_bind -> {
                Log.d("ziq", "btn_bind")
                ZQPlayerService.bindZQPlayerService(this, object : ServiceConnection {
                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        val serviceBinder = ZQPlayerServiceBinder.Stub.asInterface(service)
                        var playeriteminfo = PlayerItemInfo()
                        playeriteminfo.url = "PlayerItemInfo url"
                        serviceBinder.setPlayItemInfo(playeriteminfo)
                        serviceBinder.play()
                    }

                    override fun onServiceDisconnected(name: ComponentName?) {
                    }
                })
            }
            R.id.btn_unbind -> {
                Log.d("ziq", "btn_unbind")
            }
            else -> {}
        }
    }

}