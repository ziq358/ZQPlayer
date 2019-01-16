// ZQPlayerServiceBinder.aidl
package com.zq.playerlib.service;

//PlayerItemInfo.aidl 的路径 要与  MusicInfo实现类的 路径要一致
import com.zq.playerlib.service.PlayerItemInfo;

interface ZQPlayerServiceBinder {
    void play();
    void setPlayItemInfo(in PlayerItemInfo info);
}
