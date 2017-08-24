package com.georgeren.mytoutiao;

import android.support.annotation.NonNull;

import com.georgeren.mytoutiao.bean.LoadingBean;
import com.georgeren.mytoutiao.bean.LoadingEndBean;
import com.georgeren.mytoutiao.bean.joke.JokeCommentBean;
import com.georgeren.mytoutiao.bean.joke.JokeContentBean;
import com.georgeren.mytoutiao.bean.media.MediaChannelBean;
import com.georgeren.mytoutiao.bean.media.MediaProfileBean;
import com.georgeren.mytoutiao.bean.media.MultiMediaArticleBean;
import com.georgeren.mytoutiao.bean.news.MultiNewsArticleDataBean;
import com.georgeren.mytoutiao.bean.news.NewsCommentBean;
import com.georgeren.mytoutiao.bean.photo.PhotoArticleBean;
import com.georgeren.mytoutiao.bean.wenda.WendaArticleDataBean;
import com.georgeren.mytoutiao.bean.wenda.WendaContentBean;
import com.georgeren.mytoutiao.binder.LoadingEndViewBinder;
import com.georgeren.mytoutiao.binder.LoadingViewBinder;
import com.georgeren.mytoutiao.binder.joke.JokeCommentHeaderViewBinder;
import com.georgeren.mytoutiao.binder.joke.JokeCommentViewBinder;
import com.georgeren.mytoutiao.binder.joke.JokeContentViewBinder;
import com.georgeren.mytoutiao.binder.media.MediaArticleHeaderViewBinder;
import com.georgeren.mytoutiao.binder.media.MediaArticleImgViewBinder;
import com.georgeren.mytoutiao.binder.media.MediaArticleTextViewBinder;
import com.georgeren.mytoutiao.binder.media.MediaArticleVideoViewBinder;
import com.georgeren.mytoutiao.binder.media.MediaChannelViewBinder;
import com.georgeren.mytoutiao.binder.news.NewsArticleImgViewBinder;
import com.georgeren.mytoutiao.binder.news.NewsArticleTextViewBinder;
import com.georgeren.mytoutiao.binder.news.NewsArticleVideoViewBinder;
import com.georgeren.mytoutiao.binder.news.NewsCommentViewBinder;
import com.georgeren.mytoutiao.binder.photo.PhotoArticleViewBinder;
import com.georgeren.mytoutiao.binder.search.SearchArticleVideoViewBinder;
import com.georgeren.mytoutiao.binder.video.VideoContentHeaderViewBinder;
import com.georgeren.mytoutiao.binder.wenda.WendaArticleOneImgViewBinder;
import com.georgeren.mytoutiao.binder.wenda.WendaArticleTextViewBinder;
import com.georgeren.mytoutiao.binder.wenda.WendaArticleThreeImgViewBinder;
import com.georgeren.mytoutiao.binder.wenda.WendaContentHeaderViewBinder;
import com.georgeren.mytoutiao.binder.wenda.WendaContentViewBinder;
import com.georgeren.mytoutiao.interfaces.IOnItemLongClickListener;

import me.drakeet.multitype.ClassLinker;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by georgeRen on 2017/8/21.
 */

