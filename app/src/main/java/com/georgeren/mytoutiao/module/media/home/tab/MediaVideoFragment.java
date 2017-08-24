package com.georgeren.mytoutiao.module.media.home.tab;

import android.os.Bundle;

import com.georgeren.mytoutiao.module.base.BaseListFragment;

import java.util.List;

/**
 * Created by georgeRen on 2017/8/22.
 */

public class MediaVideoFragment extends BaseListFragment<IMediaProfile.Presenter> implements IMediaProfile.View {
    private static final String TAG = "MediaVideoFragment";
    private String mediaId;

    public static MediaVideoFragment newInstance(String mediaId) {
        Bundle args = new Bundle();
        args.putString(TAG, mediaId);
        MediaVideoFragment fragment = new MediaVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(IMediaProfile.Presenter presenter) {

    }

    @Override
    public void onSetAdapter(List<?> list) {

    }

    @Override
    public void onLoadData() {

    }

    @Override
    protected void initData() throws NullPointerException {

    }

    @Override
    public void fetchData() {

    }
}
