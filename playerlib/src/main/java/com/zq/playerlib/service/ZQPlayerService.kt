package com.zq.playerlib.service

import android.app.Service
import android.content.*
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams.*
import android.widget.TextView
import com.zq.playerlib.R
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
            mService?.get()?.showFloatingWindow(info)
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

    internal var floatingWindow: View? = null
    internal var windowManager: WindowManager? = null
    internal var wmParams: WindowManager.LayoutParams? = null
    internal var displayHeight: Int = 0
    internal var displayWidth: Int = 0
    internal var statusBarHeight: Int = 0
    internal var floatingWindowHeight: Int = 0
    internal var floatingWindowWidth: Int = 0


    internal var surfaceView: SurfaceView? = null



    private fun showFloatingWindow(info: PlayerItemInfo?) {
        if (floatingWindow == null) {

            displayHeight = DeviceInfoUtil.getDisplayHeight(applicationContext)
            displayWidth = DeviceInfoUtil.getDisplayWidth(applicationContext)
            statusBarHeight = DeviceInfoUtil.getStatusBarHeight(applicationContext)

            windowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wmParams = WindowManager.LayoutParams()
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1 && Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                wmParams!!.type = WindowManager.LayoutParams.TYPE_TOAST
            } else {
                wmParams!!.type = WindowManager.LayoutParams.TYPE_PHONE
            }
            wmParams!!.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            wmParams!!.flags = FLAG_NOT_TOUCH_MODAL or FLAG_NOT_FOCUSABLE or FLAG_LAYOUT_NO_LIMITS

            wmParams!!.gravity = Gravity.LEFT or Gravity.TOP

            wmParams!!.width = displayWidth /2
            wmParams!!.height = wmParams!!.width /16 * 9

            val inflater = LayoutInflater.from(application)
            floatingWindow = inflater.inflate(R.layout.layout_service_video, null)
            surfaceView = floatingWindow!!.findViewById(R.id.surfaceView)
            floatingWindow!!.setOnTouchListener(View.OnTouchListener { v, event ->
                // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标

                if (floatingWindowHeight == 0) {
                    floatingWindowWidth = floatingWindow!!.getMeasuredWidth()
                    floatingWindowHeight = floatingWindow!!.getMeasuredHeight()
                }

                var targetX = event.rawX.toInt() - floatingWindowWidth / 2
                var targetY = event.rawY.toInt() - floatingWindowHeight / 2 - statusBarHeight

                if (targetX <= 0) {
                    targetX = 0
                } else if (targetX + floatingWindowWidth >= displayWidth) {
                    targetX = displayWidth - floatingWindowWidth
                }
                if (targetY <= 0) {
                    targetY = 0
                } else if (targetY + floatingWindowHeight >= displayHeight) {
                    targetY = displayHeight - floatingWindowHeight - statusBarHeight
                }

                wmParams!!.x = targetX
                wmParams!!.y = targetY
                windowManager!!.updateViewLayout(floatingWindow, wmParams)// 刷新
                return@OnTouchListener false// 此处必须返回false，否则OnClickListener获取不到监听
            })
            windowManager!!.addView(floatingWindow, wmParams)

            surfaceView!!.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                    initPlayer(info, holder?.surface)
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    initPlayer(info, holder?.surface)
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {

                }
            })
        }
    }


}