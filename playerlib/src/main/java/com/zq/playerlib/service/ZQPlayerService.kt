package com.zq.playerlib.service

import android.app.Service
import android.content.*
import android.os.IBinder
import android.util.Log
import android.view.Surface
import com.zq.playerlib.ZQPlayer
import java.lang.ref.WeakReference

/**
 *@author wuyanqiang
 *@date 2019/1/16
 */
class ZQPlayerService: Service() {

    companion object {
        fun startZQPlayerService(context: Context) {
            val intent = Intent(context, ZQPlayerService::class.java)
            context.startService(intent)
        }

        fun stopZQPlayerService(context: Context) {
            val intent = Intent(context, ZQPlayerService::class.java)
            context.stopService(intent)
        }

        fun bindZQPlayerService(context: Context, serviceConnection: ServiceConnection) {
            //需要先启动服务 start service first，不然直接绑定不会立即回调onServiceConnected， 待 服务start后，统一回调所有之前 进行的绑定动作
            val intent = Intent(context, ZQPlayerService::class.java)
            context.bindService(intent, serviceConnection, 0)
        }

        fun unbindZQPlayerService(context: Context, serviceConnection: ServiceConnection) {
            //activity 绑定的时候  把ServiceConnection 给了 服务，serviceConnection 强引用了activity, 退出activity时 不解绑会 内存泄漏
            //不会调用  ServiceConnection.onServiceDisconnected  只有服务crash 或被kill 才调用
            context.unbindService(serviceConnection)
        }

        val TAG = "ZQPlayerService"

        val PLAY_CMD = "com.zq.playerlib.service.zqplayerservice.play"

    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        val intentFilter: IntentFilter = IntentFilter()
        intentFilter.addAction(PLAY_CMD)
        registerReceiver(mBroadcastReceiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        handleIntent(intent)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        unregisterReceiver(mBroadcastReceiver)
        zqPlayer?.stop()
        super.onDestroy()
    }

    //匿名内部类
    private var mBroadcastReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            handleIntent(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind")
        return ZQPlayerBind(this)
    }

    class ZQPlayerBind() : ZQPlayerServiceBinder.Stub() {

        private var mService: WeakReference<ZQPlayerService>? = null

        constructor(service: ZQPlayerService) :this(){
            mService = WeakReference(service)
        }

        override fun initPlayer(info: PlayerItemInfo?, surface: Surface?) {
            mService?.get()?.initPlayer(info, surface)
        }

    }

    // 功能
    fun handleIntent(intent: Intent?): Unit {
        val action = intent?.action
        when(action){
//            PLAY_CMD -> play()
//            REMOVE_NOTIFICATION_CMD ->{
//                mMediaPlayer.stop()
//                mMusicNotification?.removeNotification()
//            }
        }
    }


    var zqPlayer:ZQPlayer? = null
    var playerItemInfo: PlayerItemInfo? = null

    fun initPlayer(info: PlayerItemInfo?, surface: Surface?): Unit {
        Log.d(TAG, info?.url)
        playerItemInfo = info
        zqPlayer = ZQPlayer()
        zqPlayer?.setStatusListener(object : ZQPlayer.StatusListener {
            override fun onLoading() {
                Log.d(TAG, "onLoading")
            }

            override fun onPrepareFinished() {
                Log.d(TAG, "onPrepareFinished")
                zqPlayer?.start()
            }

            override fun onPlaying() {
                Log.d(TAG, "onPlaying")
            }
            override fun onPause() {
                Log.d(TAG, "onPause")
            }
        })
        zqPlayer?.prepare(playerItemInfo?.url!!)
        zqPlayer?.setSurfsce(surface)
    }




}