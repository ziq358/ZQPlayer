package com.zq.zqplayer.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zq.customviewlib.AutoRollViewPager
import com.zq.zqplayer.R
import com.zq.zqplayer.model.RecommendBannerMultiItem
import com.zq.zqplayer.model.RecommendLiveItemMultiItem
import com.zq.zqplayer.wiget.GlideRoundTransform


class RecommendAdapter : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {


    companion object {
        const val RECOMMEND_TYPE_SEARCH = 1
        const val RECOMMEND_TYPE_BANNER = 2
        const val RECOMMEND_TYPE_ALL_LIVE_TITLE = 3
        const val RECOMMEND_TYPE_LIVE_ITEM = 4
    }



    constructor(data: List<MultiItemEntity>?) : super(data){
        addItemType(RECOMMEND_TYPE_SEARCH, R.layout.item_recommend_search)
        addItemType(RECOMMEND_TYPE_BANNER, R.layout.item_recommend_banner)
        addItemType(RECOMMEND_TYPE_ALL_LIVE_TITLE, R.layout.item_recommend_all_live_title)
        addItemType(RECOMMEND_TYPE_LIVE_ITEM, R.layout.item_recommend_live_item)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        when(item!!.itemType){
            RECOMMEND_TYPE_SEARCH -> {
            }
            RECOMMEND_TYPE_BANNER -> {
                val recommendBannerMultiItem:RecommendBannerMultiItem = item as RecommendBannerMultiItem;
                val autoRollViewPager: AutoRollViewPager = helper!!.getView(R.id.autoRollViewPager);
                autoRollViewPager.adapter = object : AutoRollViewPager.RollViewPagerAdapter<Int>(recommendBannerMultiItem.urlList) {
                    override fun getItemLayoutRes(): Int {
                        return R.layout.item_in_recommend_banner
                    }

                    override fun onBindItemView(rootView: ViewGroup?, position: Int, realPosition: Int) {
                        val imageUrl:Int = getItem(realPosition)
                        val ivContent:ImageView = rootView!!.findViewById(R.id.iv_content);
                        Glide.with(rootView!!.context).load(imageUrl).into(ivContent)
                    }
                }
            }
            RECOMMEND_TYPE_ALL_LIVE_TITLE -> { }
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