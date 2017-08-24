package com.georgeren.mytoutiao.api;

import com.georgeren.mytoutiao.Constant;
import com.georgeren.mytoutiao.bean.news.NewsContentBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

/**
 * Created by georgeRen on 2017/8/21.
 */

public interface INewsApi {
    String HOST = "http://toutiao.com/";

    /**
     * 详情：获取新闻内容的API
     */
    @GET
    @Headers("User-Agent:" + Constant.USER_AGENT_MOBILE)
    Call<ResponseBody> getNewsContentRedirectUrl(@Url String url);

    /**
     * 详情：获取新闻HTML内容
     * http://m.toutiao.com/i6364969235889783298/info/
     */
    @GET
    Observable<NewsContentBean> getNewsContent(@Url String url);
}
