package com.zq.zqplayer.mvp.main.contract

import com.ziq.base.mvp.IBaseView
import com.zq.zqplayer.bean.ZQPlayerVideoListItemBean

interface RecommendContract {

    interface View : IBaseView {
        fun setData(items:List<ZQPlayerVideoListItemBean>)
        fun onGetVideoUrlSuccessful(url:String, title:String)
        fun showMessage(msg:String?)
    }
}