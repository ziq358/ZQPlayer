package com.zq.zqplayer.service;

import com.zq.zqplayer.model.request.ZQPlayerVideoListRequest;
import com.zq.zqplayer.model.request.ZQPlayerVideoUrlRequest;
import com.zq.zqplayer.model.response.BaseResponse;
import com.zq.zqplayer.model.response.ZQPlayerVideoListItemBean;
import com.zq.zqplayer.model.response.ZQPlayerVideoUrlBean;

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

    @POST()
    Observable<BaseResponse<List<ZQPlayerVideoListItemBean>>> getZQVideoList(@Url String url, @Body ZQPlayerVideoListRequest zqPlayerVideoListRequest);

    @POST()
    Observable<BaseResponse<ZQPlayerVideoUrlBean>> getZQVideoListUrl(@Url String url, @Body ZQPlayerVideoUrlRequest request);


}
