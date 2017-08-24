package com.georgeren.mytoutiao.module.base;

/**
 * Created by georgeRen on 2017/8/19.
 */

public interface IBasePresenter {
    /**
     * 刷新数据
     */
    void doRefresh();

    /**
     * 显示网络错误
     */
    void doShowNetError();
}
