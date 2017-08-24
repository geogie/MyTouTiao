package com.georgeren.mytoutiao.binder.joke;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.georgeren.mytoutiao.ErrorAction;
import com.georgeren.mytoutiao.R;
import com.georgeren.mytoutiao.bean.joke.JokeContentBean;
import com.georgeren.mytoutiao.utils.ImageLoader;
import com.georgeren.mytoutiao.widget.CircleImageView;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by georgeRen on 2017/8/22.
 * 段子头部
 */

public class JokeCommentHeaderViewBinder extends ItemViewBinder<JokeContentBean.DataBean.GroupBean, JokeCommentHeaderViewBinder.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_joke_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull JokeContentBean.DataBean.GroupBean item) {
        final Context context = holder.itemView.getContext();

        try {
            String avatar_url = item.getUser().getAvatar_url();
            String name = item.getUser().getName();
            String text = item.getText();
            String digg_count = item.getDigg_count() + "";
            String bury_count = item.getBury_count() + "";
            int comment_count = item.getComment_count();

            ImageLoader.loadCenterCrop(context, avatar_url, holder.iv_avatar, R.color.viewBackground);
            holder.tv_username.setText(name);
            holder.tv_text.setText(text);
            holder.tv_digg_count.setText(digg_count);
            holder.tv_bury_count.setText(bury_count);
            if (comment_count > 0) {
                holder.tv_comment_count.setText(comment_count + "评论");
            } else {
                holder.tv_comment_count.setVisibility(View.GONE);
            }
            holder.iv_dots.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {// 复制内容、分享内容
                @Override
                public void onClick(View v) {
//                    final String content = item.getText();
//                    final BottomSheetDialogFixed dialog = new BottomSheetDialogFixed(context);
//                    dialog.setOwnerActivity((BaseActivity) context);
//                    View view = ((BaseActivity) context).getLayoutInflater().inflate(R.layout.item_comment_action_sheet, null);
//                    view.findViewById(R.id.layout_copy_text).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ClipboardManager copy = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//                            ClipData clipData = ClipData.newPlainText("text", content);
//                            copy.setPrimaryClip(clipData);
//                            Snackbar.make(holder.itemView, R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                    });
//                    view.findViewById(R.id.layout_share_text).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            IntentAction.send(context, content);
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog.setContentView(view);
//                    dialog.show();
                }
            });
        } catch (Exception e) {
            ErrorAction.print(e);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView iv_avatar;
        private TextView tv_username;
        private TextView tv_text;
        private TextView tv_digg_count;
        private TextView tv_bury_count;
        private TextView tv_comment_count;
        private ImageView iv_dots;

        ViewHolder(View itemView) {
            super(itemView);
            this.iv_avatar = itemView.findViewById(R.id.iv_avatar);
            this.tv_username = itemView.findViewById(R.id.tv_username);
            this.tv_text = itemView.findViewById(R.id.tv_text);
            this.tv_digg_count = itemView.findViewById(R.id.tv_digg_count);
            this.tv_bury_count = itemView.findViewById(R.id.tv_bury_count);
            this.tv_comment_count = itemView.findViewById(R.id.tv_comment_count);
            this.iv_dots = itemView.findViewById(R.id.iv_dots);
        }
    }
}
