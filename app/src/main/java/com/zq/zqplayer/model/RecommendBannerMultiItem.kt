package com.zq.zqplayer.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zq.zqplayer.adapter.RecommendAdapter

class RecommendBannerMultiItem:MultiItemEntity {

    var urlList:List<Int>

    constructor(urlList: List<Int>) {
        this.urlList = urlList
    }

    override fun getItemType(): Int {
        return RecommendAdapter.RECOMMEND_TYPE_BANNER
    }

    fun getBannerUrl(): List<Int> {
        return urlList;
    }

}