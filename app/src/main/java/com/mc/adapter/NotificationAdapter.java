package com.mc.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.DateTimeUtils;
import com.mc.books.R;
import com.mc.customizes.recyclerview.ExtRecyclerViewAdapter;
import com.mc.customizes.recyclerview.ExtRecyclerViewHolder;
import com.mc.models.notification.Notification;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import java8.util.function.Consumer;

public class NotificationAdapter extends ExtRecyclerViewAdapter<Notification, NotificationAdapter.NotificationViewHolder> {
    private Context context;
    private Consumer<Notification> notificationConsumer;

    public NotificationAdapter(Context context, List<Notification> items, Consumer<Notification> consumer) {
        super(context, items);
        this.context = context;
        this.notificationConsumer = consumer;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_notification;
    }

    @Override
    protected NotificationViewHolder onCreateHolder(View view, int viewType) {
        return new NotificationViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationViewHolder holder, Notification notification, int position) {
        try {
            if (notification.getType().equals(Constant.KEY_TYPE_NOTI_MYBOOK)) {
                if (notification.getDetailMsg() != null) {
                    AppUtils.setImageGlide(context, notification.getDetailMsg().getAvatar() != null ?
                            notification.getDetailMsg().getAvatar() : "", R.mipmap.ic_launcher_round, holder.imgBookAvatar);
                    holder.txtTitleBook.setText(notification.getMessage());
                    holder.txtTitleBook.setTypeface(null, Typeface.NORMAL);
                }

            } else if (notification.getType().equals(Constant.KEY_TYPE_NOTI_MANUAL)) {
                if (notification.getDetailMsg() != null) {
                    AppUtils.setImageGlide(context, notification.getDetailMsg().getMediaUri() != null ?
                            notification.getDetailMsg().getMediaUri() : "", R.mipmap.ic_launcher_round, holder.imgBookAvatar);
                    holder.txtTitleBook.setText(notification.getMessage());
                    holder.txtTitleBook.setTypeface(null, Typeface.BOLD);
                }
            }
            holder.txtTitleTime.setText(DateTimeUtils.getTimeAgos(notification.getCreatedAt()));
            if (!notification.getIsRead())
                holder.llContaner.setBackground(context.getDrawable(R.drawable.bg_yellow_gradient));
            else {
                holder.txtTitleBook.setTypeface(null, Typeface.NORMAL);
                holder.llContaner.setBackground(context.getDrawable(R.drawable.bg_item_white));
            }
            holder.llContaner.setOnClickListener(v -> notificationConsumer.accept(notification));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class NotificationViewHolder extends ExtRecyclerViewHolder {
        @BindView(R.id.imgBookAvatar)
        CircleImageView imgBookAvatar;
        @BindView(R.id.txtTitleBook)
        ExtTextView txtTitleBook;
        @BindView(R.id.txtTitleTime)
        ExtTextView txtTitleTime;
        @BindView(R.id.llContaner)
        LinearLayout llContaner;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
