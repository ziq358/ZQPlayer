package com.zq.playerlib.service

import android.os.Parcel
import android.os.Parcelable

/**
 *@author wuyanqiang
 *@date 2019/1/16
 */
class PlayerItemInfo() : Parcelable {
    var url:String? = null

    constructor(parcel: Parcel) : this() {
        url = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayerItemInfo> {
        override fun createFromParcel(parcel: Parcel): PlayerItemInfo {
            return PlayerItemInfo(parcel)
        }

        override fun newArray(size: Int): Array<PlayerItemInfo?> {
            return arrayOfNulls(size)
        }
    }
}