package com.mc.books.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bon.customview.button.ExtButton;
import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.customizes.GridRadioGroup;
import com.mc.models.more.BookLeadBoad;
import com.mc.models.more.Filter;
import com.mc.utilities.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java8.util.function.Consumer;


public class FilterMemberDialog extends Dialog {
    @BindView(R.id.imgClose)
    ImageView imgClose;
    @BindView(R.id.llComboboxBook)
    LinearLayout llComboboxBook;
    @BindView(R.id.btnContinue)
    ExtButton btnContinue;
    @BindView(R.id.grgMember)
    GridRadioGroup grgMember;
    @BindView(R.id.grgTime)
    GridRadioGroup grgTime;
    @BindView(R.id.txtComboboxBook)
    ExtTextView txtComboboxBook;
    private String fillterUser;
    private String fillterBook;
    private String fillterTime;
    private Context context;
    private Consumer<Filter> filterConsumer;
    private FragmentManager fragmentManager;
    private List<BookLeadBoad> bookLeadBoadsList;
    private List<BookLeadBoad> bookLeadBoadListBox;
    private Consumer<String> keywordConsumer;
    private Consumer<Filter> filterConsumerUpdate;
    private ExtKeyValuePairDialogFragment extKeyValuePairDialogFragment;

    public FilterMemberDialog(@NonNull Context context, Consumer<Filter> filter, int themeResId, FragmentManager fragmentManager,
                              List<BookLeadBoad> bookLeadBoads, List<BookLeadBoad> bookLeadBoadListBox, Consumer<String> keywordConsumer, Consumer<Filter> filterConsumerUpdate) {
        super(context, themeResId);
        this.context = context;
        this.filterConsumer = filter;
        this.fragmentManager = fragmentManager;
        this.bookLeadBoadsList = bookLeadBoads;
        this.keywordConsumer = keywordConsumer;
        this.bookLeadBoadListBox = bookLeadBoadListBox;
        this.filterConsumerUpdate = filterConsumerUpdate;
    }

    public void setDataSearch(List<BookLeadBoad> leadBoadList) {
        this.bookLeadBoadListBox = leadBoadList;
        ArrayList<ExtKeyValuePair> filter = new ArrayList<>();
        filter.add(new ExtKeyValuePair(Constant.ALL_FILTER, context.getString(R.string.all)));
        for (BookLeadBoad bookLeadBoad : bookLeadBoadListBox) {
            BookLeadBoad bookLeadBoad1 = new BookLeadBoad();
            bookLeadBoad1.setCategoryId(bookLeadBoad.getCategoryId());
            bookLeadBoad1.setName(bookLeadBoad.getName());
            filter.add(new ExtKeyValuePair(String.valueOf(bookLeadBoad1.getCategoryId()), bookLeadBoad1.getName()));
        }
        extKeyValuePairDialogFragment.setDataList(filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_filter_member);
        ButterKnife.bind(this);
        grgMember.setDataRadio(Arrays.asList(context.getString(R.string.friend), context.getString(R.string.all)));
        grgTime.setDataRadio(Arrays.asList(context.getString(R.string.days), context.getString(R.string.week), context.getString(R.string.month), context.getString(R.string.year)));
    }

    @OnClick(R.id.imgClose)
    void onClose() {
        dismiss();
    }

    @OnClick(R.id.btnContinue)
    void onSubmit() {
        Filter filter = new Filter();
        if (grgMember.getValueCheckedItem().equals(context.getString(R.string.friend)))
            fillterUser = Constant.FRIEND_FILTER;
        else if (grgMember.getValueCheckedItem().equals(context.getString(R.string.all)))
            fillterUser = Constant.ALL_FILTER;

        if (txtComboboxBook.getText().toString().equals(context.getString(R.string.all)))
            fillterBook = Constant.ALL_FILTER;

        if (grgTime.getValueCheckedItem().equals(context.getString(R.string.days)))
            fillterTime = Constant.DATE_FILTER;
        else if (grgTime.getValueCheckedItem().equals(context.getString(R.string.week)))
            fillterTime = Constant.WEEK_FILTER;
        else if (grgTime.getValueCheckedItem().equals(context.getString(R.string.month)))
            fillterTime = Constant.MONTH_FILTER;
        else if (grgTime.getValueCheckedItem().equals(context.getString(R.string.year)))
            fillterTime = Constant.YEAR_FILTER;

        filter.setFilterTime(fillterTime);
        filter.setFilterBook(fillterBook);
        filter.setFilterUser(fillterUser);
        filterConsumer.accept(filter);
        filterConsumerUpdate.accept(filter);
        dismiss();
    }

    @OnClick(R.id.llComboboxBook)
    void onComboBox() {
        ArrayList<ExtKeyValuePair> filter = new ArrayList<>();
        filter.add(new ExtKeyValuePair(Constant.ALL_FILTER, context.getString(R.string.all)));
        for (BookLeadBoad bookLeadBoad : bookLeadBoadsList) {
            BookLeadBoad bookLeadBoad1 = new BookLeadBoad();
            bookLeadBoad1.setCategoryId(bookLeadBoad.getCategoryId());
            bookLeadBoad1.setName(bookLeadBoad.getName());
            filter.add(new ExtKeyValuePair(String.valueOf(bookLeadBoad1.getCategoryId()), bookLeadBoad1.getName()));
        }
        extKeyValuePairDialogFragment = new ExtKeyValuePairDialogFragment();
        extKeyValuePairDialogFragment.setExtKeyValuePairs(filter);
        extKeyValuePairDialogFragment.setOnSelectedConsumer(keyValuePair -> {
            txtComboboxBook.setText(keyValuePair.getValue());
            fillterBook = keyValuePair.getKey();
        });
        extKeyValuePairDialogFragment.setVisibleSearchAPI(true);
        extKeyValuePairDialogFragment.onSearch(keyword -> keywordConsumer.accept(keyword)).show(fragmentManager, null);
    }
}
