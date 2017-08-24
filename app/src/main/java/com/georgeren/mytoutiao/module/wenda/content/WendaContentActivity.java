package com.georgeren.mytoutiao.module.wenda.content;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.georgeren.mytoutiao.InitApp;
import com.georgeren.mytoutiao.R;
import com.georgeren.mytoutiao.module.base.BaseActivity;

/**
 * Created by georgeRen on 2017/8/22.
 * 问答 详情
 */

public class WendaContentActivity extends BaseActivity {
    private static final String TAG = "WendaContentActivity";

    public static void launch(String qid) {
        InitApp.AppContext.startActivity(new Intent(InitApp.AppContext, WendaContentActivity.class)
                .putExtra(TAG, qid)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, WendaContentFragment.newInstance(getIntent().getStringExtra(TAG)))
                .commit();
    }
}
