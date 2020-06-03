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
import com.mc.models.more.CategoryLeadBoad;
import com.mc.models.more.Filter;
import com.mc.utilities.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java8.util.function.Consumer;


public class FilterBookDialog extends Dialog {
    @BindView(R.id.imgClose)
    ImageView imgClose;
    @BindView(R.id.llComboboxBook)
    LinearLayout llComboboxBook;
    @BindView(R.id.btnContinue)
    ExtButton btnContinue;
    @BindView(R.id.grgBook)
    GridRadioGroup grgBook;
    @BindView(R.id.grgTime)
    GridRadioGroup grgTime;
    @BindView(R.id.txtComboboxBook)
    ExtTextView txtComboboxBook;
    private Context context;
    private Consumer<Filter> filterConsumer;
    private FragmentManager fragmentManager;
    private List<CategoryLeadBoad> categoryLeadBoadList;
    private Consumer<String> keywordConsumer;
    private ExtKeyValuePairDialogFragment extKeyValuePairDialogFragment;
    private String categoryId;
    private String fillterBook;
    private String fillterTime;
    private List<CategoryLeadBoad> categoryLeadBoadListBox;
    private Consumer<Filter> filterConsumerUpdate;


    public FilterBookDialog(@NonNull Context context, Consumer<Filter> filter, int themeResId, FragmentManager fragmentManager, List<CategoryLeadBoad> categoryLeadBoadList,
                            List<CategoryLeadBoad> categoryLeadBoadListBox,
                            Consumer<String> keywordConsumer, Consumer<Filter> filterConsumerUpdate) {
        super(context, themeResId);
        this.context = context;
        this.filterConsumer = filter;
        this.fragmentManager = fragmentManager;
        this.categoryLeadBoadList = categoryLeadBoadList;
        this.keywordConsumer = keywordConsumer;
        this.categoryLeadBoadListBox = categoryLeadBoadListBox;
        this.filterConsumerUpdate = filterConsumerUpdate;
    }

    public void setDataSearch(List<CategoryLeadBoad> categoryLeadBoadsList) {
        this.categoryLeadBoadListBox = categoryLeadBoadsList;
        ArrayList<ExtKeyValuePair> allFilter = new ArrayList<>();
        allFilter.add(new ExtKeyValuePair(Constant.ALL_FILTER, context.getString(R.string.all)));
        for (CategoryLeadBoad categoryLeadBoad : categoryLeadBoadListBox) {
            CategoryLeadBoad categoryLeadBoad1 = new CategoryLeadBoad();
            categoryLeadBoad1.setId(categoryLeadBoad.getId());
            categoryLeadBoad1.setName(categoryLeadBoad.getName());
            allFilter.add(new ExtKeyValuePair(String.valueOf(categoryLeadBoad1.getId()), categoryLeadBoad1.getName()));
        }
        extKeyValuePairDialogFragment.setDataList(allFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_filter_book);
        ButterKnife.bind(this);
        grgBook.setDataRadio(Arrays.asList(context.getString(R.string.my_book), context.getString(R.string.all)));
        grgTime.setDataRadio(Arrays.asList(context.getString(R.string.days), context.getString(R.string.week), context.getString(R.string.month), context.getString(R.string.year)));
    }

    @OnClick(R.id.imgClose)
    void onClose() {
        dismiss();
    }

    @OnClick(R.id.btnContinue)
    void onSubmit() {
        Filter filter = new Filter();
        if (grgBook.getValueCheckedItem().equals(context.getString(R.string.my_book)))
            fillterBook = Constant.BOOK_FILTER;
        else if (grgBook.getValueCheckedItem().equals(context.getString(R.string.all)))
            fillterBook = Constant.ALL_FILTER;

        if (txtComboboxBook.getText().toString().equals(context.getString(R.string.all)))
            categoryId = Constant.ALL_FILTER;

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
        filter.setCategory(categoryId);
        filterConsumer.accept(filter);
        filterConsumerUpdate.accept(filter);
        dismiss();
    }

    @OnClick(R.id.llComboboxBook)
    void onComboBox() {
        ArrayList<ExtKeyValuePair> filter = new ArrayList<>();
        filter.add(new ExtKeyValuePair(Constant.ALL_FILTER, context.getString(R.string.all)));
        for (CategoryLeadBoad categoryLeadBoad : categoryLeadBoadList) {
            CategoryLeadBoad categoryLeadBoad1 = new CategoryLeadBoad();
            categoryLeadBoad1.setId(categoryLeadBoad.getId());
            categoryLeadBoad1.setName(categoryLeadBoad.getName());
            filter.add(new ExtKeyValuePair(String.valueOf(categoryLeadBoad1.getId()), categoryLeadBoad1.getName()));
        }
        extKeyValuePairDialogFragment = new ExtKeyValuePairDialogFragment();
        extKeyValuePairDialogFragment.setExtKeyValuePairs(filter);
        extKeyValuePairDialogFragment.setOnSelectedConsumer(keyValuePair -> {
            txtComboboxBook.setText(keyValuePair.getValue());
            categoryId = keyValuePair.getKey();
        });
        extKeyValuePairDialogFragment.setVisibleSearchAPI(true);
        extKeyValuePairDialogFragment.onSearch(keyword -> keywordConsumer.accept(keyword)).show(fragmentManager, null);
    }
}
