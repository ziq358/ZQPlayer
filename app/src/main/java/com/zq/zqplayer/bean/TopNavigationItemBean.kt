package com.zq.zqplayer.bean

import androidx.fragment.app.Fragment
import com.zq.customviewlib.HorizontalScrollViewTab
import com.zq.zqplayer.mvp.main.ui.EmptyFragment

abstract class TopNavigationItemBean(title:String) :HorizontalScrollViewTab.ContentItem{

    var mTitle:String = title

    override fun getTitle(): String {
        return mTitle
    }

    abstract fun getFragment(): Fragment

    companion object {
        fun getEmptyInstance(title:String): TopNavigationItemBean {
            return object : TopNavigationItemBean(title) {
                override fun getFragment(): Fragment {
                    return EmptyFragment()
                }
            }
        }
    }
}