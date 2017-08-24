package com.georgeren.mytoutiao;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.georgeren.mytoutiao.utils.SettingUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.Calendar;

/**
 * Created by georgeRen on 2017/8/18.
 */

public class InitApp extends Application{
    public static Context AppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
        Logger.addLogAdapter(new AndroidLogAdapter());
        initTheme();
        if (BuildConfig.DEBUG) {
            SdkManager.initStetho(AppContext);
        }
    }

    private void initTheme() {
        SettingUtil settingUtil = SettingUtil.getInstance();

        // 获取是否开启 "自动切换夜间模式"
        if (settingUtil.getIsAutoNightMode()) {

            int nightStartHour = Integer.parseInt(settingUtil.getNightStartHour());
            int nightStartMinute = Integer.parseInt(settingUtil.getNightStartMinute());
            int dayStartHour = Integer.parseInt(settingUtil.getDayStartHour());
            int dayStartMinute = Integer.parseInt(settingUtil.getDayStartMinute());

            Calendar calendar = Calendar.getInstance();
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);

            int nightValue = nightStartHour * 60 + nightStartMinute;
            int dayValue = dayStartHour * 60 + dayStartMinute;
            int currentValue = currentHour * 60 + currentMinute;

            if (currentValue >= nightValue || currentValue <= dayValue) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                settingUtil.setIsNightMode(true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                settingUtil.setIsNightMode(false);
            }

        } else {
            // 获取当前主题
            if (settingUtil.getIsNightMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }
}
