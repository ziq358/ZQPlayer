package com.zq.zqplayer

import android.graphics.Color
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.ziq.base.mvp.BaseApplication

/**
 *author: wuyanqiang
 *2018/11/20
 */
class ZQPlayerApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            val header = ClassicsHeader(context)
            header.setTextSizeTitle(14f)
            header.setTextSizeTime(12f)
            header.setPrimaryColor(Color.WHITE)
            header
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            val footer = ClassicsFooter(context)
            footer.setTextSizeTitle(14f)
            footer.setPrimaryColor(Color.WHITE)
            footer
        }
    }

}