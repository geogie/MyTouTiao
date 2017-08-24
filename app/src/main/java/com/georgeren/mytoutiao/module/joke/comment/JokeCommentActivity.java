package com.georgeren.mytoutiao.module.joke.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.georgeren.mytoutiao.InitApp;
import com.georgeren.mytoutiao.R;
import com.georgeren.mytoutiao.bean.joke.JokeContentBean;
import com.georgeren.mytoutiao.module.base.BaseActivity;

/**
 * Created by georgeRen on 2017/8/22.
 * 段子 详情：
 */

public class JokeCommentActivity extends BaseActivity {
    private static final String TAG = "NewsCommentView";

    public static void launch(JokeContentBean.DataBean.GroupBean bean) {
        InitApp.AppContext.startActivity(new Intent(InitApp.AppContext, JokeCommentActivity.class)
                .putExtra(TAG, bean)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, JokeCommentFragment.newInstance(getIntent().getParcelableExtra(TAG)))
                .commit();
    }
}
