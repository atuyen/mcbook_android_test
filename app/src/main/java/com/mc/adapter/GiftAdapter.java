package com.mc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.customizes.gifts.ExpandableCardView;
import com.mc.models.gift.CategoryGift;
import com.mc.models.gift.Gift;
import com.mc.models.gift.GiftTemp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class GiftAdapter extends BaseRecycleAdapter<CategoryGift, GiftAdapter.GiftViewHolder> {

    private Context context;
    private Consumer<GiftTemp> lessonResponseConsumer;
    private Consumer<View> imgMoreConsumer;
    private Consumer<CategoryGift> categoryGiftConsumer;

    public GiftAdapter(Context context, Consumer<GiftTemp> lessonResponseConsumer, Consumer<View> imgMoreConsumer, Consumer<CategoryGift> categoryGiftConsumer) {
        this.context = context;
        this.lessonResponseConsumer = lessonResponseConsumer;
        this.imgMoreConsumer = imgMoreConsumer;
        this.categoryGiftConsumer = categoryGiftConsumer;
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expand_card_view_orange, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {
        try {
            CategoryGift categoryGift = listItems.get(position);
            holder.ecv.setTitle(categoryGift.getName());
            if (holder.ecv.isExpanded()) holder.ecv.collapse();
            List<Gift> gifts = categoryGift.getGifts();
                      GiftItemAdapter giftItemAdapter = new GiftItemAdapter(context, giftTemp -> {
                giftTemp.setGifts(gifts);
                giftTemp.setId(categoryGift.getId());
                giftTemp.setGiftUserId(categoryGift.getGiftUserId());
                lessonResponseConsumer.accept(giftTemp);
            }, view -> imgMoreConsumer.accept(view), v -> categoryGiftConsumer.accept(categoryGift));
            holder.rvGifts = holder.itemView.findViewById(R.id.rvItem);
            holder.rvGifts.setLayoutManager(new LinearLayoutManager(context));
            giftItemAdapter.setDataList(gifts);
            holder.rvGifts.setAdapter(giftItemAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class GiftViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ecv)
        ExpandableCardView ecv;
        @BindView(R.id.viewContainer)
        LinearLayout viewContainer;
        RecyclerView rvGifts;

        private GiftViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
