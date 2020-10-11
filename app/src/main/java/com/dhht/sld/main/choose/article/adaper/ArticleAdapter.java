package com.dhht.sld.main.choose.article.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhht.sld.R;
import com.dhht.sld.base.helper.ContextHelper;
import com.dhht.sld.main.choose.article.Bean.ArticleDataBean;
import com.dhht.sld.utlis.IconFontView;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<ArticleDataBean.ListArticle> data = new ArrayList<>();;
    private List<IconFontView> textViews = new ArrayList<>();
    private Context context = ContextHelper.getInstance().getApplicationContext();

    public ArticleAdapter() {}

    public void setData(List<ArticleDataBean.ListArticle> data) {
        this.data = data;
    }

    /**
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_choose_article,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    /**
     *
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position >= data.size()) return;
        ArticleDataBean.ListArticle listArticle = data.get(position);

        textViews.add(holder.articleIcon);
        holder.textTitle.setText(listArticle.name);

        if (listArticle.icon.length() > 0)
        {
            holder.articleIcon.setText(Html.fromHtml(listArticle.icon));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                for (IconFontView textOne: textViews)
                {
                    textOne.setTextColor(context.getResources().getColor(R.color.gray));
                }

                // 2 再设置选中颜色
                holder.articleIcon.setTextColor(context.getResources().getColor(R.color.appTheme));

                if (listener != null)  listener.onClick(listArticle.id, listArticle.name);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 内部Holder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        IconFontView articleIcon;

        public ViewHolder(View view) {
            super(view);
            textTitle = view.findViewById(R.id.article_text_title);
            articleIcon = view.findViewById(R.id.article_icon);
        }
    }

    /**
     * item点击回调
     */
    public interface OnItemClickListener {
        void onClick(int id, String articleName);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
