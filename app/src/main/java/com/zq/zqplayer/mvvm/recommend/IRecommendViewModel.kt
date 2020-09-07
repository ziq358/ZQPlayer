package com.zq.zqplayer.mvvm.recommend

import androidx.lifecycle.MutableLiveData
import com.zq.zqplayer.bean.RoomInfoBean

interface IRecommendViewModel {
    val toastMsg: MutableLiveData<String>
    val isLoading: MutableLiveData<Boolean>
    val onRefreshItems: MutableLiveData<ArrayList<RoomInfoBean>>
    val onLoadMoreItems: MutableLiveData<ArrayList<RoomInfoBean>>
    fun getZqVideoList(isRefresh:Boolean)
}