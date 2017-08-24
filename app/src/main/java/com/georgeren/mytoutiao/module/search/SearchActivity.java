package com.georgeren.mytoutiao.module.search;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.georgeren.mytoutiao.Constant;
import com.georgeren.mytoutiao.ErrorAction;
import com.georgeren.mytoutiao.R;
import com.georgeren.mytoutiao.RetrofitFactory;
import com.georgeren.mytoutiao.adapter.search.SearchHistoryAdapter;
import com.georgeren.mytoutiao.adapter.search.SearchSuggestionAdapter;
import com.georgeren.mytoutiao.api.IMobileSearchApi;
import com.georgeren.mytoutiao.bean.search.SearchHistoryBean;
import com.georgeren.mytoutiao.bean.search.SearchRecommentBean;
import com.georgeren.mytoutiao.bean.search.SearchSuggestionBean;
import com.georgeren.mytoutiao.database.dao.SearchHistoryDao;
import com.georgeren.mytoutiao.module.base.BaseActivity;
import com.georgeren.mytoutiao.module.base.BasePagerAdapter;
import com.georgeren.mytoutiao.module.search.result.SearchResultFragment;
import com.georgeren.mytoutiao.utils.SettingUtil;
import com.google.android.flexbox.FlexboxLayout;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding2.support.v7.widget.SearchViewQueryTextEvent;
import com.jakewharton.rxbinding2.view.RxView;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by georgeRen on 2017/8/24.
 * 搜索
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SearchActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] titles = new String[]{"综合", "视频", "图集", "用户(beta)", "问答"};
    private SearchView searchView;
    private LinearLayout resultLayout;
    private ListView suggestionList;
    private ListView historyList;
    private SearchHistoryAdapter historyAdapter;
    private SearchSuggestionAdapter suggestionAdapter;
    private SearchHistoryDao dao = new SearchHistoryDao();
    private FlexboxLayout flexboxLayout;
    private LinearLayout hotWordLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        getSearchHotWord();
        getSearchHistory();
    }
    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar(toolbar, true, "");
        // 热门搜索
        hotWordLayout = (LinearLayout) findViewById(R.id.hotword_layout);
        flexboxLayout = (FlexboxLayout) findViewById(R.id.flexbox_layout);
        flexboxLayout.setFlexDirection(FlexboxLayout.FLEX_DIRECTION_ROW);
        flexboxLayout.setFlexWrap(FlexboxLayout.FLEX_WRAP_WRAP);
        TextView tv_clear = (TextView) findViewById(R.id.tv_clear);
        tv_clear.setOnClickListener(this);
        TextView tv_refresh = (TextView) findViewById(R.id.tv_refresh);
        RxView.clicks(tv_refresh)
                // 防抖
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        flexboxLayout.removeAllViews();
                        getSearchHotWord();
                    }
                }, ErrorAction.error());
        // 搜索结果
        resultLayout = (LinearLayout) findViewById(R.id.result_layout);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout.setBackgroundColor(SettingUtil.getInstance().getColor());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        // 搜索建议
        suggestionList = (ListView) findViewById(R.id.suggestion_list);
        suggestionAdapter = new SearchSuggestionAdapter(this, -1);
        suggestionList.setAdapter(suggestionAdapter);
        suggestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String keyWord = suggestionAdapter.getItem(position).getKeyword();
                searchView.clearFocus();
                searchView.setQuery(keyWord, true);
            }
        });
        // 搜索历史
        historyList = (ListView) findViewById(R.id.history_list);
        historyAdapter = new SearchHistoryAdapter(this, -1);
        historyList.setAdapter(historyAdapter);
        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String keyWord = historyAdapter.getItem(position).getKeyWord();
                searchView.clearFocus();
                searchView.setQuery(keyWord, true);
            }
        });
    }

    private void initSearchLayout(String query) {
        hotWordLayout.setVisibility(View.GONE);
        resultLayout.setVisibility(View.VISIBLE);
        suggestionList.setVisibility(View.GONE);
        List<Fragment> fragmentList = new ArrayList<>();
        for (int i = 1; i < titles.length + 1; i++) {
            fragmentList.add(SearchResultFragment.newInstance(query, i + ""));
        }
        BasePagerAdapter pagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    if (slidrInterface != null) {
                        slidrInterface.unlock();
                    }
                } else {
                    if (slidrInterface != null) {
                        slidrInterface.lock();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        // 关联检索配置与 SearchActivity
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(
                new ComponentName(getApplicationContext(), SearchActivity.class));
        searchView.setSearchableInfo(searchableInfo);
        searchView.onActionViewExpanded();
//        // 设置搜索文字样式
//        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        searchEditText.setTextColor(getResources().getColor(R.color.textColorPrimary));
//        searchEditText.setHintTextColor(getResources().getColor(R.color.textColorPrimary));
//        searchEditText.setBackgroundColor(Color.WHITE);
        setOnQuenyTextChangeListener();

        return super.onCreateOptionsMenu(menu);
    }

    private void getSearchHotWord() {

        RetrofitFactory.getRetrofit().create(IMobileSearchApi.class).getSearchRecomment()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<SearchRecommentBean, List<String>>() {
                    @Override
                    public List<String> apply(@NonNull SearchRecommentBean searchRecommentBean) throws Exception {
                        List<SearchRecommentBean.DataBean.SuggestWordListBean> suggest_word_list = searchRecommentBean.getData().getSuggest_word_list();
                        List<String> hotList = new ArrayList<>();
                        for (int i = 0; i < suggest_word_list.size(); i++) {
                            if (suggest_word_list.get(i).getType().equals("recom")) {
                                hotList.add(suggest_word_list.get(i).getWord());
                            }
                        }
                        return hotList;
                    }
                })
                .compose(this.<List<String>>bindToLife())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@NonNull final List<String> list) throws Exception {
                        for (int i = 0; i < list.size(); i++) {
                            final TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_search_sug_text, flexboxLayout, false);
                            final String keyWord = list.get(i);
                            int color = Constant.TAG_COLORS[i % Constant.TAG_COLORS.length];
                            tv.setText(keyWord);
                            tv.setBackgroundColor(color);
                            tv.setTextColor(Color.WHITE);
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    searchView.clearFocus();
                                    searchView.setQuery(keyWord, true);
                                }
                            });
                            flexboxLayout.addView(tv);
                            if (i == 7) {
                                return;
                            }
                        }
                    }
                }, ErrorAction.error());
    }

    private void getSearchHistory() {
        Observable
                .create(new ObservableOnSubscribe<List<SearchHistoryBean>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<SearchHistoryBean>> e) throws Exception {
                        List<SearchHistoryBean> list = dao.queryAll();
                        e.onNext(list);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<List<SearchHistoryBean>>bindToLife())
                .subscribe(new Consumer<List<SearchHistoryBean>>() {
                    @Override
                    public void accept(@NonNull final List<SearchHistoryBean> list) throws Exception {
                        historyAdapter.updateDataSource(list);
                    }
                }, ErrorAction.error());
    }

    private void getSearchSuggest(String keyWord) {
        RetrofitFactory.getRetrofit().create(IMobileSearchApi.class)
                .getSearchSuggestion(keyWord.trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<SearchSuggestionBean>bindToLife())
                .subscribe(new Consumer<SearchSuggestionBean>() {
                    @Override
                    public void accept(@NonNull SearchSuggestionBean bean) throws Exception {
                        suggestionAdapter.updateDataSource(bean.getData());
                    }
                }, ErrorAction.error());
    }

    private void setOnQuenyTextChangeListener() {
        RxSearchView.queryTextChangeEvents(searchView)
                .throttleLast(100, TimeUnit.MILLISECONDS)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<SearchViewQueryTextEvent>bindToLife())
                .subscribe(new Consumer<SearchViewQueryTextEvent>() {
                    @Override
                    public void accept(@NonNull SearchViewQueryTextEvent searchViewQueryTextEvent) throws Exception {
                        final String keyWord = searchViewQueryTextEvent.queryText() + "";
                        Log.d(TAG, "accept: " + keyWord);
                        if (searchViewQueryTextEvent.isSubmitted()) {
                            searchView.clearFocus();
                            initSearchLayout(keyWord);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (dao.queryisExist(keyWord)) {
                                        dao.update(keyWord);
                                    } else {
                                        dao.add(keyWord);
                                    }
                                }
                            }).start();
                            return;
                        }
                        if (!TextUtils.isEmpty(keyWord)) {
                            getSearchSuggest(keyWord);
                            hotWordLayout.setVisibility(View.GONE);
                            resultLayout.setVisibility(View.GONE);
                            suggestionList.setVisibility(View.VISIBLE);
                        } else {
                            getSearchHistory();
                            if (hotWordLayout.getVisibility() != View.VISIBLE) {
                                hotWordLayout.setVisibility(View.VISIBLE);
                            }
                            if (resultLayout.getVisibility() != View.GONE) {
                                resultLayout.setVisibility(View.GONE);
                            }
                            if (suggestionList.getVisibility() != View.GONE) {
                                suggestionList.setVisibility(View.GONE);
                            }
                        }
                    }
                }, ErrorAction.error());
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchView.clearFocus();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_clear) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.delete_all_search_history)
                    .setPositiveButton(R.string.button_enter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    dao.deleteAll();
                                    getSearchHistory();
                                }
                            }).start();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        if (suggestionList.getVisibility() != View.GONE) {
            // 关闭搜索建议
            suggestionList.setVisibility(View.GONE);
            hotWordLayout.setVisibility(View.VISIBLE);
        } else if (resultLayout.getVisibility() != View.GONE) {
            // 关闭搜索结果
            searchView.setQuery("", false);
            searchView.clearFocus();
            resultLayout.setVisibility(View.GONE);
            hotWordLayout.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }

    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindUntilEvent(ActivityEvent.DESTROY);
    }
}
