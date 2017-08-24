package com.georgeren.mytoutiao.module.news.channel;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.georgeren.mytoutiao.ErrorAction;
import com.georgeren.mytoutiao.R;
import com.georgeren.mytoutiao.RxBus;
import com.georgeren.mytoutiao.adapter.news.NewsChannelAdapter;
import com.georgeren.mytoutiao.bean.news.NewsChannelBean;
import com.georgeren.mytoutiao.database.dao.NewsChannelDao;
import com.georgeren.mytoutiao.module.base.BaseActivity;
import com.georgeren.mytoutiao.module.news.NewsTabLayout;
import com.georgeren.mytoutiao.widget.helper.ItemDragHelperCallback;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * +:新闻添加频道（我的频道、隐藏频道）
 */
public class NewsChannelActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private NewsChannelAdapter adapter;
    private NewsChannelDao dao = new NewsChannelDao();
    private List<NewsChannelBean> items = dao.query(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_channel);
        initView();
        initData();
    }
    @Override
    protected void onStop() {
        super.onStop();
        onSaveData();
    }
    public void onSaveData() {

        Observable
                .create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                        e.onNext(!compare(items, adapter.getmMyChannelItems()));
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            List<NewsChannelBean> items = adapter.getmMyChannelItems();
                            List<NewsChannelBean> otherItems = adapter.getmOtherChannelItems();
                            dao.removeAll();
                            for (int i = 0; i < items.size(); i++) {
                                NewsChannelBean bean = items.get(i);
                                dao.add(bean.getChannelId(), bean.getChannelName(), 1, i);
                            }
                            for (int i = 0; i < otherItems.size(); i++) {
                                NewsChannelBean bean = otherItems.get(i);
                                dao.add(bean.getChannelId(), bean.getChannelName(), 0, i);
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean isRefresh) throws Exception {
                        RxBus.getInstance().post(NewsTabLayout.TAG, isRefresh);
                    }
                }, ErrorAction.error());
    }

    public synchronized <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
//        Collections.sort(a);
//        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }

    private void initView() {
        initToolBar((Toolbar) findViewById(R.id.toolbar), true, getString(R.string.title_item_drag));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void initData() {
        final List<NewsChannelBean> items = dao.query(1);
        final List<NewsChannelBean> otherItems = dao.query(0);

        GridLayoutManager manager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        adapter = new NewsChannelAdapter(this, helper, items, otherItems);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                return viewType == NewsChannelAdapter.TYPE_MY || viewType == NewsChannelAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
        recyclerView.setAdapter(adapter);

        adapter.setOnMyChannelItemClickListener(new NewsChannelAdapter.OnMyChannelItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(NewsChannelActivity.this, items.get(position).getChannelName() + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
