package com.bon.customview.keyvaluepair;

/**
 * Created by Dang Pham Phu on 2/11/2017.
 */

public interface ExtKeyValuePairListener<T> {
    void loadData(ExtKeyValuePairServiceDialogFragment dialog);

    void loadMoreData(ExtKeyValuePairServiceDialogFragment dialog);

    void onClickItem(T itemClick);
}
