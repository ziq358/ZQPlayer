package com.zq.zqplayer.http.service;

import com.zq.zqplayer.bean.RoomInfoBean;
import com.zq.zqplayer.http.request.LiveRequest;
import com.zq.zqplayer.http.response.BaseResponse;
import com.zq.zqplayer.http.serviceapi.VideoServiceApi;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * author: wuyanqiang
 * 2018/11/19
 */
public interface VideoService {

    @POST(VideoServiceApi.LIVE_LIST)
    Observable<BaseResponse<ArrayList<RoomInfoBean>>> getLiveList(@HeaderMap Map<String, String> headers, @Body LiveRequest liveRequest);

}
