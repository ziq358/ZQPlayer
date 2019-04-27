package com.zq.zqplayer.util

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.ziq.base.utils.SharePreferenceUtil
import com.zq.zqplayer.ZQPlayerApplication
import com.zq.zqplayer.bean.UserInfoBean
import com.zq.zqplayer.common.Constants

class UserInfoUtil {
    companion object {
        private var sUserInfoBean: UserInfoBean? = null

        fun getUserInfo(): UserInfoBean? {
            if(sUserInfoBean == null){
                synchronized(UserInfoUtil::class.java) {
                    if (sUserInfoBean == null) {
                        sUserInfoBean = getUserInfoFromSP()
                    }
                }
            }
            return sUserInfoBean
        }

        fun saveToSP(bean: UserInfoBean) {
            sUserInfoBean = bean
            SharePreferenceUtil.save(ZQPlayerApplication.sApplication, Constants.SP_KEY_USER_INFO, Gson().toJson(bean))
        }

        private fun getUserInfoFromSP(): UserInfoBean? {
            val value = SharePreferenceUtil.get(ZQPlayerApplication.sApplication, Constants.SP_KEY_USER_INFO, "") as String
            return if (TextUtils.isEmpty(value)) {
                null
            } else Gson().fromJson(value, UserInfoBean::class.java)
        }

        fun cleanSP() {
            sUserInfoBean = null
            SharePreferenceUtil.remove(ZQPlayerApplication.sApplication, Constants.SP_KEY_USER_INFO)
        }
    }



}