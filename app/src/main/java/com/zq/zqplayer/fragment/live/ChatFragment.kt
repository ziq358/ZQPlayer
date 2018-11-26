package com.zq.zqplayer.fragment.live

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ziq.base.dagger.component.AppComponent
import com.ziq.base.mvp.BaseFragment
import com.ziq.base.mvp.IBasePresenter
import com.zq.zqplayer.R

/**
 * @author wuyanqiang
 * @date 2018/10/15
 */
class ChatFragment : BaseFragment<IBasePresenter>() {

    @BindView(R.id.recycleView)
    lateinit var recycleView: RecyclerView

    override fun initForInject(appComponent: AppComponent?) {
    }
    override fun initLayoutResourceId(): Int {
        return R.layout.fragment_chat
    }

    override fun initData(view: View, savedInstanceState: Bundle?) {
        var dataList: ArrayList<Data> = arrayListOf()

        dataList.add(Data("从你的全世界路过","从你的全世界路过 2"))

        var adapter:ChatListAdapter = ChatListAdapter(dataList)
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.adapter = adapter
    }

    class Data(var name:String, var content:String){
    }

    class ChatListAdapter:BaseQuickAdapter<Data, BaseViewHolder>{

        constructor(data: List<Data>?) : super(R.layout.item_chat, data){

        }

        override fun convert(helper: BaseViewHolder?, item: Data?) {
            helper?.setText(R.id.name, item?.name)
            helper?.setText(R.id.content, item?.content)
        }
    }

}