package com.zq.zqplayer.mvp.live.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseFragment
import com.ziq.base.recycleview.BaseViewHolder
import com.ziq.base.recycleview.adapter.ListRecyclerAdapter
import com.zq.zqplayer.R

/**
 * @author wuyanqiang
 * @date 2018/10/15
 */
class ChatFragment : MvpBaseFragment<IBasePresenter>() {

    @BindView(R.id.recycleView)
    lateinit var recycleView: RecyclerView

    override fun initForInject(appComponent: AppComponent?) {
    }
    override fun initLayoutResourceId(): Int {
        return R.layout.fragment_chat
    }

    override fun initData(view: View, savedInstanceState: Bundle?) {
        var dataList: ArrayList<Data> = arrayListOf()

        dataList.add(Data("虎酱酱", "送礼物 2"))
        dataList.add(Data("YY900...", "送礼物 2"))
        dataList.add(Data("monry", "ez改了？"))
        dataList.add(Data("从你的全世界路过", "从你的全世界路过 2"))
        dataList.add(Data("小闹腾", "送礼物 2"))
        dataList.add(Data("我知道啊", "送礼物 2"))
        dataList.add(Data("皮一皮十年少", "6666"))
        dataList.add(Data("心随", "6666"))

        var adapter: ChatListAdapter = ChatListAdapter(context,dataList)
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.adapter = adapter
    }

    class Data(var name:String, var content:String){
    }

    class ChatListAdapter(context: Context?, data: MutableList<Data>?) : ListRecyclerAdapter<Data>(context, data) {
        override fun getItemLayoutRes(): Int {
            return R.layout.item_chat
        }

        override fun bindDataViewHolder(holder: BaseViewHolder?, position: Int) {
            holder?.getViewById<TextView>(R.id.name)?.text = getItem(position).name
            holder?.getViewById<TextView>(R.id.content)?.text = getItem(position).content
        }
    }

}