package com.zq.zqplayer.util

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.ziq.base.utils.SharePreferenceUtil
import com.zq.zqplayer.bean.UserInfoBean
import com.zq.zqplayer.common.Constants

class UserInfoUtil {
    companion object {
        var sUserInfoBean: UserInfoBean? = null

        fun getUserInfo(context:Context): UserInfoBean? {
            if(sUserInfoBean == null){
                synchronized(UserInfoUtil::class.java) {
                    if (sUserInfoBean == null) {
                        sUserInfoBean = getUserInfoFromSP(context)
                    }
                }
            }
            return sUserInfoBean
        }

        fun saveToSP(context:Context, bean: UserInfoBean) {
            sUserInfoBean = bean
            SharePreferenceUtil.save(context, Constants.SP_KEY_USER_INFO, Gson().toJson(bean))
        }

        private fun getUserInfoFromSP(context:Context): UserInfoBean? {
            val value = SharePreferenceUtil.get(context, Constants.SP_KEY_USER_INFO, "") as String
            return if (TextUtils.isEmpty(value)) {
                null
            } else Gson().fromJson(value, UserInfoBean::class.java)
        }
    }



}