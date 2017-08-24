package com.georgeren.mytoutiao.module.wenda.content;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.georgeren.mytoutiao.IntentAction;
import com.georgeren.mytoutiao.R;
import com.georgeren.mytoutiao.Register;
import com.georgeren.mytoutiao.adapter.DiffCallback;
import com.georgeren.mytoutiao.bean.LoadingBean;
import com.georgeren.mytoutiao.bean.wenda.WendaContentBean;
import com.georgeren.mytoutiao.module.base.BaseListFragment;
import com.georgeren.mytoutiao.utils.OnLoadMoreListener;
import com.georgeren.mytoutiao.utils.SettingUtil;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by georgeRen on 2017/8/22.
 * 问答 详情的Fragment
 */

public class WendaContentFragment extends BaseListFragment<IWendaContent.Presenter> implements IWendaContent.View {
    private static final String TAG = "WendaContentFragment";
    private String qid;
    private String shareTitle;
    private String shareUrl;
    private WendaContentBean.QuestionBean WendaContentHeaderBean;

    public static WendaContentFragment newInstance(String qid) {
        Bundle args = new Bundle();
        args.putString(TAG, qid);
        WendaContentFragment fragment = new WendaContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(IWendaContent.Presenter presenter) {
        if (null == presenter) {
            this.presenter = new WendaContentPresenter(this);
        }
    }

    @Override
    public void onSetAdapter(List<?> list) {
        Items newItems = new Items();
        newItems.add(WendaContentHeaderBean);
        newItems.addAll(list);
        newItems.add(new LoadingBean());
        DiffCallback.notifyDataSetChanged(oldItems, newItems, DiffCallback.WENDA_CONTENT, adapter);
        oldItems.clear();
        oldItems.addAll(newItems);
        canLoadMore = true;
    }

    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_list_toolbar;
    }

    @Override
    public void onLoadData() {
        onShowLoading();
        presenter.doLoadData(qid);
    }

    @Override
    public void onSetHeader(WendaContentBean.QuestionBean questionBean) {
        this.shareTitle = questionBean.getShare_data().getTitle();
        this.shareUrl = questionBean.getShare_data().getShare_url();
        this.WendaContentHeaderBean = questionBean;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        initToolBar(toolbar, true, getString(R.string.title_wenda));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        toolbar.setBackgroundColor(SettingUtil.getInstance().getColor());

        adapter = new MultiTypeAdapter(oldItems);
        Register.registerWendaContentItem(adapter);
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
    protected void initData() {
        this.qid = getArguments().getString(TAG);
        onLoadData();
    }

    @Override
    public void fetchData() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_wenda_content, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_wenda_share) {
            IntentAction.send(getActivity(), shareTitle + "\n" + shareUrl);
        }
        return super.onOptionsItemSelected(item);
    }
}
