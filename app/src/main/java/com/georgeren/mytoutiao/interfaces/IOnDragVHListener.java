package com.georgeren.mytoutiao.interfaces;

/**
 * Created by georgeRen on 2017/8/22.
 * 拖拽频道监听
 */

public interface IOnDragVHListener {
    /**
     * Item被选中时触发
     */
    void onItemSelected();


    /**
     * Item在拖拽结束/滑动结束后触发
     */
    void onItemFinish();
}
