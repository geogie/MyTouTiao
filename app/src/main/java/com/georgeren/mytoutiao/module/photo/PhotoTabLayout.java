package com.georgeren.mytoutiao.module.photo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.georgeren.mytoutiao.InitApp;
import com.georgeren.mytoutiao.R;
import com.georgeren.mytoutiao.module.base.BaseListFragment;
import com.georgeren.mytoutiao.module.base.BasePagerAdapter;
import com.georgeren.mytoutiao.module.photo.article.PhotoArticleView;
import com.georgeren.mytoutiao.utils.SettingUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgeRen on 2017/8/19.
 * 图片：全部、老照片、故事照片、摄影集
 */

public class PhotoTabLayout extends Fragment{
    private static PhotoTabLayout instance = null;
    private static int pageSize = InitApp.AppContext.getResources().getStringArray(R.array.photo_id).length;
    private String categoryId[] = InitApp.AppContext.getResources().getStringArray(R.array.photo_id);
    private String categoryName[] = InitApp.AppContext.getResources().getStringArray(R.array.photo_name);
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private BasePagerAdapter adapter;

    public static PhotoTabLayout getInstance() {
        if (instance == null) {
            instance = new PhotoTabLayout();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_tab, container, false);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tabLayout.setBackgroundColor(SettingUtil.getInstance().getColor());// 主题修改后可以很快的修改背景色
    }

    private void initView(View view) {
        tabLayout = view.findViewById(R.id.tab_layout_photo);
        viewPager = view.findViewById(R.id.view_pager_photo);

        tabLayout.setupWithViewPager(viewPager);// tab和viewPager进行联系
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);// 设置滑动模式
        tabLayout.setBackgroundColor(SettingUtil.getInstance().getColor());
        viewPager.setOffscreenPageLimit(pageSize);// 允许最多缓存的pager数
    }

    /**
     * 根据 category 初始化 fragments
     * 初始化 viewpager 的 adapter，设置adapter
     */
    private void initData() {
        for (int i = 0; i < categoryId.length; i++) {// 根据category的个数来初始化fragment的个数
            Fragment fragment = PhotoArticleView.newInstance(categoryId[i]);
            fragmentList.add(fragment);
        }
        adapter = new BasePagerAdapter(getChildFragmentManager(), fragmentList, categoryName);
        viewPager.setAdapter(adapter);
    }

    public void onDoubleClick() {
        if (fragmentList != null && fragmentList.size() > 0) {
            int item = viewPager.getCurrentItem();
            ((BaseListFragment) fragmentList.get(item)).onRefresh();
        }
    }

    @Override
    public void onDestroy() {
        if (instance != null) {
            instance = null;
        }
        super.onDestroy();
    }
}