public class Register {
    /**
     * 用adapter注册多item对应的样式
     * 第三分类Fragment（其它）item：
     * @param adapter
     */
    public static void registerNewsArticleItem(@NonNull MultiTypeAdapter adapter) {
        // 一个类型对应多个 ItemViewBinder
        adapter.register(MultiNewsArticleDataBean.class)
                .to(new NewsArticleImgViewBinder(),// item:左文右图
                        new NewsArticleVideoViewBinder(),// item:视频样式
                        new NewsArticleTextViewBinder())// item:纯文本
                .withClassLinker(new ClassLinker<MultiNewsArticleDataBean>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<MultiNewsArticleDataBean, ?>> index(@NonNull MultiNewsArticleDataBean item) {
                        if (item.isHas_video()) {
                            return NewsArticleVideoViewBinder.class;
                        }
                        if (null != item.getImage_list() && item.getImage_list().size() > 0) {
                            return NewsArticleImgViewBinder.class;
                        }
                        return NewsArticleTextViewBinder.class;
                    }
                });
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    /**
     * 第二分类的Fragment（问答）item：
     * @param adapter
     */
    public static void registerWendaArticleItem(@NonNull MultiTypeAdapter adapter) {
        // 一个类型对应多个 ItemViewBinder
        adapter.register(WendaArticleDataBean.class)
                .to(new WendaArticleTextViewBinder(),// 纯文本
                        new WendaArticleOneImgViewBinder(),// 一张图片
                        new WendaArticleThreeImgViewBinder())// 3张图片
                .withClassLinker(new ClassLinker<WendaArticleDataBean>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<WendaArticleDataBean, ?>> index(@NonNull WendaArticleDataBean item) {
                        if (null != item.getExtraBean().getWenda_image() &&
                                null != item.getExtraBean().getWenda_image().getThree_image_list() &&
                                item.getExtraBean().getWenda_image().getThree_image_list().size() > 0) {
                            return WendaArticleThreeImgViewBinder.class;
                        }
                        if (null != item.getExtraBean().getWenda_image() &&
                                null != item.getExtraBean().getWenda_image().getLarge_image_list() &&
                                item.getExtraBean().getWenda_image().getLarge_image_list().size() > 0) {
                            return WendaArticleOneImgViewBinder.class;
                        }
                        return WendaArticleTextViewBinder.class;
                    }
                });
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    /**
     * 第一分类的Fragment（段子）item：
     * @param adapter
     */
    public static void registerJokeContentItem(@NonNull MultiTypeAdapter adapter) {
        adapter.register(JokeContentBean.DataBean.GroupBean.class, new JokeContentViewBinder());
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    /**
     * video:的item
     * @param adapter
     */
    public static void registerVideoContentItem(@NonNull MultiTypeAdapter adapter) {
        adapter.register(MultiNewsArticleDataBean.class, new VideoContentHeaderViewBinder());// video视频下的头部：视频信息
        adapter.register(NewsCommentBean.DataBean.CommentBean.class, new NewsCommentViewBinder());// video视频下的评论内容
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    /**
     * 问答 详情：
     * @param adapter
     */
    public static void registerWendaContentItem(@NonNull MultiTypeAdapter adapter) {
        adapter.register(WendaContentBean.QuestionBean.class, new WendaContentHeaderViewBinder());// 问答标题item
        adapter.register(WendaContentBean.AnsListBean.class, new WendaContentViewBinder());// 问答 回答item
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    /**
     * 段子 详情：头部+内容
     * @param adapter
     */
    public static void registerJokeCommentItem(@NonNull MultiTypeAdapter adapter) {
        adapter.register(JokeContentBean.DataBean.GroupBean.class, new JokeCommentHeaderViewBinder());
        adapter.register(JokeCommentBean.DataBean.RecentCommentsBean.class, new JokeCommentViewBinder());
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    /**
     * 评论 ：
     * @param adapter
     */
    public static void registerNewsCommentItem(@NonNull MultiTypeAdapter adapter) {
        adapter.register(NewsCommentBean.DataBean.CommentBean.class, new NewsCommentViewBinder());
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    /**
     * 头条号主页-文章:
     * @param adapter
     */
    public static void registerMediaArticleItem(@NonNull MultiTypeAdapter adapter) {
        adapter.register(MultiMediaArticleBean.DataBean.class)
                .to(new MediaArticleImgViewBinder(),// 带图片的
                        new MediaArticleVideoViewBinder(),// 带视频图片的
                        new MediaArticleTextViewBinder())// 不带图片的
                .withClassLinker(new ClassLinker<MultiMediaArticleBean.DataBean>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<MultiMediaArticleBean.DataBean, ?>> index(@NonNull MultiMediaArticleBean.DataBean item) {
                        if (item.isHas_video()) {
                            return MediaArticleVideoViewBinder.class;
                        }
                        if (null != item.getImage_list() && item.getImage_list().size() > 0) {
                            return MediaArticleImgViewBinder.class;
                        }
                        return MediaArticleTextViewBinder.class;
                    }
                });
        adapter.register(MediaProfileBean.DataBean.class, new MediaArticleHeaderViewBinder());// 头部
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    /**
     * 图片：
     * @param adapter
     */
    public static void registerPhotoArticleItem(@NonNull MultiTypeAdapter adapter) {
        adapter.register(PhotoArticleBean.DataBean.class, new PhotoArticleViewBinder());// 三个图片
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    /**
     * 视频
     * @param adapter
     */
    public static void registerVideoArticleItem(@NonNull MultiTypeAdapter adapter) {
        adapter.register(MultiNewsArticleDataBean.class, new NewsArticleVideoViewBinder());
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

    /**
     * 头条号
     * @param adapter
     * @param listener
     */
    public static void registerMediaChannelItem(@NonNull MultiTypeAdapter adapter, @NonNull IOnItemLongClickListener listener) {
        adapter.register(MediaChannelBean.class, new MediaChannelViewBinder(listener));
    }

    /**
     * 搜索结果
     * @param adapter
     */
    public static void registerSearchItem(@NonNull MultiTypeAdapter adapter) {
        adapter.register(MultiNewsArticleDataBean.class)
                .to(new NewsArticleImgViewBinder(),
                        new SearchArticleVideoViewBinder(),
                        new NewsArticleTextViewBinder())
                .withClassLinker(new ClassLinker<MultiNewsArticleDataBean>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<MultiNewsArticleDataBean, ?>> index(@NonNull MultiNewsArticleDataBean item) {
                        if (item.isHas_video()) {
                            return SearchArticleVideoViewBinder.class;
                        }
                        if (null != item.getImage_list() && item.getImage_list().size() > 0) {
                            return NewsArticleImgViewBinder.class;
                        }
                        return NewsArticleTextViewBinder.class;
                    }
                });
        adapter.register(LoadingBean.class, new LoadingViewBinder());
        adapter.register(LoadingEndBean.class, new LoadingEndViewBinder());
    }

}
