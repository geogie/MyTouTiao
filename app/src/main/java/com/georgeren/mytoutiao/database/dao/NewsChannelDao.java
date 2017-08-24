package com.georgeren.mytoutiao.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.georgeren.mytoutiao.InitApp;
import com.georgeren.mytoutiao.R;
import com.georgeren.mytoutiao.bean.news.NewsChannelBean;
import com.georgeren.mytoutiao.database.DatabaseHelper;
import com.georgeren.mytoutiao.database.table.NewsChannelTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Meiji on 2017/3/10.
 * 频道：
 */

public class NewsChannelDao {

    private SQLiteDatabase db;

    public NewsChannelDao() {
        this.db = DatabaseHelper.getDatabase();
    }

    /**
     *
     */
    public void addInitData() {
        String categoryId[] = InitApp.AppContext.getResources().getStringArray(R.array.mobile_news_id);
        String categoryName[] = InitApp.AppContext.getResources().getStringArray(R.array.mobile_news_name);
        for (int i = 0; i < 8; i++) {// 8个可见
            add(categoryId[i], categoryName[i], 1, i);
        }
        for (int i = 8; i < categoryId.length; i++) {// 8个后面所有的（不可见），手动可添加为可见
            add(categoryId[i], categoryName[i], 0, i);
        }
    }

    public boolean add(String channelId, String channelName, int isEnable, int position) {
        ContentValues values = new ContentValues();
        values.put(NewsChannelTable.ID, channelId);
        values.put(NewsChannelTable.NAME, channelName);
        values.put(NewsChannelTable.IS_ENABLE, isEnable);
        values.put(NewsChannelTable.POSITION, position);
        long result = db.insert(NewsChannelTable.TABLENAME, null, values);
        return result != -1;
    }

    public List<NewsChannelBean> query(int isEnable) {
        Cursor cursor = db.query(NewsChannelTable.TABLENAME, null, NewsChannelTable.IS_ENABLE + "=?", new String[]{isEnable + ""}, null, null, null);
        List<NewsChannelBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            NewsChannelBean bean = new NewsChannelBean();
            bean.setChannelId(cursor.getString(NewsChannelTable.ID_ID));
            bean.setChannelName(cursor.getString(NewsChannelTable.ID_NAME));
            bean.setIsEnable(cursor.getInt(NewsChannelTable.ID_ISENABLE));
            bean.setPosition(cursor.getInt(NewsChannelTable.ID_POSITION));
            list.add(bean);
        }
        cursor.close();
        return list;
    }

    public List<NewsChannelBean> queryAll() {
        Cursor cursor = db.query(NewsChannelTable.TABLENAME, null, null, null, null, null, null);
        List<NewsChannelBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            NewsChannelBean bean = new NewsChannelBean();
            bean.setChannelId(cursor.getString(NewsChannelTable.ID_ID));
            bean.setChannelName(cursor.getString(NewsChannelTable.ID_NAME));
            bean.setIsEnable(cursor.getInt(NewsChannelTable.ID_ISENABLE));
            bean.setPosition(cursor.getInt(NewsChannelTable.ID_POSITION));
            list.add(bean);
        }
        cursor.close();
        return list;
    }

    public void updateAll(List<NewsChannelBean> list) {
    }

    public boolean removeAll() {
        int result = db.delete(NewsChannelTable.TABLENAME, null, null);
        return result != -1;
    }
}
