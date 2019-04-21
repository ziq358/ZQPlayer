package com.zq.zqplayer.mvp.main.contract

import com.ziq.base.mvp.IBaseView
import com.zq.zqplayer.bean.LiveItemDetailBean
import com.zq.zqplayer.bean.LiveListItemBean

interface RecommendContract {

    interface View : IBaseView {
        fun setData(items:List<LiveListItemBean>)
        fun onGetVideoUrlSuccessful(detailBean: LiveItemDetailBean)
        fun showMessage(msg:String?)
    }
}