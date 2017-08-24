package com.georgeren.mytoutiao.module.photo.content;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.georgeren.mytoutiao.InitApp;
import com.georgeren.mytoutiao.R;
import com.georgeren.mytoutiao.bean.photo.PhotoArticleBean;
import com.georgeren.mytoutiao.module.base.BaseActivity;
import com.r0adkll.slidr.model.SlidrInterface;

/**
 * Created by georgeRen on 2017/8/23.
 * 图片详情：
 */

public class PhotoContentActivity extends BaseActivity {
    private static final String TAG = "PhotoContentActivity";

    public static void launch(PhotoArticleBean.DataBean bean) {
        InitApp.AppContext.startActivity(new Intent(InitApp.AppContext, PhotoContentActivity.class)
                .putExtra(TAG, bean)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PhotoContentFragment.newInstance(getIntent().getParcelableExtra(TAG)))
                .commit();
    }

    public SlidrInterface getSlidrInterface() {
        return slidrInterface;
    }
}
