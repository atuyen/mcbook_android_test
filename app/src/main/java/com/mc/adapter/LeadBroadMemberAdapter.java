package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.models.more.UserRanking;
import com.mc.utilities.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LeadBroadMemberAdapter extends BaseRecycleAdapter<UserRanking, LeadBroadMemberAdapter.BookViewHolder> {
    private Context context;
    private String userId;
    private boolean isSelected = false;

    public LeadBroadMemberAdapter(Context context, String userId) {
        this.context = context;
        this.userId = userId;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_leadbroad_member, parent, false);
        return new BookViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        try {
            UserRanking member = listItems.get(position);
            AppUtils.setImageGlide(context, member.getAvatar() != null ? member.getAvatar() : "", R.drawable.ic_default_avatar, holder.imgBookAvatar);
            String lastName = member.getLastName() != null ? member.getLastName() : "";
            String firstName = member.getFirstName() != null ? member.getFirstName() : "";
            if (member.getId().equals(userId) && !isSelected) {
                isSelected = true;
                holder.txtIndex.setText(String.format("%d", member.getRank()));
                holder.llContainer.setBackground(context.getDrawable(R.drawable.bg_item_white));
            } else {
                isSelected = false;
                holder.txtIndex.setText(String.format("%d", position + 1));
                holder.llContainer.setBackground(context.getDrawable(R.drawable.bg_yellow_gradient_90));
            }
            holder.txtBookName.setText(String.format("%s %s", lastName, firstName));
            holder.txtPoint.setText(String.format("%s %s", member.getPoint(), context.getString(R.string.points)));
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
        CircleImageView imgBookAvatar;
        @BindView(R.id.txtBookName)
        ExtTextView txtBookName;
        @BindView(R.id.txtPoint)
        ExtTextView txtPoint;
        @BindView(R.id.llContainer)
        LinearLayout llContainer;

        private BookViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
