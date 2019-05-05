package com.zq.zqplayer.mvvm.recommend

import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.zq.zqplayer.bean.LiveItemDetailBean
import com.zq.zqplayer.bean.LiveListItemBean
import com.zq.zqplayer.bean.UserInfoBean

interface IRecommendViewModel {
    val toastMsg: MutableLiveData<String>
    val isLoading: MutableLiveData<Boolean>
    val onRefreshItems: MutableLiveData<ArrayList<LiveListItemBean>>
    val onLoadMoreItems: MutableLiveData<ArrayList<LiveListItemBean>>
    val onDetailClick: MutableLiveData<LiveItemDetailBean>
    fun getZqVideoList(isRefresh:Boolean)
    fun getZqVideoUrl(roomId:String , live_type:String, game_type:String)
}