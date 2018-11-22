package com.zq.zqplayer.adapter

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zq.zqplayer.R
import com.zq.zqplayer.model.PandaTvListItemBean
import com.zq.zqplayer.model.RecommendLiveItemMultiItem
import com.zq.zqplayer.wiget.GlideRoundTransform


class RecommendAdapter : BaseQuickAdapter<PandaTvListItemBean, BaseViewHolder> {



    companion object {
        const val RECOMMEND_TYPE_LIVE_ITEM = 4
    }



    constructor(data: List<PandaTvListItemBean>?) : super(R.layout.item_recommend_live_item, data){
    }

    override fun convert(helper: BaseViewHolder?, item: PandaTvListItemBean?) {
        val tvTitle: TextView = helper!!.getView(R.id.tv_title)
        tvTitle.text = item!!.name

        val ivCover: ImageView = helper.getView(R.id.iv_cover)
        val requestOptions: RequestOptions = RequestOptions()
                .placeholder(R.drawable.ic_picture_default_bg)
                .error(R.drawable.ic_picture_default_bg)
                .transform(GlideRoundTransform(5))
        Glide.with(ivCover.context)
                .load(item.pictures.img)
                .apply(requestOptions)
                .into(ivCover)

        helper.itemView.setOnClickListener {
            mOnActionListener?.onLiveItemClick(item)
        }
    }

    var mOnActionListener:OnActionListener? = null


    interface OnActionListener{
        fun onLiveItemClick(item:PandaTvListItemBean): Unit
    }

}