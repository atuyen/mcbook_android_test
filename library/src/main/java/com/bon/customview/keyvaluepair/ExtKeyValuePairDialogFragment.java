package com.bon.customview.keyvaluepair;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.bon.customview.edittext.ExtEditText;
import com.bon.customview.listview.ExtListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.fragment.ExtBaseBottomDialogFragment;
import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.KeyboardUtils;
import com.bon.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import java8.util.function.Consumer;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

/**
 * Created by Administrator on 12/01/2017.
 */

public class ExtKeyValuePairDialogFragment extends ExtBaseBottomDialogFragment {
    private static final String TAG = ExtKeyValuePairDialogFragment.class.getSimpleName();

    // instance
    public static ExtKeyValuePairDialogFragment newInstance() {
        return new ExtKeyValuePairDialogFragment();
    }

    private String value;
    private List<ExtKeyValuePair> extKeyValuePairs;
    private List<ExtKeyValuePair> extKeyValuePairsOrigins;
    private boolean isVisibleFilter;
    private boolean isVisibleSearchAPI;

    private ExtKeyValuePairAdapter<ExtKeyValuePair> extKeyValuePairAdapter;
    private Consumer<ExtKeyValuePair> onSelectedConsumer;
    private Consumer<String> keywordConsumer;


    private ExtTextView tvCancel;
    private ExtEditText edtSearch;
    private ExtListView lvKeyValuePair;
    private ExtEditText edtSearchAPI;

    private int index = 0;
    private int positionSelected = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.key_value_pair_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            // cancel
            this.tvCancel = view.findViewById(R.id.tvCancel);
            this.tvCancel.setOnClickListener(v -> onClickCancel());

            // filter
            this.edtSearch = view.findViewById(R.id.edtSearch);

            //search online
            this.edtSearchAPI = view.findViewById(R.id.edtSearchAPI);

            // data
            this.lvKeyValuePair = view.findViewById(R.id.lvKeyValuePair);

            // active key is selected
            if (this.extKeyValuePairsOrigins != null && this.extKeyValuePairsOrigins.size() > 0) {
                StreamSupport.stream(this.extKeyValuePairsOrigins).forEach(n -> {
                    if (!StringUtils.isEmpty(this.value) && this.value.equalsIgnoreCase(n.getKey())) {
                        n.setSelected(true);
                        this.positionSelected = this.index;
                    } else {
                        n.setSelected(false);
                    }
                    this.index++;
                });
            }

            //search online
            this.edtSearchAPI.setVisibility(this.isVisibleSearchAPI ? View.VISIBLE : View.GONE);
            this.edtSearchAPI.setOnEditorActionListener((textView, actionId, event) -> {
                if ((actionId == EditorInfo.IME_ACTION_SEARCH) ||
                        ((!event.isShiftPressed()) &&
                                (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) &&
                                (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    if (keywordConsumer != null) {
                        keywordConsumer.accept(textView.getText().toString().trim());
                    }

                    return true;
                }

                return false;
            });

            // filter
            this.edtSearch.setVisibility(this.isVisibleFilter ? View.VISIBLE : View.GONE);
            this.edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    filterData();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            // don't show keyboard
            this.getDialog().setOnShowListener(dialog -> KeyboardUtils.hideKeyboard(getActivity(), this.edtSearch));

            // load data
            this.loadData();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    public void onClickCancel() {
        try {
//            if (this.onSelectedConsumer != null) {
//                this.onSelectedConsumer.accept(new ExtKeyValuePair("", ""));
//            }

            dismiss();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private void filterData() {
        try {
            this.extKeyValuePairs = new ArrayList<>(this.extKeyValuePairsOrigins);

            // filter
            String query = this.edtSearch.getText().toString().toLowerCase();
            if (!StringUtils.isEmpty(query)) {
                this.extKeyValuePairs = StreamSupport.stream(this.extKeyValuePairs)
                        .filter(n -> n.getKey().toLowerCase().contains(query) || n.getValue().toLowerCase().contains(query))
                        .collect(Collectors.toList());
            }

            // notification data
            this.extKeyValuePairAdapter.notifyDataSetChanged(this.extKeyValuePairs);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private void loadData() {
        try {
            this.extKeyValuePairs = new ArrayList<>(this.extKeyValuePairsOrigins);
            this.extKeyValuePairAdapter = new ExtKeyValuePairAdapter(this.getContext(), this.extKeyValuePairs);
            this.lvKeyValuePair.setAdapter(this.extKeyValuePairAdapter);
            this.lvKeyValuePair.setOnItemClickListener((parent, view, position, id) -> {
                try {
                    if (this.onSelectedConsumer != null) {
                        this.onSelectedConsumer.accept(this.extKeyValuePairAdapter.getItem(position));
                    }

                    dismiss();
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });

            this.lvKeyValuePair.setSelection(this.positionSelected);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    public ExtKeyValuePairDialogFragment onSearch(Consumer<String> keyword) {
        this.keywordConsumer = keyword;
        return this;
    }

    public ExtKeyValuePairDialogFragment setValue(String value) {
        this.value = value;
        return this;
    }

    public ExtKeyValuePairDialogFragment setDataList(List<ExtKeyValuePair> extKeyValuePairs) {
        try {
            this.extKeyValuePairs = extKeyValuePairs;
            if (extKeyValuePairAdapter != null) {
                extKeyValuePairAdapter.notifyDataSetChanged(extKeyValuePairs);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
        return this;
    }

    public ExtKeyValuePairDialogFragment setExtKeyValuePairs(List<ExtKeyValuePair> extKeyValuePairs) {
        this.extKeyValuePairsOrigins = extKeyValuePairs;
        return this;
    }

    public ExtKeyValuePairDialogFragment setVisibleFilter(boolean isVisibleFilter) {
        this.isVisibleFilter = isVisibleFilter;
        return this;
    }

    public ExtKeyValuePairDialogFragment setVisibleSearchAPI(boolean isVisibleSearchAPI) {
        this.isVisibleSearchAPI = isVisibleSearchAPI;
        return this;
    }

    public ExtKeyValuePairDialogFragment setOnSelectedConsumer(Consumer<ExtKeyValuePair> onSelectedConsumer) {
        this.onSelectedConsumer = onSelectedConsumer;
        return this;
    }
}

