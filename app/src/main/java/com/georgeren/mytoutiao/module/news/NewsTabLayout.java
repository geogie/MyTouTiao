package com.georgeren.mytoutiao.module.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.georgeren.mytoutiao.R;
import com.georgeren.mytoutiao.RxBus;
import com.georgeren.mytoutiao.bean.news.NewsChannelBean;
import com.georgeren.mytoutiao.database.dao.NewsChannelDao;
import com.georgeren.mytoutiao.module.base.BaseListFragment;
import com.georgeren.mytoutiao.module.base.BasePagerAdapter;
import com.georgeren.mytoutiao.module.joke.content.JokeContentView;
import com.georgeren.mytoutiao.module.news.article.NewsArticleView;
import com.georgeren.mytoutiao.module.news.channel.NewsChannelActivity;
import com.georgeren.mytoutiao.module.wenda.article.WendaArticleView;
import com.georgeren.mytoutiao.utils.SettingUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by georgeRen on 2017/8/18.
 * 头条（新闻）：推荐、热点、视频、社会等三大类Fragment
 */

public class NewsTabLayout extends Fragment {
    public static final String TAG = "NewsTabLayout";
    private static NewsTabLayout instance = null;
    private ViewPager viewPager;
    private BasePagerAdapter adapter;
    private LinearLayout linearLayout;
    private NewsChannelDao dao = new NewsChannelDao();
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private Observable<Boolean> observable;

    public static NewsTabLayout getInstance() {
        if (instance == null) {
            instance = new NewsTabLayout();
        }
        return instance;
    }

    public void onDoubleClick() {
        if (titleList != null && titleList.size() > 0 && fragmentList != null && fragmentList.size() > 0) {
            int item = viewPager.getCurrentItem();
            ((BaseListFragment) fragmentList.get(item)).onRefresh();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_tab, container, false);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        linearLayout.setBackgroundColor(SettingUtil.getInstance().getColor());
    }

    private void initView(View view) {
        TabLayout tab_layout = view.findViewById(R.id.tab_layout_news);
        viewPager = view.findViewById(R.id.view_pager_news);

        tab_layout.setupWithViewPager(viewPager);
        tab_layout.setTabMode(TabLayout.MODE_SCROLLABLE);
        ImageView add_channel_iv = view.findViewById(R.id.add_channel_iv);
        add_channel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewsChannelActivity.class));// 添加新闻频道
            }
        });
        linearLayout = view.findViewById(R.id.header_layout);
        linearLayout.setBackgroundColor(SettingUtil.getInstance().getColor());
    }

    private void initData() {
        initTabs();
        adapter = new BasePagerAdapter(getChildFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(15);

        observable = RxBus.getInstance().register(NewsTabLayout.TAG);
        observable.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isRefresh) throws Exception {
                if (isRefresh) {
                    initTabs();
                    adapter.recreateItems(fragmentList, titleList);
                }
            }
        });
    }

    private void initTabs() {
        List<NewsChannelBean> channelList = dao.query(1);
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        if (channelList.size() == 0) {// 如果导航的数据为null，则去初始化
            dao.addInitData();
            channelList = dao.query(1);
        }

        for (NewsChannelBean bean : channelList) {// 共三类fragment
            if (bean.getChannelId().equals("essay_joke")) {// 段子
                fragmentList.add(JokeContentView.newInstance());
                Logger.d("channel:段子" + bean.getChannelId());
            } else if (bean.getChannelId().equals("question_and_answer")) {// 问答
                fragmentList.add(WendaArticleView.newInstance());
                Logger.d("channel:问答" + bean.getChannelId());
            } else {// 其它
                fragmentList.add(NewsArticleView.newInstance(bean.getChannelId()));
                Logger.d("channel:其它" + bean.getChannelId());
            }
            titleList.add(bean.getChannelName());
        }
    }

    @Override
    public void onDestroy() {
        RxBus.getInstance().unregister(NewsTabLayout.TAG, observable);
        if (instance != null) {
            instance = null;
        }
        super.onDestroy();
    }
}
