package com.georgeren.mytoutiao.module.joke.comment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.georgeren.mytoutiao.R;
import com.georgeren.mytoutiao.Register;
import com.georgeren.mytoutiao.adapter.DiffCallback;
import com.georgeren.mytoutiao.bean.LoadingBean;
import com.georgeren.mytoutiao.bean.joke.JokeContentBean;
import com.georgeren.mytoutiao.module.base.BaseListFragment;
import com.georgeren.mytoutiao.utils.OnLoadMoreListener;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by georgeRen on 2017/8/22.
 * 段子 详情的Fragment
 */

public class JokeCommentFragment extends BaseListFragment<IJokeComment.Presenter> implements IJokeComment.View {

    public static final String TAG = "JokeCommentFragment";
    private String jokeId;
    private String jokeCommentCount;
    private String jokeText;
    private JokeContentBean.DataBean.GroupBean jokeCommentHeaderBean;
    public static JokeCommentFragment newInstance(Parcelable data) {
        Bundle args = new Bundle();
        args.putParcelable(TAG, data);
        JokeCommentFragment fragment = new JokeCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_list_toolbar;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        initToolBar(toolbar, true, "");
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        adapter = new MultiTypeAdapter(oldItems);
        Register.registerJokeCommentItem(adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (canLoadMore) {
                    canLoadMore = false;
                    presenter.doLoadMoreData();
                }
            }
        });
        setHasOptionsMenu(true);
    }

    @Override
    public void onRefresh() {
        presenter.doRefresh();
    }

    @Override
    public void setPresenter(IJokeComment.Presenter presenter) {
        if (null == presenter) {
            this.presenter = new JokeCommentPresenter(this);
        }
    }

    @Override
    public void onSetAdapter(List<?> list) {
        Items newItems = new Items();
        newItems.add(jokeCommentHeaderBean);
        newItems.addAll(list);
        newItems.add(new LoadingBean());
        DiffCallback.notifyDataSetChanged(oldItems, newItems, DiffCallback.JOKE_COMMENT, adapter);
        oldItems.clear();
        oldItems.addAll(newItems);
        canLoadMore = true;
    }

    @Override
    public void onLoadData() {
        presenter.doLoadData(jokeId, jokeCommentCount);
    }

    @Override
    protected void initData(){
        Bundle bundle = getArguments();
        try {
            jokeCommentHeaderBean = bundle.getParcelable(TAG);
            jokeId = jokeCommentHeaderBean.getId() + "";
            jokeCommentCount = jokeCommentHeaderBean.getComment_count() + "";
            jokeText = jokeCommentHeaderBean.getText();
            oldItems.add(jokeCommentHeaderBean);
        } catch (Exception e) {

        }
        onLoadData();
    }

    @Override
    public void fetchData() {

    }
}
