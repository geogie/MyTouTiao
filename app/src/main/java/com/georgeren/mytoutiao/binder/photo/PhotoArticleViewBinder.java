package com.georgeren.mytoutiao.binder.photo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.georgeren.mytoutiao.ErrorAction;
import com.georgeren.mytoutiao.IntentAction;
import com.georgeren.mytoutiao.R;
import com.georgeren.mytoutiao.bean.photo.PhotoArticleBean;
import com.georgeren.mytoutiao.module.photo.content.PhotoContentActivity;
import com.georgeren.mytoutiao.utils.ImageLoader;
import com.georgeren.mytoutiao.utils.TimeUtil;
import com.georgeren.mytoutiao.widget.CircleImageView;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by georgeRen on 2017/8/23.
 * 图片：
 */

public class PhotoArticleViewBinder extends ItemViewBinder<PhotoArticleBean.DataBean, PhotoArticleViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_photo_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final PhotoArticleBean.DataBean item) {
        final Context context = holder.itemView.getContext();

        try {
            String tv_title = item.getTitle();

            if (!TextUtils.isEmpty(item.getMedia_avatar_url())) {
                ImageLoader.loadCenterCrop(context, ImageLoader.path2Url(item.getMedia_avatar_url()), holder.iv_media, R.color.viewBackground);
            }

            if (item.getImage_list() != null) {
                int size = item.getImage_list().size();
                String[] ivs = new String[size];
                for (int i = 0; i < item.getImage_list().size(); i++) {
                    ivs[i] = item.getImage_list().get(i).getUrl();
                }
                switch (ivs.length) {
                    case 1:
                        ImageLoader.loadCenterCrop(context, ImageLoader.path2Url(ivs[0]), holder.iv_0, R.color.viewBackground);
                        break;
                    case 2:
                        ImageLoader.loadCenterCrop(context, ImageLoader.path2Url(ivs[0]), holder.iv_0, R.color.viewBackground);
                        ImageLoader.loadCenterCrop(context, ImageLoader.path2Url(ivs[1]), holder.iv_1, R.color.viewBackground);
                        break;
                    case 3:
                        ImageLoader.loadCenterCrop(context, ImageLoader.path2Url(ivs[0]), holder.iv_0, R.color.viewBackground);
                        ImageLoader.loadCenterCrop(context, ImageLoader.path2Url(ivs[1]), holder.iv_1, R.color.viewBackground);
                        ImageLoader.loadCenterCrop(context, ImageLoader.path2Url(ivs[2]), holder.iv_2, R.color.viewBackground);
                        break;
                }
            }
            String tv_source = item.getSource();
            String tv_datetime = item.getBehot_time() + "";
            String comments_count = item.getComments_count() + "评论";
            if (!TextUtils.isEmpty(tv_datetime)) {
                tv_datetime = TimeUtil.getTimeStampAgo(tv_datetime);
            }
            holder.tv_title.setText(tv_title);
            holder.tv_extra.setText(tv_source + " - " + comments_count + " - " + tv_datetime);
            holder.iv_dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context,
                            holder.iv_dots, Gravity.END, 0, R.style.MyPopupMenu);
                    popupMenu.inflate(R.menu.menu_share);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menu) {
                            int itemId = menu.getItemId();
                            if (itemId == R.id.action_share) {
                                String shareUrl = item.getSource_url();
                                if (!shareUrl.contains("toutiao")) {
                                    shareUrl = "http://toutiao.com" + shareUrl;
                                }
                                IntentAction.send(context, item.getTitle() + "\n" + shareUrl);
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

            RxView.clicks(holder.itemView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Object o) throws Exception {
                            PhotoContentActivity.launch(item);
                        }
                    });
        } catch (Exception e) {
            ErrorAction.print(e);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView iv_media;
        private TextView tv_extra;
        private TextView tv_title;
        private ImageView iv_0;
        private ImageView iv_1;
        private ImageView iv_2;
        private ImageView iv_dots;

        public ViewHolder(View itemView) {
            super(itemView);
            this.iv_media = itemView.findViewById(R.id.iv_media);
            this.tv_extra = itemView.findViewById(R.id.tv_extra);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.iv_0 = itemView.findViewById(R.id.iv_0);
            this.iv_1 = itemView.findViewById(R.id.iv_1);
            this.iv_2 = itemView.findViewById(R.id.iv_2);
            this.iv_dots = itemView.findViewById(R.id.iv_dots);
        }
    }
}