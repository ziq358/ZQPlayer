package com.zq.zqplayer.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zq.zqplayer.common.Constants.Companion.MULTI_ITEM_TYPE_BANNER
import com.zq.zqplayer.common.Constants.Companion.MULTI_ITEM_TYPE_TITLE

class TitleMultiItemEntity : MultiItemEntity {
    override fun getItemType(): Int {
        return MULTI_ITEM_TYPE_TITLE
    }
}