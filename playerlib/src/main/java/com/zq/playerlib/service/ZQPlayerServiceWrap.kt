package com.zq.playerlib.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.view.Surface

class ZQPlayerServiceWrap {


    var context: Context? = null
    var playeriteminfo: PlayerItemInfo? = null
    var surface : Surface? = null
    var listener: StatusListener? =null
    var serviceBinder: ZQPlayerServiceBinder? = null
    var serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceBinder = ZQPlayerServiceBinder.Stub.asInterface(service)
            serviceBinder?.initPlayer(playeriteminfo, surface, listener)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBinder = null
        }
    }

    constructor(context: Context){
        this.context = context
    }

    fun init(playeriteminfo: PlayerItemInfo, surface : Surface, listener: StatusListener): Unit {
        this.playeriteminfo = playeriteminfo
        this.surface = surface
        this.listener = listener
        ZQPlayerService.startZQPlayerService(context!!)
        ZQPlayerService.bindZQPlayerService(context!!, serviceConnection)
    }

    fun play(): Unit {
        context?.sendBroadcast(Intent(ZQPlayerService.PLAY_CMD))
    }

    fun pause(): Unit {
        context?.sendBroadcast(Intent(ZQPlayerService.PAUSE_CMD))
    }

    fun stop(): Unit {
        context?.sendBroadcast(Intent(ZQPlayerService.STOP_CMD))
    }

    fun destroy(): Unit {
        ZQPlayerService.unbindZQPlayerService(context!!, serviceConnection)
    }

    fun isPlaying():Boolean{
        if(serviceBinder != null){
            return serviceBinder?.isPlaying!!
        }
        return false
    }

    fun showFloatingWindow(): Unit {
        serviceBinder?.showFloatingWindow()
    }

}