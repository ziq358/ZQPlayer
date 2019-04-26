package com.zq.zqplayer.http.service;

import com.zq.zqplayer.bean.LiveItemDetailBean;
import com.zq.zqplayer.bean.LiveListItemBean;
import com.zq.zqplayer.bean.UserInfoBean;
import com.zq.zqplayer.http.request.LoginRequest;
import com.zq.zqplayer.http.request.ZQPlayerVideoListRequest;
import com.zq.zqplayer.http.request.ZQPlayerVideoUrlRequest;
import com.zq.zqplayer.http.response.BaseResponse;
import com.zq.zqplayer.http.serviceapi.UserServiceApi;
import com.zq.zqplayer.http.serviceapi.VideoServiceApi;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * author: wuyanqiang
 * 2018/11/19
 */
public interface UserService {

    @POST(UserServiceApi.LOGIN)
    Observable<BaseResponse<UserInfoBean>> login(@Body LoginRequest request);


    @POST(UserServiceApi.REGISTER)
    Observable<BaseResponse<UserInfoBean>> register(@Body LoginRequest request);
}
