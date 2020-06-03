package com.mc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bon.customview.textview.ExtTextView;
import com.bumptech.glide.Glide;
import com.mc.books.R;
import com.mc.common.adapters.BaseRecycleAdapter;
import com.mc.models.gift.Gift;
import com.mc.models.gift.GiftTemp;
import com.mc.utilities.AppUtils;
import com.mc.utilities.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class GiftItemAdapter extends BaseRecycleAdapter<Gift, RecyclerView.ViewHolder> {

    private Context context;
    private Consumer<GiftTemp> giftResponseConsumer;
    private Consumer<View> imgMoreConsumer;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_CONTENT = 1;
    private Consumer<String> deleteGiftConsumer;


    public GiftItemAdapter(Context context, Consumer<GiftTemp> giftResponseConsumer, Consumer<View> imgMoreConsumer, Consumer<String> deleteGiftConsumer) {
        this.context = context;
        this.giftResponseConsumer = giftResponseConsumer;
        this.imgMoreConsumer = imgMoreConsumer;
        this.deleteGiftConsumer = deleteGiftConsumer;
    }

    @Override
    public int getItemCount() {
        if (listItems != null) return listItems.size() + 1;
        return listItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == listItems.size())
            return TYPE_FOOTER;
        else
            return TYPE_CONTENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CONTENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_lesson, parent, false);
            return new GiftItemAdapter.GiftViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_footer_gift, parent, false);
            return new GiftItemAdapter.GiftFooterViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        try {
            if (holder instanceof GiftFooterViewHolder)
                ((GiftFooterViewHolder) holder).txtDelete.setOnClickListener(v -> deleteGiftConsumer.accept(""));
            else if (holder instanceof GiftViewHolder) {
                Gift gift = listItems.get(position);
                if (gift != null) {
                    Glide.with(context).clear(((GiftViewHolder) holder).imgTypeGift);
                    ((GiftViewHolder) holder).imgTypeGift.setVisibility(View.GONE);
                    ((GiftViewHolder) holder).rlTypeExam.setVisibility(View.GONE);
                    ((GiftViewHolder) holder).txtGiftLevel.setVisibility(View.GONE);
                    if (gift.getType() != null && gift.getType().equals(Constant.KEY_VIDEO)) {
                        ((GiftViewHolder) holder).imgTypeGift.setVisibility(View.VISIBLE);
                        Glide.with(context).load(getImage("ic_type_video")).into(((GiftViewHolder) holder).imgTypeGift);
                    } else if (gift.getType() != null && gift.getType().equals(Constant.KEY_AUDIO)) {
                        ((GiftViewHolder) holder).imgTypeGift.setVisibility(View.VISIBLE);
                        Glide.with(context).load(getImage("ic_type_audio")).into(((GiftViewHolder) holder).imgTypeGift);
                    } else if (gift.getType() != null && gift.getType().equals(Constant.KEY_PDF)) {
                        ((GiftViewHolder) holder).imgTypeGift.setVisibility(View.VISIBLE);
                        Glide.with(context).load(getImage("ic_type_pdf")).into(((GiftViewHolder) holder).imgTypeGift);
                    } else if (gift.getType() != null && gift.getType().equals(Constant.KEY_EXAM)) {
                        ((GiftViewHolder) holder).imgTypeGift.setVisibility(View.GONE);
                        ((GiftViewHolder) holder).rlTypeExam.setVisibility(View.VISIBLE);
                        ((GiftViewHolder) holder).txtGiftLevel.setVisibility(View.VISIBLE);

                        String level = context.getString(R.string.level_exam) + " ";
                        if (gift.getExamData().getLevel() != null) {
                            switch (gift.getExamData().getLevel()) {
                                case Constant.EASY_EXAMS:
                                    level += (context.getString(R.string.easy));
                                    break;
                                case Constant.MEDIUM_EXAMS:
                                    level += context.getString(R.string.medium);
                                    break;
                                case Constant.HARD_EXAMS:
                                    level += context.getString(R.string.hard);
                                    break;
                                case Constant.HARDEST_EXAMS:
                                    level += context.getString(R.string.hardest);
                                    break;
                            }
                        }

                        ((GiftViewHolder) holder).txtGiftLevel.setText(level);
                        ((GiftViewHolder) holder).txtTimeExam.setText(gift.getExamData().getTime() + "'");
                    }
                    AppUtils.setImageGlide(context, gift.getCoverUri(), R.drawable.ic_default_gift, ((GiftViewHolder) holder).imgGift);
                    ((GiftViewHolder) holder).txtGiftName.setText(gift.getName());
                    ((GiftViewHolder) holder).txtGiftAuthor.setText(renderType(gift));
                    ((GiftViewHolder) holder).imgNew.setVisibility(gift.getNew() ? View.VISIBLE : View.INVISIBLE);
                    ((GiftViewHolder) holder).cvGift.setOnClickListener(v -> {
                        GiftTemp giftTemp = new GiftTemp();
                        giftTemp.setIndex(position);
                        giftTemp.setGift(gift);
                        giftResponseConsumer.accept(giftTemp);
                    });
                    ((GiftViewHolder) holder).imgMore.setOnClickListener(v -> imgMoreConsumer.accept(((GiftViewHolder) holder).imgMore));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getImage(String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

    private String renderType(Gift gift) {
        if (gift.getType() != null) {
            switch (gift.getType()) {
                case Constant.KEY_AUDIO:
                    return context.getString(R.string.audio);
                case Constant.KEY_VIDEO:
                    return context.getString(R.string.video);
                case Constant.KEY_PDF:
                    return context.getString(R.string.document);
                case Constant.KEY_COURSE:
                    return gift.getBookAuthor() != null ? gift.getBookAuthor() : "";
                case Constant.KEY_EXAM:
                    return gift.getExamData().getSubjectName();
                default:
                    return "";
            }
        }
        return null;
    }

    class GiftViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cvGift)
        CardView cvGift;
        @BindView(R.id.imgMore)
        ImageView imgMore;
        @BindView(R.id.txtGiftName)
        ExtTextView txtGiftName;
        @BindView(R.id.txtGiftAuthor)
        ExtTextView txtGiftAuthor;
        @BindView(R.id.imgNext)
        ImageView imgNext;
        @BindView(R.id.imgGift)
        AppCompatImageView imgGift;
        @BindView(R.id.imgNew)
        ImageView imgNew;
        @BindView(R.id.imgTypeGift)
        ImageView imgTypeGift;
        @BindView(R.id.layout_contain)
        RelativeLayout rlContainer;
        @BindView(R.id.rlTypeExam)
        RelativeLayout rlTypeExam;
        @BindView(R.id.txtTimeExam)
        ExtTextView txtTimeExam;
        @BindView(R.id.txtGiftLevel)
        ExtTextView txtGiftLevel;

        private GiftViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class GiftFooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtDelete)
        ExtTextView txtDelete;


        private GiftFooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
} 

