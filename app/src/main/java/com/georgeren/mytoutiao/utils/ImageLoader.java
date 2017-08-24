package com.georgeren.mytoutiao.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.georgeren.mytoutiao.Constant;

/**
 * Created by georgeRen on 2017/8/21.
 */

public class ImageLoader {
    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param view
     * @param defaultResId
     */
    public static void loadCenterCrop(Context context, String url, ImageView view, int defaultResId) {
        if (SettingUtil.getInstance().getIsNoPhotoMode() && NetWorkUtil.isMobileConnected(context)) {
            view.setImageResource(defaultResId);
        } else {
            Glide.with(context).load(url).crossFade().centerCrop().into(view);
        }
    }

    /**
     * 带加载异常图片
     *
     * @param context
     * @param url
     * @param view
     * @param defaultResId
     * @param errorResId
     */
    public static void loadCenterCrop(Context context, String url, ImageView view, int defaultResId, int errorResId) {
        if (SettingUtil.getInstance().getIsNoPhotoMode() && NetWorkUtil.isMobileConnected(context)) {
            view.setImageResource(defaultResId);
        } else {
            Glide.with(context).load(url).crossFade().centerCrop().error(errorResId).into(view);
        }
    }

    /**
     * 带监听处理
     */
    public static void loadCenterCrop(Context context, String url, ImageView view, RequestListener listener) {
        Glide.with(context).load(url).crossFade().centerCrop().listener(listener).into(view);
    }

    /**
     * path 确保是 http 开头的，否则加上头部 https
     * @param path
     * @return
     */
    public static String path2Url(String path) {
        if (path.startsWith(Constant.HTTP1)) {
            return path;
        } else {
            return Constant.HTTPS + path;
        }
    }
}
