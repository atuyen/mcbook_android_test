package com.mc.books.fragments.more.changepass;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.button.ExtButton;
import com.bon.customview.edittext.ExtEditText;
import com.bon.util.ToastUtils;
import com.mc.books.R;
import com.mc.books.dialog.ErrorBoxDialog;
import com.mc.common.fragments.BaseMvpFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePassFragment extends BaseMvpFragment<IChangePassView, IChangePassPresenter<IChangePassView>> implements IChangePassView {
    public static ChangePassFragment newInstance() {
        Bundle args = new Bundle();
        ChangePassFragment fragment = new ChangePassFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.edtPass)
    ExtEditText edtPass;
    @BindView(R.id.edtConfirmPass)
    ExtEditText edtConfirmPass;
    @BindView(R.id.btnSave)
    ExtButton btnSave;

    @Override
    public IChangePassPresenter<IChangePassView> createPresenter() {
        return new ChangePassPresenter<>(getAppComponent());
    }

    @Override
    public int getResourceId() {
        return R.layout.change_password;
    }

    @Override
    public int getTitleId() {
        return R.string.change_password;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @OnClick(R.id.btnSave)
    void onChangePass() {
        if (edtPass.getText().length() < 6 || edtConfirmPass.getText().length() < 6) {
            ErrorBoxDialog errorBoxDialog = new ErrorBoxDialog(getContext(), getString(R.string.pass_word_require));
            errorBoxDialog.show();
            return;
        }

        if (!edtPass.getText().toString().equals(edtConfirmPass.getText().toString())) {
            ErrorBoxDialog errorBoxDialog = new ErrorBoxDialog(getContext(), getString(R.string.confirm_pass_wrong));
            errorBoxDialog.show();
            return;
        }

        presenter.changePass(edtPass.getText().toString());
    }

    @Override
    public void onShowLoading(boolean isShow) {
        showProgress(isShow);
    }

    @Override
    public void onChangePassSuccess() {
        ToastUtils.showToast(getAppContext(), getString(R.string.change_pass_success));
    }

    @Override
    public void onError(int code) {
        if (code == -1) return;
        ErrorBoxDialog errorBoxDialog = new ErrorBoxDialog(getContext(), getString(code));
        errorBoxDialog.show();
    }
}
