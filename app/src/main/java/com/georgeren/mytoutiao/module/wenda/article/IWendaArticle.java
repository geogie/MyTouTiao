package com.georgeren.mytoutiao.module.wenda.article;

import com.georgeren.mytoutiao.bean.wenda.WendaArticleDataBean;
import com.georgeren.mytoutiao.module.base.IBaseListView;
import com.georgeren.mytoutiao.module.base.IBasePresenter;

import java.util.List;

/**
 * Created by georgeRen on 2017/8/21.
 */

public interface IWendaArticle {
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
        void doLoadData();

        /**
         * 再起请求数据
         */
        void doLoadMoreData();

        /**
         * 设置适配器
         */
        void doSetAdapter(List<WendaArticleDataBean> list);
    }
}
