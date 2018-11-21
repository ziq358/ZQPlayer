package com.zq.zqplayer.fragment.topnavigation

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.trello.rxlifecycle2.LifecycleTransformer
import com.ziq.base.dagger.component.AppComponent
import com.ziq.base.dagger.module.RxLifecycleModule
import com.ziq.base.mvp.BaseFragment
import com.ziq.base.mvp.IBasePresenter
import com.zq.customviewlib.AutoRollViewPager
import com.zq.zqplayer.R
import com.zq.zqplayer.activity.LiveActivity
import com.zq.zqplayer.adapter.RecommendAdapter
import com.zq.zqplayer.dagger.component.DaggerRecommendComponent
import com.zq.zqplayer.dagger.module.RecommendModule
import com.zq.zqplayer.model.RecommendLiveItemMultiItem
import com.zq.zqplayer.presenter.RecommendPresenter
import java.io.*


/**
 * @author wuyanqiang
 * @date 2018/10/15
 */
class RecommendFragment : BaseFragment<RecommendPresenter>(), RecommendPresenter.View {


    @BindView(R.id.smartRefreshLayout)
    lateinit var mSmartRefreshLayout: SmartRefreshLayout;
    @BindView(R.id.recycleView)
    lateinit var recycleView: RecyclerView;
    val data:ArrayList<MultiItemEntity> = arrayListOf()

    override fun initLayoutResourceId(): Int {
        return R.layout.fragment_recommend
    }
    override fun initForInject(appComponent: AppComponent?) {
        val lifecycleTransformer: LifecycleTransformer<Any> = this.bindToLifecycle()
        DaggerRecommendComponent.builder()
                .appComponent(appComponent)
                .rxLifecycleModule(RxLifecycleModule(lifecycleTransformer))
                .recommendModule(RecommendModule(this))
                .build().inject(this)
    }

    override fun initData(view: View, savedInstanceState: Bundle?) {

        val recommendBannerList:ArrayList<Int> = arrayListOf()
        recommendBannerList.add(R.drawable.home_recommend_live_app_1523155786)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1526869950)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1530243925)
        recommendBannerList.add(R.drawable.home_recommend_live_app_1540959428)
        val autoRollViewPager: AutoRollViewPager = view.findViewById(R.id.autoRollViewPager)
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

        val adapter = RecommendAdapter(data)
        recycleView.adapter = adapter // 要先设置 adapter  SpanSizeLookup才有效
        val layoutManager:GridLayoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }

        }
        recycleView.layoutManager = layoutManager
        adapter.mOnActionListener = object : RecommendAdapter.OnActionListener {
            override fun onLiveItemClick(liveItemMultiItem: RecommendLiveItemMultiItem) {
                LiveActivity.openVideo(context, getData())
            }
        }


        mSmartRefreshLayout.setOnRefreshListener { onRefreshLive() }
        mSmartRefreshLayout.setOnLoadMoreListener { onLoadMoreLive() }
    }


    fun onRefreshLive(): Unit {
        mPresenter.getVideo()
    }

    fun onLoadMoreLive(): Unit {
        mPresenter.getVideo()
    }

    override fun hideLoading() {
        super.hideLoading()
        mSmartRefreshLayout.finishRefresh()
        mSmartRefreshLayout.finishLoadMore()
    }

    override fun setData() {
        hideLoading()
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


    private fun getData():String {
        var videoPath: String  = getDataDirPath(context!!, "meidacodec") + File.separator + "gao_bai_qi_qiu.mp4"
        val `in` = BufferedInputStream(resources.openRawResource(R.raw.gao_bai_qi_qiu))
        val out: BufferedOutputStream
        try {
            val outputStream = FileOutputStream(videoPath)
            out = BufferedOutputStream(outputStream)
            val buf = ByteArray(1024)
            var size = `in`.read(buf)
            while (size > 0) {
                out.write(buf, 0, size)
                size = `in`.read(buf)
            }
            `in`.close()
            out.flush()
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return videoPath
    }

    fun getDataDirPath(context: Context, dir: String): String {
        val path = context.externalCacheDir!!.absolutePath + File.separator + dir
        val file = File(path)
        if (!file.exists()) {
            file.mkdir()
        }
        return path
    }

}