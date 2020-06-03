package com.mc.books.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;

import com.bon.customview.button.ExtButton;
import com.bon.customview.textview.ExtTextView;
import com.bon.sharepreferences.AppPreferences;
import com.bon.util.ActivityUtils;
import com.bon.util.StringUtils;
import com.mc.books.R;
import com.mc.common.activities.BaseAppCompatActivity;
import com.mc.utilities.Constant;
import com.mc.utilities.KeycloakHelper;
import com.mc.utilities.McBookStore;

import org.jboss.aerogear.android.core.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends BaseAppCompatActivity {

    @BindView(R.id.imglogo)
    ImageView imglogo;
    @BindView(R.id.txtLogin)
    ExtTextView txtLogin;

    ExtButton btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> onViewClicked());
        if (StringUtils.isNullOrEmpty(McBookStore.getInstance(getAppContext()).getString(Constant.KEY_TOKEN))) {
            btnLogin.setEnabled(false);
            KeycloakHelper.connect(this, new Callback() {
                        @Override
                        public void onSuccess(Object o) {
                            btnLogin.setEnabled(false);
                            if (!AppPreferences.getInstance(getAppContext()).getBoolean(Constant.KEY_OPEN_INSTRUCTION)) {
                                goIntruction();
                            } else {
                                goMain();
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                        }
                    }
            );
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.sign_in_activity;
    }

    @Override
    public ActionBar getAppSupportActionBar() {
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!StringUtils.isNullOrEmpty(McBookStore.getInstance(getAppContext()).getString(Constant.KEY_TOKEN))) {
            startActivity(new Intent(getAppContext(), MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnLogin.setEnabled(true);
    }


    public void onViewClicked() {
        KeycloakHelper.connect(this, new Callback() {
                    @Override
                    public void onSuccess(Object o) {
                        btnLogin.setEnabled(false);
                        if (!AppPreferences.getInstance(getAppContext()).getBoolean(Constant.KEY_OPEN_INSTRUCTION)) {
                            goIntruction();
                        } else {
                            goMain();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                    }
                }
        );
    }

    void goMain() {
        AppPreferences.getInstance(getAppContext()).putString(Constant.KEY_TOKEN, "login");
        startActivity(new Intent(getAppContext(), MainActivity.class));
        finish();
    }

    void goIntruction() {
        AppPreferences.getInstance(getAppContext()).putBoolean(Constant.KEY_OPEN_INSTRUCTION, true);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            ActivityUtils.startActivity(InStructionActivity.class);
            finish();
        }, 500);
    }

}
