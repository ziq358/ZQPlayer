package com.zq.zqplayer.mvp.adapter

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.ziq.base.glide.GlideRoundTransform
import com.zq.customviewlib.AutoRollViewPager
import com.zq.zqplayer.R
import com.zq.zqplayer.bean.LiveItemMultiItemEntity
import com.zq.zqplayer.bean.LiveListItemBean
import com.zq.zqplayer.common.Constants.Companion.MULTI_ITEM_TYPE_BANNER
import com.zq.zqplayer.common.Constants.Companion.MULTI_ITEM_TYPE_LIVE_ITEM
import com.zq.zqplayer.common.Constants.Companion.MULTI_ITEM_TYPE_TITLE


class RecommendAdapter : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {



    constructor(data: List<MultiItemEntity>?) : super(data){
        addItemType(MULTI_ITEM_TYPE_BANNER, R.layout.item_recommend_banner)
        addItemType(MULTI_ITEM_TYPE_TITLE, R.layout.item_recommend_all_live_title)
        addItemType(MULTI_ITEM_TYPE_LIVE_ITEM, R.layout.item_recommend_live_item)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        when(item!!.itemType){
            MULTI_ITEM_TYPE_BANNER ->{
                convertBanner(helper, item)
            }
            MULTI_ITEM_TYPE_TITLE ->{}
            MULTI_ITEM_TYPE_LIVE_ITEM ->{
                convertLiveItem(helper, item)
            }
        }
    }

    private fun convertBanner(helper: BaseViewHolder?, item: MultiItemEntity?) {
        val recommendBannerList:ArrayList<Int> = arrayListOf()
        recommendBannerList.add(R.drawable.home_recommend_live_app_1523155786)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1526869950)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1530243925)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1540959428)
        val autoRollViewPager: AutoRollViewPager = helper!!.getView(R.id.autoRollViewPager)
        autoRollViewPager.adapter = object : AutoRollViewPager.RollViewPagerAdapter<Int>(recommendBannerList) {
            override fun getItemLayoutRes(): Int {
                return R.layout.item_in_recommend_banner
            }

            override fun onBindItemView(rootView: ViewGroup?, position: Int, realPosition: Int) {
                val imageUrl:Int = getItem(realPosition)
                val ivContent: ImageView = rootView!!.findViewById(R.id.iv_content);
                Glide.with(rootView.context).load(imageUrl).into(ivContent)
            }
        }
        autoRollViewPager.startRoll()
    }

    private fun convertLiveItem(helper: BaseViewHolder?, item: MultiItemEntity?) {
        if(item is LiveItemMultiItemEntity){
            helper!!.setText(R.id.tv_title, item.listItemBean?.live_title)
            val ivCover: ImageView = helper!!.getView(R.id.iv_cover)
            val requestOptions: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_picture_default_bg)
                    .error(R.drawable.ic_picture_default_bg)
                    .transform(GlideRoundTransform(5))
            Glide.with(ivCover.context)
                    .load(item.listItemBean?.live_img)
                    .apply(requestOptions)
                    .into(ivCover)
            helper.itemView.setOnClickListener {
                mOnActionListener?.onLiveItemClick(item.listItemBean!!)
            }
        }
    }

    var mOnActionListener:OnActionListener? = null
    interface OnActionListener{
        fun onLiveItemClick(item: LiveListItemBean): Unit
    }

}