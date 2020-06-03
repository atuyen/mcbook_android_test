package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.customizes.recyclerview.ExtRecyclerViewAdapter;
import com.mc.customizes.recyclerview.ExtRecyclerViewHolder;
import com.mc.models.more.Members;
import com.mc.utilities.AppUtils;

import java.util.List;
import java8.util.function.Consumer;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ListMemberAdapter extends ExtRecyclerViewAdapter<Members, ListMemberAdapter.MemberViewHolder> {

    private Context context;
    private Consumer<Members> memberConsumer;

    public ListMemberAdapter(Context context, List<Members> membersList, Consumer<Members> memberConsumer) {
        super(context, membersList);
        this.context = context;
        this.memberConsumer = memberConsumer;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_list_member;
    }

    @Override
    protected MemberViewHolder onCreateHolder(View view, int viewType) {
        return new MemberViewHolder(view);
    }

    @SuppressLint({"NewApi", "DefaultLocale"})
    @Override
    protected void onBindViewHolder(@NonNull MemberViewHolder holder, Members member, int position) {
        try {
            AppUtils.setImageGlide(context, member.getAvatar() != null ? member.getAvatar() : "", R.drawable.ic_default_avatar, holder.imgBookAvatar);
            holder.txtIndex.setText(String.format("%d", position + 1));
            String lastName = member.getLastName() == null ? "" : member.getLastName();
            String firstName = member.getFirstName() == null ? "" : member.getFirstName();
            holder.txtBookName.setText(String.format("%s %s", lastName, firstName));
            holder.btnFollow.setText(member.getIsFriend() ? context.getString(R.string.unfollow) : context.getString(R.string.follow));
            holder.btnFollow.setBackground(member.getIsFriend() ? context.getDrawable(R.drawable.bg_btn_white) : context.getDrawable(R.drawable.bg_btn_nagative));
            holder.btnFollow.setTextColor(member.getIsFriend() ? context.getResources().getColor(R.color.colorDarkOrange) : context.getResources().getColor(R.color.colorWhite));
            holder.btnFollow.setTag(position);
            holder.btnFollow.setOnClickListener(v -> {
                if (member.getIsFriend()) {
                    memberConsumer.accept(member);
                    new Handler().post(() -> {
                        holder.btnFollow.setText(context.getString(R.string.follow));
                        holder.btnFollow.setBackground(context.getDrawable(R.drawable.bg_btn_nagative));
                        holder.btnFollow.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        member.setIsFriend(false);
                    });
                } else {
                    memberConsumer.accept(member);
                    new Handler().post(() -> {
                        holder.btnFollow.setText(context.getString(R.string.unfollow));
                        holder.btnFollow.setBackground(context.getDrawable(R.drawable.bg_btn_white));
                        holder.btnFollow.setTextColor(context.getResources().getColor(R.color.colorDarkOrange));
                        member.setIsFriend(true);
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MemberViewHolder extends ExtRecyclerViewHolder {
        @BindView(R.id.txtIndex)
        ExtTextView txtIndex;
        @BindView(R.id.imgBookAvatar)
        CircleImageView imgBookAvatar;
        @BindView(R.id.txtBookName)
        ExtTextView txtBookName;
        @BindView(R.id.btnFollow)
        Button btnFollow;

        private MemberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
