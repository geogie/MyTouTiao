package com.georgeren.mytoutiao;

import android.support.annotation.NonNull;

import io.reactivex.functions.Consumer;

/**
 * Created by georgeRen on 2017/8/21.
 * 异常捕获类
 */

public class ErrorAction {
    @NonNull
    public static Consumer<Throwable> error() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                if (BuildConfig.DEBUG) {
                    throwable.printStackTrace();
                }
            }
        };
    }

    public static void print(@NonNull Throwable throwable) {
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace();
        }
    }
}
