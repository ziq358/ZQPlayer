package com.zq.zqplayer.fragment.topnavigation

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import butterknife.BindView
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.ziq.base.mvp.BaseFragment
import com.ziq.base.mvp.IBasePresenter
import com.zq.zqplayer.R
import com.zq.zqplayer.adapter.RecommendAdapter
import com.zq.zqplayer.model.RecommendBannerMultiItem
import com.zq.zqplayer.model.RecommendLiveItemMultiItem



/**
 * @author wuyanqiang
 * @date 2018/10/15
 */
class RecommendFragment : BaseFragment<IBasePresenter>() {

    @BindView(R.id.recycleView)
    lateinit var recycleView: RecyclerView;

    override fun initLayoutResourceId(): Int {
        return R.layout.fragment_recommend
    }

    override fun initData(view: View, savedInstanceState: Bundle?) {

        val data:ArrayList<MultiItemEntity> = arrayListOf();
        data.add(MultiItemEntity { RecommendAdapter.RECOMMEND_TYPE_SEARCH })

        val recommendBannerList:ArrayList<Int> = arrayListOf()
        recommendBannerList.add(R.drawable.home_recommend_live_app_1523155786)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1526869950)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1530243925)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1540959428)
        data.add(RecommendBannerMultiItem(recommendBannerList))

        data.add(MultiItemEntity { RecommendAdapter.RECOMMEND_TYPE_ALL_LIVE_TITLE })
        data.addAll(getLiveData())

        val adapter = RecommendAdapter(data)
        recycleView.adapter = adapter // 要先设置 adapter  SpanSizeLookup才有效
        var layoutManager:GridLayoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item:MultiItemEntity = data[position]
                return when(item.itemType){
                    RecommendAdapter.RECOMMEND_TYPE_SEARCH,
                    RecommendAdapter.RECOMMEND_TYPE_BANNER,
                    RecommendAdapter.RECOMMEND_TYPE_ALL_LIVE_TITLE ->{
                        2
                    }
                    else ->{
                        1
                    }
                }
            }

        }
        recycleView.layoutManager = layoutManager
        adapter.mOnActionListener = object : RecommendAdapter.OnActionListener {
            override fun onLiveItemClick(liveItemMultiItem: RecommendLiveItemMultiItem) {
                Toast.makeText(context, ""+liveItemMultiItem.title, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLiveData(): List<RecommendLiveItemMultiItem> {
        val data:ArrayList<RecommendLiveItemMultiItem> = arrayListOf()

        data.add(RecommendLiveItemMultiItem("100胜率上砖石已上9个共...", "android.resource://" + context!!.packageName + "/" + R.raw.pic_1))
        data.add(RecommendLiveItemMultiItem("世界锦标赛秋季赛-Day1...", "android.resource://" + context!!.packageName + "/" + R.raw.pic_2))
        data.add(RecommendLiveItemMultiItem("哇，妲己也能打野？", "android.resource://" + context!!.packageName + "/" + R.raw.pic_3))
        data.add(RecommendLiveItemMultiItem("奇老板的早间上分砖石至...", "android.resource://" + context!!.packageName + "/" + R.raw.pic_4))
        data.add(RecommendLiveItemMultiItem("晚上好", "android.resource://" + context!!.packageName + "/" + R.raw.pic_5))
        data.add(RecommendLiveItemMultiItem("小苍车队：888现金红包送...", "android.resource://" + context!!.packageName + "/" + R.raw.pic_6))
        data.add(RecommendLiveItemMultiItem("震惊！尺帝 VS Blank", "android.resource://" + context!!.packageName + "/" + R.raw.pic_7))
        data.add(RecommendLiveItemMultiItem("【大号冲分】输了发10000...", "android.resource://" + context!!.packageName + "/" + R.raw.pic_8))
        data.add(RecommendLiveItemMultiItem("峡谷之巅单排第一剑圣", "android.resource://" + context!!.packageName + "/" + R.raw.pic_9))
        data.add(RecommendLiveItemMultiItem("岚切千钰3000米外秒杀！...", "android.resource://" + context!!.packageName + "/" + R.raw.pic_10))

        return data
    }

}