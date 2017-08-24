package com.georgeren.mytoutiao.module.news.article;

import com.georgeren.mytoutiao.bean.news.MultiNewsArticleDataBean;
import com.georgeren.mytoutiao.module.base.IBaseListView;
import com.georgeren.mytoutiao.module.base.IBasePresenter;

import java.util.List;

/**
 * Created by georgeRen on 2017/8/21.
 * mvp模式：View+Presenter
 */

public interface INewsArticle {
    interface View extends IBaseListView<Presenter> {

        /**
         * 请求数据
         */
        void onLoadData();

        /**
         * 刷新
         */
        void onRefresh();
    }

    interface Presenter extends IBasePresenter {

        /**
         * 请求数据
         */
        void doLoadData(String... category);

        /**
         * 再起请求数据
         */
        void doLoadMoreData();

        /**
         * 设置适配器
         */
        void doSetAdapter(List<MultiNewsArticleDataBean> dataBeen);

        /**
         * 加载完毕
         */
        void doShowNoMore();
    }
}
