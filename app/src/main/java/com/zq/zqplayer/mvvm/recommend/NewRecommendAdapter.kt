package com.zq.zqplayer.mvvm.recommend

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.zq.zqplayer.R
import com.zq.zqplayer.bean.LiveListItemBean
import com.zq.zqplayer.databinding.ItemNewRecommendLiveItemBinding
import androidx.annotation.NonNull




class NewRecommendAdapter : RecyclerView.Adapter<NewRecommendAdapter.ViewHolder> {

    var data: ArrayList<LiveListItemBean>? = null

    constructor(data: ArrayList<LiveListItemBean>?) : super() {
        this.data = data
    }



    override fun getItemCount(): Int {
        return if(data == null) 0 else data!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_new_recommend_live_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data?.get(position)
        holder.bind(item!!)
        holder.itemView.setOnClickListener {
            mOnActionListener?.onLiveItemClick(item)
        }
    }

    var mOnActionListener:OnActionListener? = null


    interface OnActionListener{
        fun onLiveItemClick(item: LiveListItemBean): Unit
    }


    class ViewHolder : RecyclerView.ViewHolder {
        var mBinding:ItemNewRecommendLiveItemBinding? = null

        constructor(view:View) : super(view) {
            mBinding = DataBindingUtil.bind( itemView)
        }

        fun bind(@NonNull itme: LiveListItemBean) {
            mBinding?.item = itme
        }
    }

}