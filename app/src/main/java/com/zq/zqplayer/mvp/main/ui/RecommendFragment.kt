package com.zq.zqplayer.mvp.main.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.bumptech.glide.Glide
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.baserx.dagger.module.LifecycleProviderModule
import com.ziq.base.mvp.MvpBaseFragment
import com.zq.customviewlib.AutoRollViewPager
import com.zq.zqplayer.R
import com.zq.zqplayer.bean.RoomInfoBean
import com.zq.zqplayer.mvp.adapter.RecommendAdapter
import com.zq.zqplayer.mvp.live.ui.LiveActivity
import com.zq.zqplayer.mvp.main.contract.RecommendContract
import com.zq.zqplayer.mvp.main.dagger.component.DaggerRecommendComponent
import com.zq.zqplayer.mvp.main.dagger.module.RecommendModule
import com.zq.zqplayer.mvp.main.presenter.RecommendPresenter


/**
 * @author wuyanqiang
 * @date 2018/10/15
 */
class RecommendFragment : MvpBaseFragment<RecommendPresenter>(), RecommendContract.View {


    @BindView(R.id.smartRefreshLayout)
    lateinit var mSmartRefreshLayout: SmartRefreshLayout;
    @BindView(R.id.recycleView)
    lateinit var recycleView: RecyclerView;


    var data:ArrayList<RoomInfoBean> = arrayListOf()
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
        initBanner()
        adapter = RecommendAdapter(context,data)
        recycleView.adapter = adapter // 要先设置 adapter  SpanSizeLookup才有效
        val layoutManager: GridLayoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
        recycleView.layoutManager = layoutManager
        adapter!!.mOnActionListener = object : RecommendAdapter.OnActionListener {
            override fun onLiveItemClick(item: RoomInfoBean) {
                LiveActivity.openVideo(context, item.live_url, item.room_name)
            }
        }


        mSmartRefreshLayout.setOnRefreshListener { onRefreshLive() }
        mSmartRefreshLayout.setOnLoadMoreListener { onLoadMoreLive() }
        mSmartRefreshLayout.autoRefresh()
    }

    private fun initBanner() {
        val recommendBannerList:ArrayList<Int> = arrayListOf()
        recommendBannerList.add(R.drawable.home_recommend_live_app_1523155786)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1526869950)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1530243925)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1540959428)
        val autoRollViewPager: AutoRollViewPager = view?.findViewById(R.id.autoRollViewPager)!!
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


    private fun onRefreshLive(): Unit {
        mPresenter.getLiveList(true)
    }

    private fun onLoadMoreLive(): Unit {
        mPresenter.getLiveList(false)
    }

    override fun hideLoading() {
        super.hideLoading()
        mSmartRefreshLayout.finishRefresh()
        mSmartRefreshLayout.finishLoadMore()
    }

    override fun setData(items: List<RoomInfoBean>) {
        if (mSmartRefreshLayout.isRefreshing) {
            adapter?.setData(items)
            mSmartRefreshLayout.finishRefresh()
        } else {
            adapter?.addDataList(items)
            mSmartRefreshLayout.finishLoadMore()
        }
        mSmartRefreshLayout.isEnableLoadMore = !items.isEmpty()
    }

    override fun showMessage(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}