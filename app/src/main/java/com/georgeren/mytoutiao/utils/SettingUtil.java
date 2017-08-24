package com.georgeren.mytoutiao.utils;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.georgeren.mytoutiao.InitApp;
import com.georgeren.mytoutiao.R;

/**
 * Created by georgeRen on 2017/8/18.
 */

public class SettingUtil {
    private SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(InitApp.AppContext);
    //内部类实现单例
    public static SettingUtil getInstance() {
        return SettingsUtilInstance.instance;
    }
    /**
     * 获取是否开启自动切换夜间模式
     */
    public boolean getIsAutoNightMode() {
        return setting.getBoolean("auto_nightMode", false);
    }

    /**
     * 默认晚上10点开启夜间模式
     * @return
     */
    public String getNightStartHour() {
        return setting.getString("night_startHour", "22");
    }
    public String getNightStartMinute() {
        return setting.getString("night_startMinute", "00");
    }

    /**
     * 默认早上6点开启白天模式
     * @return
     */
    public String getDayStartHour() {
        return setting.getString("day_startHour", "06");
    }
    public String getDayStartMinute() {
        return setting.getString("day_startMinute", "00");
    }

    /**
     * 设置夜间模式
     */
    public void setIsNightMode(boolean flag) {
        setting.edit().putBoolean("switch_nightMode", flag).apply();
    }

    /**
     * 获取是否开启夜间模式
     */
    public boolean getIsNightMode() {
        return setting.getBoolean("switch_nightMode", false);
    }

    private static final class SettingsUtilInstance {
        private static final SettingUtil instance = new SettingUtil();
    }

    /**
     * 获取滑动返回值
     */
    public int getSlidable() {
        String s = setting.getString("slidable", "1");
        return Integer.parseInt(s);
    }

    /**
     * 获取图标值
     */
    public int getCustomIconValue() {
        String s = setting.getString("custom_icon", "0");
        return Integer.parseInt(s);
    }

    /**
     * 获取主题颜色
     */
    public int getColor() {
        int defaultColor = InitApp.AppContext.getResources().getColor(R.color.colorPrimary);
        int color = setting.getInt("color", defaultColor);
        if ((color != 0) && Color.alpha(color) != 255) {
            return defaultColor;
        }
        return color;
    }

    /**
     * 设置主题颜色
     */
    public void setColor(int color) {
        setting.edit().putInt("color", color).apply();
    }

    /**
     * 获取是否开启导航栏上色
     */
    public boolean getNavBar() {
        return setting.getBoolean("nav_bar", false);
    }

    /**
     * 获取是否开启无图模式
     */
    public boolean getIsNoPhotoMode() {
        return setting.getBoolean("switch_noPhotoMode", false) && NetWorkUtil.isMobileConnected(InitApp.AppContext);
    }

    /**
     * 获取是否开启视频强制横屏
     */
    public boolean getIsVideoForceLandscape() {
        return setting.getBoolean("video_force_landscape", false);
    }
    /**
     * 获取是否开启视频自动播放
     */
    public boolean getIsVideoAutoPlay() {
        return setting.getBoolean("video_auto_play", false) && NetWorkUtil.isWifiConnected(InitApp.AppContext);
    }

    public void setNightStartHour(String nightStartHour) {
        setting.edit().putString("night_startHour", nightStartHour).apply();
    }
    public void setNightStartMinute(String nightStartMinute) {
        setting.edit().putString("night_startMinute", nightStartMinute).apply();
    }
    public void setDayStartHour(String dayStartHour) {
        setting.edit().putString("day_startHour", dayStartHour).apply();
    }
    public void setDayStartMinute(String dayStartMinute) {
        setting.edit().putString("day_startMinute", dayStartMinute).apply();
    }
}
