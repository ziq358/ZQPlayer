package com.zq.zqplayer.mvp.main.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.baserx.dagger.module.LifecycleProviderModule
import com.ziq.base.mvp.MvpBaseFragment
import com.zq.customviewlib.AutoRollViewPager
import com.zq.zqplayer.R
import com.zq.zqplayer.bean.*
import com.zq.zqplayer.mvp.adapter.RecommendAdapter
import com.zq.zqplayer.mvp.live.ui.LiveActivity
import com.zq.zqplayer.mvp.main.contract.RecommendContract
import com.zq.zqplayer.mvp.main.dagger.component.DaggerRecommendComponent
import com.zq.zqplayer.mvp.main.dagger.module.RecommendModule
import com.zq.zqplayer.mvp.main.presenter.RecommendPresenter
import java.io.*


/**
 * @author wuyanqiang
 * @date 2018/10/15
 */
class RecommendFragment : MvpBaseFragment<RecommendPresenter>(), RecommendContract.View {


    @BindView(R.id.smartRefreshLayout)
    lateinit var mSmartRefreshLayout: SmartRefreshLayout;
    @BindView(R.id.recycleView)
    lateinit var recycleView: RecyclerView;


    var data:ArrayList<MultiItemEntity> = arrayListOf()
    var adapter:RecommendAdapter? = null

    override fun initLayoutResourceId(): Int {
        return R.layout.fragment_recommend
    }

    override fun initForInject(appComponent: AppComponent?) {
        DaggerRecommendComponent.builder()
                .appComponent(appComponent)
                .lifecycleProviderModule(LifecycleProviderModule(this))
                .recommendModule(RecommendModule(this))
                .build().inject(this)
    }

    override fun initData(view: View, savedInstanceState: Bundle?) {


        data.add(BannerMultiItemEntity())
        data.add(TitleMultiItemEntity())
        adapter = RecommendAdapter(data)
        recycleView.adapter = adapter // 要先设置 adapter  SpanSizeLookup才有效
        val layoutManager: GridLayoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if(position == 0 || position == 1){
                    return 2
                }
                return 1
            }
        }
        recycleView.layoutManager = layoutManager
        adapter!!.mOnActionListener = object : RecommendAdapter.OnActionListener {
            override fun onLiveItemClick(item: LiveListItemBean) {
                mPresenter.getZqVideoUrl(item.live_id, item.live_type, item.game_type)
            }
        }


        mSmartRefreshLayout.setOnRefreshListener { onRefreshLive() }
        mSmartRefreshLayout.setOnLoadMoreListener { onLoadMoreLive() }
        mSmartRefreshLayout.autoRefresh()
    }


    private fun onRefreshLive(): Unit {
        mPresenter.getZqVideoList(true)
    }

    private fun onLoadMoreLive(): Unit {
        mPresenter.getZqVideoList(false)
    }

    override fun hideLoading() {
        super.hideLoading()
        mSmartRefreshLayout.finishRefresh()
        mSmartRefreshLayout.finishLoadMore()
    }

    override fun setData(items: List<LiveListItemBean>) {
        if (mSmartRefreshLayout.isRefreshing) {
            adapter!!.data.clear()
            adapter!!.data.add(BannerMultiItemEntity())
            adapter!!.data.add(TitleMultiItemEntity())
            mSmartRefreshLayout.finishRefresh()
        } else {
            mSmartRefreshLayout.finishLoadMore()
        }
        mSmartRefreshLayout.isEnableLoadMore = !items.isEmpty()
        var dataList:ArrayList<LiveItemMultiItemEntity> = arrayListOf<LiveItemMultiItemEntity>()
        for (itemBean in items){
            dataList.add(LiveItemMultiItemEntity(itemBean))
        }
        adapter!!.data.addAll(dataList)
        adapter!!.notifyDataSetChanged()
    }

    override fun onGetVideoUrlSuccessful(detailBean: LiveItemDetailBean) {
        if(detailBean.stream_list != null && !detailBean.stream_list.isEmpty()){
            var stream = detailBean.stream_list[0]
            LiveActivity.openVideo(context, stream.url, detailBean.live_title)
        }else{
            Toast.makeText(activity, "暂无直播源", Toast.LENGTH_SHORT).show()
        }
    }

    override fun showMessage(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}