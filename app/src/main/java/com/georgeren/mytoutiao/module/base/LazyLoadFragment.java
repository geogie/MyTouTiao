package com.georgeren.mytoutiao.module.base;

import android.os.Bundle;

/**
 * Created by georgeRen on 2017/8/19.
 * Fragment第二层封装：继承BaseFragment，给MVP占位
 * 懒加载Fragment
 */

public abstract class LazyLoadFragment <T extends IBasePresenter> extends BaseFragment<T>{
    private static final String TAG = "LazyLoadFragment";
    protected boolean isViewInitiated;// view是否已经初始化
    protected boolean isVisibleToUser;// 用户是否可见
    protected boolean isDataInitiated;// 数据是否初始化

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();// 拉取数据（网络数据）
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    /**
     * 拉取数据，具体下层实现
     */
    public abstract void fetchData();

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    /**
     * 更新数据
     * @param forceUpdate 是否强制：如果已经初始化数据了，强制更新数据否？，如果没有初始化数据，那么强制否，都会加载的。
     * @return
     */
    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }
}
