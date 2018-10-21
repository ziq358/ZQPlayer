package com.zq.zqplayer.model

import android.app.Fragment
import com.zq.customviewlib.HorizontalScrollViewTab
import com.zq.zqplayer.fragment.EmptyFragment

abstract class TopNavigationItemBean(title:String) :HorizontalScrollViewTab.ContentItem{

    var mTitle:String = title

    override fun getTitle(): String {
        return mTitle
    }

    abstract fun getFragment(): android.support.v4.app.Fragment

    companion object {
        fun getEmptyInstance(title:String): TopNavigationItemBean {
            return object : TopNavigationItemBean(title) {
                override fun getFragment(): android.support.v4.app.Fragment {
                    return EmptyFragment()
                }
            }
        }
    }
}