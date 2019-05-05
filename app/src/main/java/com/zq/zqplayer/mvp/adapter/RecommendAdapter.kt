package com.zq.zqplayer.mvp.adapter

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zq.zqplayer.R
import com.zq.zqplayer.bean.LiveListItemBean
import com.zq.zqplayer.wiget.GlideRoundTransform


class RecommendAdapter : BaseQuickAdapter<LiveListItemBean, BaseViewHolder> {

    constructor(data: List<LiveListItemBean>?) : super(R.layout.item_recommend_live_item, data){
    }

    override fun convert(helper: BaseViewHolder?, item: LiveListItemBean?) {
        val tvTitle: TextView = helper!!.getView(R.id.tv_title)
        tvTitle.text = item!!.live_title

        val ivCover: ImageView = helper.getView(R.id.iv_cover)
        val requestOptions: RequestOptions = RequestOptions()
                .placeholder(R.drawable.ic_picture_default_bg)
                .error(R.drawable.ic_picture_default_bg)
                .transform(GlideRoundTransform(5))
        Glide.with(ivCover.context)
                .load(item.live_img)
                .apply(requestOptions)
                .into(ivCover)

        helper.itemView.setOnClickListener {
            mOnActionListener?.onLiveItemClick(item)
        }
    }

    var mOnActionListener:OnActionListener? = null


    interface OnActionListener{
        fun onLiveItemClick(item: LiveListItemBean): Unit
    }

}