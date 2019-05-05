package com.zq.zqplayer.mvvm.bindingadapter

import androidx.databinding.BindingAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.zq.zqplayer.mvvm.recommend.IRecommendViewModel

class SmartRefreshLayoutBindingAdapter {

    companion object {
        @BindingAdapter("app:onRefreshRecommend")
        @JvmStatic
        fun setRecommendOnRefreshListener(smartRefreshLayout: SmartRefreshLayout,
                                          viewModel: IRecommendViewModel):Unit {
            smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onLoadMore(refreshLayout: RefreshLayout?) {
                    viewModel.getZqVideoList(false)
                }

                override fun onRefresh(refreshLayout: RefreshLayout?) {
                    viewModel.getZqVideoList(true)
                }
            })
        }
    }

}