package com.georgeren.mytoutiao.module.search.result;

import android.os.Bundle;
import android.view.View;

import com.georgeren.mytoutiao.Register;
import com.georgeren.mytoutiao.adapter.DiffCallback;
import com.georgeren.mytoutiao.bean.LoadingBean;
import com.georgeren.mytoutiao.module.base.BaseListFragment;
import com.georgeren.mytoutiao.utils.OnLoadMoreListener;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by georgeRen on 2017/8/24.
 * 搜索结果
 */

public class SearchResultFragment extends BaseListFragment<ISearchResult.Presenter> implements ISearchResult.View {
    private String query;
    private String curTab;

    public static SearchResultFragment newInstance(String query, String curTab) {
        Bundle args = new Bundle();
        args.putString("queryAll", query);
        args.putString("curTab", curTab);
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void setPresenter(ISearchResult.Presenter presenter) {
        if (presenter == null) {
            this.presenter = new SearchResultPresenter(this);
        }
    }

    @Override
    public void onSetAdapter(List<?> list) {
        Items newItems = new Items(list);
        newItems.add(new LoadingBean());
        DiffCallback.notifyDataSetChanged(oldItems, newItems, DiffCallback.MUlTI_NEWS, adapter);
        oldItems.clear();
        oldItems.addAll(newItems);
        canLoadMore = true;
    }

    @Override
    public void onLoadData() {
        onShowLoading();
        presenter.doLoadData(query, curTab);
    }

    @Override
    protected void initData() throws NullPointerException {
        query = getArguments().getString("queryAll");
        curTab = getArguments().getString("curTab");
    }

    @Override
    public void fetchData() {
        onLoadData();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        recyclerView.smoothScrollToPosition(0);
        presenter.doRefresh();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        adapter = new MultiTypeAdapter(oldItems);
        Register.registerSearchItem(adapter);
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
    }
}
