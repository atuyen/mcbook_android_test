package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.models.more.BookRanking;
import com.mc.utilities.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeadBroadBookAdapter extends BaseRecycleAdapter<BookRanking, LeadBroadBookAdapter.BookViewHolder> {
    private Context context;

    public LeadBroadBookAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_leadbroad_book, parent, false);
        return new BookViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        try {
            BookRanking bookRanking = listItems.get(position);
            AppUtils.setImageGlide(context, bookRanking.getAvatar() != null ? bookRanking.getAvatar() : "", R.drawable.ic_img_book_default, holder.imgBookAvatar);
            holder.txtIndex.setText(String.format("%d", position + 1));
            holder.txtBookName.setText(bookRanking.getName());
            holder.txtPoint.setText(String.format("%s %s", bookRanking.getPoint(), context.getString(R.string.points)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtIndex)
        ExtTextView txtIndex;
        @BindView(R.id.imgBookAvatar)
        ImageView imgBookAvatar;
        @BindView(R.id.txtBookName)
        ExtTextView txtBookName;
        @BindView(R.id.txtPoint)
        ExtTextView txtPoint;

        private BookViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
