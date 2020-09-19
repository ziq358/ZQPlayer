package com.zq.zqplayer.test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ziq.base.recycleview.BaseViewHolder
import com.ziq.base.recycleview.adapter.ListRecyclerAdapter
import com.zq.zqplayer.R
import com.zq.zqplayer.mvp.live.ui.ChatFragment

class TestListActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_list)
        val testListRV = findViewById<RecyclerView>(R.id.test_list)
        testListRV.layoutManager = LinearLayoutManager(this)

        var dataList: ArrayList<Data> = arrayListOf()

        dataList.add(Data("OpenGL2Activity", OpenGL2Activity::class.java))
        dataList.add(Data("ZQPlayerServiceTestActivity", ZQPlayerServiceTestActivity::class.java))
        dataList.add(Data("PushVideoActivity", PushVideoActivity::class.java))
        testListRV.adapter = TestListAdapter(this, dataList)
    }

    class Data(var title:String, var target:Class<*>){}

    class TestListAdapter(context: Context?, data: MutableList<Data>?) : ListRecyclerAdapter<Data>(context, data) {
        override fun getItemLayoutRes(): Int {
            return R.layout.item_test
        }

        override fun bindDataViewHolder(holder: BaseViewHolder?, position: Int) {
            holder?.getViewById<TextView>(R.id.title)?.text = getItem(position).title
            holder?.itemView?.setOnClickListener {
                holder?.itemView?.context?.startActivity(Intent(holder.itemView.context, getItem(position).target))
            }
        }
    }
}