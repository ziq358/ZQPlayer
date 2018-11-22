package com.zq.zqplayer.model.request

/**
 *author: wuyanqiang
 *2018/11/22
 */
class BaseRequest {

    var pageNo = 1//当前页码，默认：1
    var pageSize = 20//一页显示数量，默认：20

    fun setLen(len: Int) {
        pageNo = len / pageSize + 1
        if (len % pageSize != 0) {
            pageNo++
        }
    }

}