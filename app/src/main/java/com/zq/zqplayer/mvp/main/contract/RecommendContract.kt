package com.zq.zqplayer.mvp.main.contract

import com.ziq.base.mvp.IBaseView
import com.zq.zqplayer.bean.RoomInfoBean

interface RecommendContract {

    interface View : IBaseView {
        fun setData(items:List<RoomInfoBean>)
        fun showMessage(msg:String?)
    }
}