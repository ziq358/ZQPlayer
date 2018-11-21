package com.zq.zqplayer.service;

import com.zq.zqplayer.model.PandaTvDataBean;
import com.zq.zqplayer.model.VideoHttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * author: wuyanqiang
 * 2018/11/19
 */
public interface VideoService {

    @GET("ajax_get_live_list_by_cate")
    Observable<VideoHttpResult<PandaTvDataBean>> getVideList(@Query("cate") String cate,
                                                             @Query("pageno") int pageno,
                                                             @Query("pagenum") int pagenum,
                                                             @Query("room") int room,
                                                             @Query("version") String version);

}
