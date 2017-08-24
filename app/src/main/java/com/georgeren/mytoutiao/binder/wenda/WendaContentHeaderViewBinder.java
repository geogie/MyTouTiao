package com.georgeren.mytoutiao.binder.wenda;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.georgeren.mytoutiao.ErrorAction;
import com.georgeren.mytoutiao.R;
import com.georgeren.mytoutiao.bean.wenda.WendaContentBean;
import com.georgeren.mytoutiao.utils.SettingUtil;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by georgeRen on 2017/8/22.
 * 问答标题item：
 */

public class WendaContentHeaderViewBinder extends ItemViewBinder<WendaContentBean.QuestionBean, WendaContentHeaderViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_wenda_content_header, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull WendaContentBean.QuestionBean item) {
        try {
            String tv_title = item.getTitle();
            String tv_abstract = item.getContent().getText();

            String tv_answer_count = item.getNormal_ans_count() + item.getNice_ans_count() + " 回答";
            String tv_follow_count = item.getFollow_count() + " 关注";
            holder.tv_title.setText(tv_title);
            if (!TextUtils.isEmpty(tv_abstract)) {
                holder.tv_abstract.setText(tv_abstract);
            } else {
                holder.tv_abstract.setVisibility(View.GONE);
            }
            holder.tv_answer_count.setText(tv_answer_count);
            holder.tv_follow_count.setText(tv_follow_count);
            holder.title_view.setBackgroundColor(SettingUtil.getInstance().getColor());
        } catch (Exception e) {
            ErrorAction.print(e);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_abstract;
        private TextView tv_answer_count;
        private TextView tv_follow_count;
        private LinearLayout title_view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_abstract = itemView.findViewById(R.id.tv_abstract);
            this.tv_answer_count = itemView.findViewById(R.id.tv_answer_count);
            this.tv_follow_count = itemView.findViewById(R.id.tv_follow_count);
            this.title_view = itemView.findViewById(R.id.title_view);
        }
    }
}
