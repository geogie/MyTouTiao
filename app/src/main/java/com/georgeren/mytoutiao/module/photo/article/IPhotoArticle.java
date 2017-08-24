package com.georgeren.mytoutiao.module.photo.article;

import com.georgeren.mytoutiao.bean.photo.PhotoArticleBean;
import com.georgeren.mytoutiao.module.base.IBaseListView;
import com.georgeren.mytoutiao.module.base.IBasePresenter;

import java.util.List;

/**
 * Created by georgeRen on 2017/8/23.
 */

public interface IPhotoArticle {

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
        void doLoadData(String... category);

        /**
         * 再起请求数据
         */
        void doLoadMoreData();

        /**
         * 设置适配器
         */
        void doSetAdapter(List<PhotoArticleBean.DataBean> dataBeen);

        void doShowNoMore();
    }

}
