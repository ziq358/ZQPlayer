package com.zq.zqplayer.http.service;

import com.zq.zqplayer.http.request.ZQPlayerVideoListRequest;
import com.zq.zqplayer.http.request.ZQPlayerVideoUrlRequest;
import com.zq.zqplayer.http.response.BaseResponse;
import com.zq.zqplayer.bean.ZQPlayerVideoListItemBean;
import com.zq.zqplayer.bean.ZQPlayerVideoUrlBean;
import com.zq.zqplayer.http.serviceapi.VideoServiceApi;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * author: wuyanqiang
 * 2018/11/19
 */
public interface VideoService {

    @POST(VideoServiceApi.LIVE_LIST)
    Observable<BaseResponse<List<ZQPlayerVideoListItemBean>>> getZQVideoList(@Body ZQPlayerVideoListRequest zqPlayerVideoListRequest);

    @POST(VideoServiceApi.LIVE_LIST_ITEM)
    Observable<BaseResponse<ZQPlayerVideoUrlBean>> getZQVideoListUrl(@Body ZQPlayerVideoUrlRequest request);


}
