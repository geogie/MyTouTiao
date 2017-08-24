package com.georgeren.mytoutiao.api;

import com.georgeren.mytoutiao.bean.video.VideoContentBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by georgeRen on 2017/8/22.
 * video的api
 */

public interface IVideoApi {
    /**
     *
     * 获取视频信息
     * Api 生成较复杂 详情查看 {@linkplain com.georgeren.mytoutiao.module.video.content.VideoContentPresenter#doLoadVideoData(String)}
     * http://ib.365yg.com/video/urls/v/1/toutiao/mp4/视频ID?r=17位随机数&s=加密结果
     */
    @GET
    Observable<VideoContentBean> getVideoContent(@Url String url);
}
