package com.zq.zqplayer.http.service;

import com.zq.zqplayer.http.request.ZQPlayerVideoListRequest;
import com.zq.zqplayer.http.request.ZQPlayerVideoUrlRequest;
import com.zq.zqplayer.http.response.BaseResponse;
import com.zq.zqplayer.bean.LiveListItemBean;
import com.zq.zqplayer.bean.LiveItemDetailBean;
import com.zq.zqplayer.http.serviceapi.VideoServiceApi;

import java.util.List;
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
    Observable<BaseResponse<List<LiveListItemBean>>> getZQVideoList(@HeaderMap Map<String, String> headers, @Body ZQPlayerVideoListRequest zqPlayerVideoListRequest);

    @POST(VideoServiceApi.LIVE_LIST_ITEM)
    Observable<BaseResponse<LiveItemDetailBean>> getZQVideoListUrl(@HeaderMap Map<String, String> headers, @Body ZQPlayerVideoUrlRequest request);


}
