package com.mc.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.models.home.SubTitles;
import com.mc.utilities.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubtitlesAdapter extends BaseRecycleAdapter<SubTitles, SubtitlesAdapter.SubTitleViewHolder> {
    private Context context;
    private int selectedItem = -1;

    public SubtitlesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SubTitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subtitle_training, parent, false);
        return new SubTitleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubTitleViewHolder holder, int position) {
        try {
            SubTitles subTitles = listItems.get(position);
            holder.tvItemSub.setText(subTitles.getItemSub());
            setColorItemSub(subTitles, holder);
            holder.tvItemSub.setTextColor(context.getResources().getColor(subTitles.getColor()));
            if (position == selectedItem) {
                holder.tvItemSub.setTypeface(null, Typeface.BOLD_ITALIC);
                holder.tvItemSub.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_large));
            } else {
                holder.tvItemSub.setTypeface(null, Typeface.NORMAL);
                holder.tvItemSub.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_title));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setColorItemSub(SubTitles subTitles, SubTitleViewHolder holder) {
        if (subTitles.getItemSub().contains(Constant.KEY_DOT)) {
            String firtSub = subTitles.getItemSub().split(Constant.KEY_DOT)[0];
            String lastSub = subTitles.getItemSub().split(Constant.KEY_DOT)[1];
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString firstSpannable = new SpannableString(firtSub);
            SpannableString lastSpannable = new SpannableString(lastSub);
            firstSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, firtSub.length(), 0);
            builder.append(firstSpannable);
            builder.append(Constant.KEY_DOT);
            builder.append(lastSpannable);
            holder.tvItemSub.setText(builder, TextView.BufferType.SPANNABLE);
        } else {
            holder.tvItemSub.setTypeface(null, Typeface.NORMAL);
        }
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    class SubTitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvItemSub)
        TextView tvItemSub;

        private SubTitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

