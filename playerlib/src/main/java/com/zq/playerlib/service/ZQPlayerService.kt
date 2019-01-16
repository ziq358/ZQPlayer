package com.zq.playerlib.service

import android.app.Service
import android.content.*
import android.os.IBinder
import android.util.Log
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

        fun bindZQPlayerService(context: Context, serviceConnection: ServiceConnection) {
            //need to start service first
            val intent = Intent(context, ZQPlayerService::class.java)
            context.bindService(intent, serviceConnection, 0)
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

        override fun play() {
            mService?.get()?.play()
        }

        override fun setPlayItemInfo(info: PlayerItemInfo?) {
            mService?.get()?.setPlayItemInfo(info)
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


    fun play(): Unit {
        Log.d(TAG, "play")
    }

    fun setPlayItemInfo(info: PlayerItemInfo?): Unit {
        Log.d(TAG, info?.url)
    }



}