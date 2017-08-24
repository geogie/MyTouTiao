package com.georgeren.mytoutiao.widget.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.georgeren.mytoutiao.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by georgeRen on 2017/8/22.
 * 对节操播放器自定义：点击全屏的监听
 */

public class MyJCVideoPlayerStandard extends JCVideoPlayerStandard {
    public static onClickFullScreenListener onClickFullScreenListener;

    public MyJCVideoPlayerStandard(Context context) {
        super(context);
    }

    public MyJCVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static void setOnClickFullScreenListener(onClickFullScreenListener listener) {
        onClickFullScreenListener = listener;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.fullscreen) {
            if (onClickFullScreenListener != null) {
                onClickFullScreenListener.onClickFullScreen();
            }
        }
    }

    public interface onClickFullScreenListener {
        void onClickFullScreen();
    }
}
