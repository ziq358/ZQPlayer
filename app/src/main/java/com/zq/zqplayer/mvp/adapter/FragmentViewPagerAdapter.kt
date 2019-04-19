package com.zq.zqplayer.mvp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FragmentViewPagerAdapter(var dataList: List<Data>, fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return dataList[position].fragment
        }

        override fun getCount(): Int {
            return dataList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return dataList[position].title
        }

    class Data{
        var title:String
        var fragment: Fragment

        constructor(title: String, fragment: Fragment) {
            this.title = title
            this.fragment = fragment
        }
    }


}