package com.georgeren.mytoutiao.module.news.content;

import com.georgeren.mytoutiao.bean.news.MultiNewsArticleDataBean;
import com.georgeren.mytoutiao.module.base.IBasePresenter;
import com.georgeren.mytoutiao.module.base.IBaseView;

/**
 * Created by georgeRen on 2017/8/22.
 */

public interface INewsContent {
    interface View extends IBaseView<Presenter> {

        /**
         * 加载网页
         */
        void onSetWebView(String url, boolean flag);
    }

    interface Presenter extends IBasePresenter {

        /**
         * 请求数据
         */
        void doLoadData(MultiNewsArticleDataBean dataBean);
    }
}
