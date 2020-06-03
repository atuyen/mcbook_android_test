package com.mc.adapter;

import android.content.Context;
import android.graphics.Typeface;
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
import com.mc.customizes.subtitles.ExpandableCardViewSubTitle;
import com.mc.models.home.ItemTrainingResponse;
import com.mc.models.home.TrainingResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class TrainingAdapter extends BaseRecycleAdapter<TrainingResponse, TrainingAdapter.GiftViewHolder> {

    private Context context;
    private Consumer<ItemTrainingResponse> consumer;

    public TrainingAdapter(Context context, Consumer<ItemTrainingResponse> consumer) {
        this.context = context;
        this.consumer = consumer;
    }

    @NonNull
    @Override
    public TrainingAdapter.GiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expand_card_view_orange_sub, parent, false);
        return new TrainingAdapter.GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingAdapter.GiftViewHolder holder, int position) {
        try {
            TrainingResponse trainingResponse = listItems.get(position);
            holder.ecv.setFontTitle(trainingResponse.isRead());
            holder.ecv.setTitle(trainingResponse.getName());
            if (holder.ecv.isExpanded()) holder.ecv.collapse();
            List<ItemTrainingResponse> gifts = trainingResponse.getTrainings();
            ItemTrainingAdapter itemTrainingAdapter = new ItemTrainingAdapter(itemTrainingResponse -> consumer.accept(itemTrainingResponse));
            holder.rvTraining = holder.itemView.findViewById(R.id.rvItem);
            holder.rvTraining.setLayoutManager(new LinearLayoutManager(context));
            itemTrainingAdapter.setDataList(gifts);
            holder.rvTraining.setAdapter(itemTrainingAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class GiftViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ecv)
        ExpandableCardViewSubTitle ecv;
        @BindView(R.id.viewContainer)
        LinearLayout viewContainer;
        RecyclerView rvTraining;

        private GiftViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
