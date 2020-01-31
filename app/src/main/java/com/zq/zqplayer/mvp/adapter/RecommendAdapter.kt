package com.zq.zqplayer.mvp.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ziq.base.glide.GlideRoundTransform
import com.ziq.base.recycleview.BaseViewHolder
import com.ziq.base.recycleview.adapter.ListRecyclerAdapter
import com.zq.zqplayer.R
import com.zq.zqplayer.bean.LiveListItemBean


class RecommendAdapter(context: Context?, data: MutableList<LiveListItemBean>?) : ListRecyclerAdapter<LiveListItemBean>(context, data) {

    override fun getItemLayoutRes(): Int {
        return R.layout.item_recommend_live_item
    }

    override fun bindDataViewHolder(holder: BaseViewHolder?, position: Int) {
        var listItemBean:LiveListItemBean = getItem(position)
        val tvTitle: TextView = holder!!.getViewById(R.id.tv_title)
        tvTitle.text = listItemBean.live_title
        val ivCover: ImageView = holder.getViewById(R.id.iv_cover)
        val requestOptions: RequestOptions = RequestOptions()
                .placeholder(R.drawable.ic_picture_default_bg)
                .error(R.drawable.ic_picture_default_bg)
                .transform(GlideRoundTransform(5))
        Glide.with(ivCover.context)
                .load(listItemBean.live_img)
                .apply(requestOptions)
                .into(ivCover)
        holder.rootView.setOnClickListener {
            mOnActionListener?.onLiveItemClick(listItemBean)
        }
    }

    var mOnActionListener:OnActionListener? = null
    interface OnActionListener{
        fun onLiveItemClick(item: LiveListItemBean): Unit
    }

}