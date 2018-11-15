package com.zq.zqplayer.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zq.zqplayer.adapter.RecommendAdapter

class RecommendLiveItemMultiItem:MultiItemEntity {

    public var title:String
    public var coverUrl:String

    constructor(title:String, coverUrl:String) {
        this.title = title
        this.coverUrl = coverUrl
    }

    override fun getItemType(): Int {
        return RecommendAdapter.RECOMMEND_TYPE_LIVE_ITEM
    }



}