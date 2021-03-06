package com.georgeren.mytoutiao.module.channel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.georgeren.mytoutiao.R;
import com.georgeren.mytoutiao.Register;
import com.georgeren.mytoutiao.bean.media.MediaChannelBean;
import com.georgeren.mytoutiao.database.dao.MediaChannelDao;
import com.georgeren.mytoutiao.interfaces.IOnItemLongClickListener;
import com.georgeren.mytoutiao.utils.SettingUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by georgeRen on 2017/8/19.
 */

public class MediaChannelView extends RxFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MediaChannelView";
    private static MediaChannelView instance = null;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MultiTypeAdapter adapter;
    private MediaChannelDao dao = new MediaChannelDao();
    private TextView tv_desc;
    private String isFirstTime = "isFirstTime";
    private List<MediaChannelBean> list;

    public static MediaChannelView getInstance() {
        if (instance == null) {
            instance = new MediaChannelView();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setColorSchemeColors(SettingUtil.getInstance().getColor());
        setAdapter();
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(SettingUtil.getInstance().getColor());
        swipeRefreshLayout.setOnRefreshListener(this);
        tv_desc = view.findViewById(R.id.tv_desc);

        IOnItemLongClickListener listener = new IOnItemLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                final MediaChannelBean item = list.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("取消订阅\" " + item.getName() + " \"?");
                builder.setPositiveButton(R.string.button_enter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                dao.delete(item.getId());
                                setAdapter();
                            }
                        }).start();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        };
        adapter = new MultiTypeAdapter();
        Register.registerMediaChannelItem(adapter, listener);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        SharedPreferences editor = getActivity().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        boolean result = editor.getBoolean(isFirstTime, true);
        if (result) {
            dao.initData();
            editor.edit().putBoolean(isFirstTime, false).apply();
        }
        setAdapter();
    }


    private void setAdapter() {
        Observable
                .create(new ObservableOnSubscribe<List<MediaChannelBean>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<MediaChannelBean>> e) throws Exception {
                        list = dao.queryAll();
                        e.onNext(list);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<List<MediaChannelBean>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<List<MediaChannelBean>>() {
                    @Override
                    public void accept(@NonNull List<MediaChannelBean> list) throws Exception {
                        adapter.setItems(list);
                        adapter.notifyDataSetChanged();
                        if (list.size() == 0) {
                            tv_desc.setVisibility(View.VISIBLE);
                        } else {
                            tv_desc.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        setAdapter();
        swipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (instance != null) {
            instance = null;
        }
    }
}
