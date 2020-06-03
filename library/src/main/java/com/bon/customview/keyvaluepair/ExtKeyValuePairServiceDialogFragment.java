package com.bon.customview.keyvaluepair;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bon.customview.edittext.ExtEditText;
import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.fragment.ExtBaseBottomDialogFragment;
import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.KeyboardUtils;
import com.bon.util.StringUtils;

import java8.util.stream.StreamSupport;

/**
 * Created by Administrator on 12/01/2017.
 */

public class ExtKeyValuePairServiceDialogFragment<AD extends ExtBaseAdapter<T>, T extends ExtKeyValuePair> extends ExtBaseBottomDialogFragment {
    private static final String TAG = ExtKeyValuePairServiceDialogFragment.class.getSimpleName();

    // instance
    public static ExtKeyValuePairServiceDialogFragment newInstance() {
        return new ExtKeyValuePairServiceDialogFragment();
    }

    // view
    private RelativeLayout trSearch;
    private ExtTextView tvCancel;
    private ExtTextView tvTitle;
    private ExtEditText edtSearch;
    private ExtPagingListView<T> lvData;
    private ImageView ivCancel;

    // variable
    private AD adapter;
    private ExtKeyValuePairListener<T> actionListener;
    private String titleDialog;
    private boolean isVisibleFilter;
    private int index = 0;
    private int indexSelected = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.key_value_pair_service_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            // view
            this.tvTitle = view.findViewById(R.id.tvTitle);
            this.tvCancel = view.findViewById(R.id.tvCancel);
            this.tvCancel.setOnClickListener(v -> onClickCancel());
            this.ivCancel = view.findViewById(R.id.ivCancel);
            this.trSearch = view.findViewById(R.id.trSearch);
            this.edtSearch = view.findViewById(R.id.edtSearch);
            this.lvData = view.findViewById(R.id.lvData);

            // setup view
            this.setUpView();

            // load default data
            if (this.actionListener != null) this.actionListener.loadData(this);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * set up view
     */
    private void setUpView() {
        try {
            // don't show keyboard
            this.getDialog().setOnShowListener(dialog -> KeyboardUtils.hideKeyboard(getActivity(), this.edtSearch));

            // set title app
            this.tvTitle.setText(StringUtils.isEmpty(titleDialog) ? getString(R.string.select_value) : titleDialog);

            // show or hide search view
            this.trSearch.setVisibility(this.isVisibleFilter ? View.VISIBLE : View.GONE);
            this.edtSearch.setOnEditorActionListener((v, actionId, event) -> {
                try {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        // hide keyboard
                        KeyboardUtils.hideKeyboard(getActivity(), edtSearch);

                        // clear data
                        this.lvData.clearItems();

                        // load data
                        if (this.actionListener != null) {
                            this.actionListener.loadData(this);
                        }

                        return true;
                    }
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }

                return false;
            });

            // search
            this.edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        ivCancel.setVisibility(StringUtils.isEmpty(String.valueOf(charSequence)) ? View.GONE : View.VISIBLE);
                    } catch (Exception e) {
                        Logger.e(TAG, e);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            // cancel search
            this.ivCancel.setOnClickListener(view -> {
                try {
                    // hide keyboard
                    KeyboardUtils.hideKeyboard(getActivity(), edtSearch);

                    // clear search condition
                    this.edtSearch.setText("");

                    // clear data
                    this.lvData.clearItems();

                    // load data
                    if (this.actionListener != null) {
                        this.actionListener.loadData(this);
                    }
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });

            // adapter
            if (this.adapter == null) {
                throw new NullPointerException("Adapter can not null!!!");
            }

            // init data list view
            this.lvData.init(this.getContext(), this.adapter)
                    .setOnExtClickListener((position, item) -> {
                        try {
                            if (actionListener != null) {
                                actionListener.onClickItem((T) item);
                            }

                            dismiss();
                        } catch (Exception e) {
                            Logger.e(TAG, e);
                        }
                    })
                    .setOnExtLoadMoreListener(() -> {
                        // load more
                        if (actionListener != null) {
                            actionListener.loadMoreData(this);
                        }
                    })
                    .setEnabledSwipeRefreshing(true)
                    .setOnExtRefreshListener(() -> {
                        this.lvData.clearItems();
                        if (this.actionListener != null) {
                            this.actionListener.loadData(this);
                        }
                    });
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * click cancel
     */
    void onClickCancel() {
        try {
            if (actionListener != null) {
                actionListener.onClickItem(null);
            }

            dismiss();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * set adapter
     *
     * @param adapter
     * @return
     */
    public ExtKeyValuePairServiceDialogFragment setUpAdapter(AD adapter) {
        try {
            this.adapter = adapter;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * set selected
     *
     * @param item
     * @return
     */
    public ExtKeyValuePairServiceDialogFragment setSelectedItem(T item) {
        try {
            if (this.adapter == null || this.adapter.getCount() <= 0 || item == null) {
                return this;
            }

            // set selected
            this.indexSelected = 0;
            this.index = 0;
            StreamSupport.stream(this.adapter.getItems()).forEach(n -> {
                if (item.getKey().equalsIgnoreCase(n.getKey())) {
                    n.setSelected(true);
                    this.indexSelected = index;
                } else {
                    n.setSelected(false);
                }

                this.index++;
            });

            // notification
            this.lvData.notifyDataSetChanged(this.adapter.getItems());
            this.lvData.setSelection(this.indexSelected);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * set action listener
     *
     * @param actionListener
     * @return
     */
    public ExtKeyValuePairServiceDialogFragment setActionListener(ExtKeyValuePairListener<T> actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    /**
     * set visible or hide view
     *
     * @param visibleFilter
     * @return
     */
    public ExtKeyValuePairServiceDialogFragment setVisibleFilter(boolean visibleFilter) {
        this.isVisibleFilter = visibleFilter;
        return this;
    }

    /**
     * set title dialog
     *
     * @param titleDialog
     * @return
     */
    public ExtKeyValuePairServiceDialogFragment setTitleDialog(String titleDialog) {
        this.titleDialog = titleDialog;
        return this;
    }

    /**
     * get list view data
     *
     * @return
     */
    public ExtPagingListView getPagingListView() {
        if (this.lvData == null) {
            throw new NullPointerException("PagingListView can not null!!!");
        }

        return this.lvData;
    }

    /**
     * get view search
     *
     * @return
     */
    public ExtEditText getEditTextSearch() {
        if (this.edtSearch == null) {
            throw new NullPointerException("EditText can not null!!!");
        }

        return this.edtSearch;
    }

    /**
     * @return
     */
    public String getConditionText() {
        try {
            return this.edtSearch.getText().toString();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }
}

