package com.bon.activity;

import android.support.v7.app.AppCompatActivity;

import com.bon.fragment.ExtBaseFragment;
import com.bon.fragment.FragmentUtils;
import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.BackUtils;
import com.bon.util.KeyboardUtils;
import com.bon.util.ProgressDialogUtils;

import java.util.Stack;

/**
 * Created by Dang Pham Phu on 2/1/2017.
 */
public abstract class ExtBaseActivity extends AppCompatActivity {
    private static final String TAG = ExtBaseActivity.class.getSimpleName();
    public static final int NUMBER_ONE = 1;

    // variable
    private ProgressDialogUtils progressDialog = null;

    // store fragment in back stack
    public Stack<ExtBaseFragment> fragments = new Stack<>();

    // set fragment default
    public abstract void initFragmentDefault();

    // onclick back
    private void onClickBackAction() {
        try {
            // hide keyboard
            KeyboardUtils.dontShowKeyboardActivity(this);

            // stacks
            if (this.fragments != null && this.fragments.size() > NUMBER_ONE) {
                // remove current fragment
                fragments.pop();

                // get previous fragment
                ExtBaseFragment fragment = this.fragments.peek();
                if (fragment == null) return;

                // back to previous fragment
                FragmentUtils.replaceFragment(this, fragment, frag -> this.fragments.add(frag));
            } else {
                // clear stack
                this.fragments.clear();

                // init default fragment
                this.initFragmentDefault();
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    /**
     * onclick back action
     */
    public void onBackPressedAction() {
        try {
            KeyboardUtils.dontShowKeyboardActivity(this);
            if (fragments != null && fragments.size() > 0) {
                onClickBackAction();
            } else {
                BackUtils.onClickExit(this, getString(R.string.double_tap_to_exit));
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        // hide keyboard
//        KeyboardUtils.dontShowKeyboardActivity(this);
        onBackPressedAction();
    }

    /**
     * show dialog
     */
    public void showProgressDialog() {
        try {
            this.progressDialog = new ProgressDialogUtils();
            this.progressDialog.setMessage(getString(R.string.loading));
            this.progressDialog.show(this);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    /**
     * dismiss dialog
     */
    public void hideProgressDialog() {
        try {
            if (this.progressDialog != null) {
                this.progressDialog.dismiss();
                this.progressDialog = null;
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }
}