// ZQPlayerServiceBinder.aidl
package com.zq.playerlib.service;

//PlayerItemInfo.aidl 的路径 要与  PlayerItemInfo java实现类的 路径要一致
import com.zq.playerlib.service.PlayerItemInfo;

interface ZQPlayerServiceBinder {
    void initPlayer(in PlayerItemInfo info, in Surface surface);
}
