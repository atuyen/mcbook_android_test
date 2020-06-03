package com.mc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.models.home.Author;
import com.mc.utilities.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AuthorAdapter extends BaseRecycleAdapter<Author, AuthorAdapter.AuthorViewHolder> {
    private Context context;

    public AuthorAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_author, parent, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        try {
            Author author = listItems.get(position);
            holder.txtAuthorName.setText(author.getName());
            AppUtils.setImageGlide(context, author.getImage(), R.drawable.ic_default_avatar, holder.imgAuthorAvatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class AuthorViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgAuthorAvatar)
        CircleImageView imgAuthorAvatar;
        @BindView(R.id.txtAuthorName)
        ExtTextView txtAuthorName;

        private AuthorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
