package com.georgeren.mytoutiao.module.video.content;

import com.georgeren.mytoutiao.module.news.comment.INewsComment;

/**
 * Created by georgeRen on 2017/8/22.
 */

public interface IVideoContent {
    interface View extends INewsComment.View {

        /**
         * 设置播放器
         */
        void onSetVideoPlay(String url);
    }

    interface Presenter extends INewsComment.Presenter {

        /**
         * 请求数据
         */
        void doLoadVideoData(String videoid);
    }
}
