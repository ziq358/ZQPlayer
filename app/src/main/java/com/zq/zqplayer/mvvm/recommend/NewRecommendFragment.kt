package com.zq.zqplayer.mvvm.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvvm.MvvmBaseFragment
import com.zq.customviewlib.AutoRollViewPager
import com.zq.zqplayer.R
import com.zq.zqplayer.bean.LiveItemDetailBean
import com.zq.zqplayer.bean.LiveListItemBean
import com.zq.zqplayer.databinding.FragmentNewRecommendBinding
import com.zq.zqplayer.mvp.live.ui.LiveActivity
import com.zq.zqplayer.mvvm.LifecycleProviderModule
import com.zq.zqplayer.mvvm.recommend.component.DaggerNewRecommendComponent
import com.zq.zqplayer.mvvm.recommend.component.NewRecommendModule
import javax.inject.Inject

class NewRecommendFragment: MvvmBaseFragment() {

    @Inject
    lateinit var mRecommendViewModel : IRecommendViewModel
    var binding:FragmentNewRecommendBinding? = null
    var adapter: NewRecommendAdapter? = null
    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewRecommendBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun initForInject(appComponent: AppComponent?) {
        DaggerNewRecommendComponent
                .builder()
                .appComponent(appComponent)
                .newRecommendModule(NewRecommendModule(this))
                .lifecycleProviderModule(LifecycleProviderModule(this))
                .build()
                .inject(this)
    }

    override fun initData(savedInstanceState: Bundle?) {
        binding?.viewmodel = mRecommendViewModel
        binding?.lifecycleOwner = this // 设置了这个， livedata 的变化才会应用到界面
        adapter = NewRecommendAdapter(ArrayList())
        initRecycleView()
        initListener()
        binding?.smartRefreshLayout?.autoRefresh()

        val recommendBannerList:ArrayList<Int> = arrayListOf()
        recommendBannerList.add(R.drawable.home_recommend_live_app_1523155786)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1526869950)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1530243925)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1540959428)
        binding?.banner?.autoRollViewPager!!.adapter = object : AutoRollViewPager.RollViewPagerAdapter<Int>(recommendBannerList) {
            override fun getItemLayoutRes(): Int {
                return R.layout.item_in_recommend_banner
            }

            override fun onBindItemView(rootView: ViewGroup?, position: Int, realPosition: Int) {
                val imageUrl:Int = getItem(realPosition)
                val ivContent: ImageView = rootView!!.findViewById(R.id.iv_content);
                Glide.with(rootView.context).load(imageUrl).into(ivContent)
            }
        }

    }

    fun initRecycleView(): Unit {
        binding?.recycleView?.adapter = adapter
        val layoutManager: GridLayoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
        binding?.recycleView?.layoutManager = layoutManager
        adapter!!.mOnActionListener = object : NewRecommendAdapter.OnActionListener {
            override fun onLiveItemClick(item: LiveListItemBean) {
                mRecommendViewModel.getZqVideoUrl(item.live_id, item.live_type, item.game_type)
            }
        }
    }

    fun initListener(): Unit {
        mRecommendViewModel.isLoading.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if(t!!){
                    showLoading()
                }else{
                    hideLoading()
                }
            }
        })

        mRecommendViewModel.toastMsg.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                Toast.makeText(context, t, Toast.LENGTH_LONG).show()
            }
        })

        mRecommendViewModel.onRefreshItems.observe(this, object : Observer<ArrayList<LiveListItemBean>> {
            override fun onChanged(t: ArrayList<LiveListItemBean>?) {
                adapter?.data?.clear()
                adapter?.data?.addAll(t!!)
                adapter?.notifyDataSetChanged()
            }
        })

        mRecommendViewModel.onLoadMoreItems.observe(this, object : Observer<ArrayList<LiveListItemBean>> {
            override fun onChanged(t: ArrayList<LiveListItemBean>?) {
                adapter?.data?.addAll(t!!)
                adapter?.notifyDataSetChanged()
            }
        })

        mRecommendViewModel.onDetailClick.observe(this, object : Observer<LiveItemDetailBean> {
            override fun onChanged(detailBean: LiveItemDetailBean?) {
                if(detailBean!!.stream_list != null && !detailBean.stream_list.isEmpty()){
                    var stream = detailBean!!.stream_list[0]
                    LiveActivity.openVideo(context, stream.url, detailBean.live_title)
                }else{
                    Toast.makeText(activity, "暂无直播源", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun hideLoading() {
        super.hideLoading()
        binding?.smartRefreshLayout?.finishRefresh()
        binding?.smartRefreshLayout?.finishLoadMore()
    }



}