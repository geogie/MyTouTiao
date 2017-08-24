package com.georgeren.mytoutiao.module.joke.comment;

import com.georgeren.mytoutiao.bean.joke.JokeCommentBean;
import com.georgeren.mytoutiao.module.base.IBaseListView;
import com.georgeren.mytoutiao.module.base.IBasePresenter;

import java.util.List;

/**
 * Created by georgeRen on 2017/8/22.
 */

public interface IJokeComment {
    interface View extends IBaseListView<Presenter> {

        /**
         * 请求数据
         */
        void onLoadData();
    }

    interface Presenter extends IBasePresenter {

        /**
         * 请求数据
         */
        void doLoadData(String... jokeId_Count);

        /**
         * 再起请求数据
         */
        void doLoadMoreData();

        /**
         * 设置适配器
         */
        void doSetAdapter(List<JokeCommentBean.DataBean.RecentCommentsBean> commentsBeanList);

        /**
         * 加载完毕
         */
        void doShowNoMore();
    }

}
