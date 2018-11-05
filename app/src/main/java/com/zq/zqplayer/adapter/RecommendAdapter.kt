package com.zq.zqplayer.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zq.zqplayer.R

class RecommendAdapter : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {


    companion object {
        const val RECOMMEND_TYPE_SEARCH = 1
        const val RECOMMEND_TYPE_BANNER = 2
    }

    constructor(data: List<MultiItemEntity>?) : super(data){
        addItemType(RECOMMEND_TYPE_SEARCH, R.layout.item_recommend_search)
        addItemType(RECOMMEND_TYPE_BANNER, R.layout.item_recommend_banner)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        when(item!!.itemType){
            RECOMMEND_TYPE_SEARCH -> {

            }
            RECOMMEND_TYPE_BANNER -> {

            }
            else ->{

            }
        }
    }
}