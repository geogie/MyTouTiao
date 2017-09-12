package com.georgeren.mytoutiao;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.georgeren.mytoutiao.module.base.BaseActivity;
import com.georgeren.mytoutiao.module.channel.MediaChannelView;
import com.georgeren.mytoutiao.module.news.NewsTabLayout;
import com.georgeren.mytoutiao.module.photo.PhotoTabLayout;
import com.georgeren.mytoutiao.module.search.SearchActivity;
import com.georgeren.mytoutiao.module.video.VideoTabLayout;
import com.georgeren.mytoutiao.setting.SettingActivity;
import com.georgeren.mytoutiao.utils.SettingUtil;
import com.georgeren.mytoutiao.widget.helper.BottomNavigationViewHelper;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";
    private static final String POSITION = "position";
    private static final int FRAGMENT_NEWS = 0;// 头条
    private static final int FRAGMENT_PHOTO = 1;// 图片
    private static final int FRAGMENT_VIDEO = 2;// 视频
    private static final int FRAGMENT_MEDIA = 3;// 头条号
    private NewsTabLayout newsTabLayout;// 头条
    private PhotoTabLayout photoTabLayout;// 图片
    private VideoTabLayout videoTabLayout;// 视频
    private MediaChannelView mediaChannelView;// 头条号
    private Toolbar toolbar;
    private BottomNavigationView bottom_navigation;// 底部导航：新闻、图片、视频、头条号
    private long exitTime = 0;
    private long firstClickTime = 0;
    private int position;
    private NavigationView nav_view;
    private DrawerLayout drawer_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        if (savedInstanceState != null) {
            newsTabLayout = (NewsTabLayout) getSupportFragmentManager().findFragmentByTag(NewsTabLayout.class.getName());
            photoTabLayout = (PhotoTabLayout) getSupportFragmentManager().findFragmentByTag(PhotoTabLayout.class.getName());
            videoTabLayout = (VideoTabLayout) getSupportFragmentManager().findFragmentByTag(VideoTabLayout.class.getName());
            mediaChannelView = (MediaChannelView) getSupportFragmentManager().findFragmentByTag(MediaChannelView.class.getName());
            // 屏幕恢复时取出位置
            showFragment(savedInstanceState.getInt(POSITION));
        } else {
            showFragment(FRAGMENT_NEWS);
        }
    }

    @Override
    protected void initSlidable() {
//        super.initSlidable(); //禁止滑动返回,如果调用super会导致侧滑栏被拦截，直接侧滑返回了。
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 屏幕旋转时记录位置
        outState.putInt(POSITION, position);
    }

    /**
     * bottomNavigation 反射修改 item>3 的bug
     * item点击监听
     *
     */
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottom_navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottom_navigation);// 解决 item>3 ui难看问题
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_news:// 新闻
                        showFragment(FRAGMENT_NEWS);
                        doubleClick(FRAGMENT_NEWS);
                        break;
                    case R.id.action_photo:// 图片
                        showFragment(FRAGMENT_PHOTO);
                        doubleClick(FRAGMENT_PHOTO);
                        break;
                    case R.id.action_video:// 视频
                        showFragment(FRAGMENT_VIDEO);
                        doubleClick(FRAGMENT_VIDEO);
                        break;
                    case R.id.action_media:// 头条号
                        showFragment(FRAGMENT_MEDIA);
                        break;
                }
                return true;
            }
        });

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
    }

    private void showFragment(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);
        position = index;
        switch (index) {
            case FRAGMENT_NEWS:
                toolbar.setTitle(R.string.app_name);
                /**
                 * 如果Fragment为空，就新建一个实例
                 * 如果不为空，就将它从栈中显示出来
                 */
                if (newsTabLayout == null) {
                    newsTabLayout = NewsTabLayout.getInstance();
                    ft.add(R.id.container, newsTabLayout, NewsTabLayout.class.getName());
                } else {
                    ft.show(newsTabLayout);
                }
                break;

            case FRAGMENT_PHOTO:
                toolbar.setTitle(R.string.title_photo);
                if (photoTabLayout == null) {
                    photoTabLayout = PhotoTabLayout.getInstance();
                    ft.add(R.id.container, photoTabLayout, PhotoTabLayout.class.getName());
                } else {
                    ft.show(photoTabLayout);
                }
                break;

            case FRAGMENT_VIDEO:
                toolbar.setTitle(getString(R.string.title_video));
                if (videoTabLayout == null) {
                    videoTabLayout = VideoTabLayout.getInstance();
                    ft.add(R.id.container, videoTabLayout, VideoTabLayout.class.getName());
                } else {
                    ft.show(videoTabLayout);
                }
                break;

            case FRAGMENT_MEDIA:
                toolbar.setTitle(getString(R.string.title_media));
                if (mediaChannelView == null) {
                    mediaChannelView = MediaChannelView.getInstance();
                    ft.add(R.id.container, mediaChannelView, MediaChannelView.class.getName());
                } else {
                    ft.show(mediaChannelView);
                }
        }

        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {
        // 如果不为空，就先隐藏起来
        if (newsTabLayout != null) {
            ft.hide(newsTabLayout);
        }
        if (photoTabLayout != null) {
            ft.hide(photoTabLayout);
        }
        if (videoTabLayout != null) {
            ft.hide(videoTabLayout);
        }
        if (mediaChannelView != null) {
            ft.hide(mediaChannelView);
        }
    }

    public void doubleClick(int index) {
        long secondClickTime = System.currentTimeMillis();
        if ((secondClickTime - firstClickTime < 500)) {
            switch (index) {
                case FRAGMENT_NEWS:
                    newsTabLayout.onDoubleClick();
                    break;
                case FRAGMENT_PHOTO:
                    photoTabLayout.onDoubleClick();
                    break;
                case FRAGMENT_VIDEO:
                    videoTabLayout.onDoubleClick();
                    break;
            }
        } else {
            firstClickTime = secondClickTime;
        }
    }

    /**
     * 搜索布局
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    /**
     * 搜索点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
//            case R.id.nav_account:
//                drawer_layout.closeDrawers();
//                return false;

            case R.id.nav_switch_night_mode:// 切换主题
                int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (mode == Configuration.UI_MODE_NIGHT_YES) {
                    SettingUtil.getInstance().setIsNightMode(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    SettingUtil.getInstance().setIsNightMode(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                recreate();// 重新创建activity
                return false;

            case R.id.nav_setting:// 设置
                startActivity(new Intent(this, SettingActivity.class));
                drawer_layout.closeDrawers();
                return false;

            case R.id.nav_share:// 分享
                Intent shareIntent = new Intent()
                        .setAction(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_text) + getString(R.string.source_code_url));
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_to)));
                drawer_layout.closeDrawers();
                return false;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - exitTime) < 2000) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, R.string.double_click_exit, Toast.LENGTH_SHORT).show();
            exitTime = currentTime;
        }
    }
}
