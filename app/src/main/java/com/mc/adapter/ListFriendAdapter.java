package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.customizes.recyclerview.ExtRecyclerViewAdapter;
import com.mc.customizes.recyclerview.ExtRecyclerViewHolder;
import com.mc.models.more.Friend;
import com.mc.utilities.AppUtils;

import java.util.List;
import java8.util.function.Consumer;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ListFriendAdapter extends ExtRecyclerViewAdapter<Friend, ListFriendAdapter.FriendViewHolder> {

    private Context context;
    private Consumer<String> idConsumer;

    public ListFriendAdapter(Context context, List<Friend> friendList, Consumer<String> idConsumer) {
        super(context, friendList);
        this.context = context;
        this.idConsumer = idConsumer;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_list_friend;
    }

    @Override
    protected FriendViewHolder onCreateHolder(View view, int viewType) {
        return new FriendViewHolder(view);
    }

    @SuppressLint({"NewApi", "DefaultLocale"})
    @Override
    protected void onBindViewHolder(@NonNull FriendViewHolder holder, Friend friend, int position) {
        try {
            AppUtils.setImageGlide(context, friend.getAvatar() != null ? friend.getAvatar() : "", R.drawable.ic_default_avatar, holder.imgBookAvatar);
            holder.txtIndex.setText(String.format("%d", position + 1));
            String lastName = friend.getLastName() == null ? "" : friend.getLastName();
            String firstName = friend.getFirstName() == null ? "" : friend.getFirstName();
            holder.txtBookName.setText(String.format("%s %s", lastName, firstName));
            holder.btnFollow.setText(context.getString(R.string.unfollow));
            holder.btnFollow.setBackground(context.getDrawable(R.drawable.bg_btn_white));
            holder.btnFollow.setTextColor(context.getResources().getColor(R.color.colorDarkOrange));
            holder.btnFollow.setTag(position);
            holder.btnFollow.setOnClickListener(v -> {
                int positionRemove = (int) v.getTag();
                if (positionRemove <= items.size() - 1) {
                    items.remove(positionRemove);
                    notifyDataSetChanged();
                }
                idConsumer.accept(friend.getId());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class FriendViewHolder extends ExtRecyclerViewHolder {
        @BindView(R.id.txtIndex)
        ExtTextView txtIndex;
        @BindView(R.id.imgBookAvatar)
        CircleImageView imgBookAvatar;
        @BindView(R.id.txtBookName)
        ExtTextView txtBookName;
        @BindView(R.id.btnFollow)
        Button btnFollow;

        private FriendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
