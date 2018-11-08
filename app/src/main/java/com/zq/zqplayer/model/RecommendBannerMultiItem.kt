package com.zq.zqplayer.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zq.zqplayer.adapter.RecommendAdapter

class RecommendBannerMultiItem:MultiItemEntity {

    var urlList:List<String>

    constructor(urlList: List<String>) {
        this.urlList = urlList
    }

    override fun getItemType(): Int {
        return RecommendAdapter.RECOMMEND_TYPE_BANNER
    }

    fun getBannerUrl(): List<String> {
        return urlList;
    }

}