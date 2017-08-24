package com.georgeren.mytoutiao.interfaces;

import android.view.View;

/**
 * Created by georgeRen on 2017/8/24.
 */

public interface IOnItemLongClickListener {
    /**
     * RecyclerView Item长按事件
     */
    void onLongClick(View view, int position);
}
