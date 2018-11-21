package com.zq.zqplayer.adapter

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zq.zqplayer.R
import com.zq.zqplayer.model.RecommendLiveItemMultiItem
import com.zq.zqplayer.wiget.GlideRoundTransform


class RecommendAdapter : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {


    companion object {
        const val RECOMMEND_TYPE_LIVE_ITEM = 4
    }



    constructor(data: List<MultiItemEntity>?) : super(data){
        addItemType(RECOMMEND_TYPE_LIVE_ITEM, R.layout.item_recommend_live_item)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        when(item!!.itemType){
            RECOMMEND_TYPE_LIVE_ITEM -> {
                val liveItemMultiItem:RecommendLiveItemMultiItem = item as RecommendLiveItemMultiItem;
                val tvTitle: TextView = helper!!.getView(R.id.tv_title)
                tvTitle.text = liveItemMultiItem.title

                val ivCover: ImageView = helper.getView(R.id.iv_cover)
                val requestOptions: RequestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_picture_default_bg)
                        .error(R.drawable.ic_picture_default_bg)
                        .transform(GlideRoundTransform(5))
                Log.e("ziq", liveItemMultiItem.coverUrl)
                Glide.with(ivCover.context)
                        .load(liveItemMultiItem.coverUrl)
                        .apply(requestOptions)
                        .into(ivCover)

                helper.itemView.setOnClickListener {
                    mOnActionListener?.onLiveItemClick(liveItemMultiItem)
                }

            }
            else ->{ }
        }
    }

    var mOnActionListener:OnActionListener? = null


    interface OnActionListener{
        fun onLiveItemClick(liveItemMultiItem:RecommendLiveItemMultiItem): Unit
    }

}