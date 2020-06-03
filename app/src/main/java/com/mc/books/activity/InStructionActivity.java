package com.mc.books.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.bon.sharepreferences.AppPreferences;
import com.bon.util.ActivityUtils;
import com.mc.adapter.InStructionPagerAdapter;
import com.mc.books.R;
import com.mc.common.activities.BaseAppCompatActivity;
import com.mc.customizes.circlePageIndicator.ViewPagerIndicator;
import com.mc.utilities.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class InStructionActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final String TAG = InStructionActivity.class.getCanonicalName();
    @BindView(R.id.btnIgnore)
    Button btnIgnore;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.pager_indicator)
    ViewPagerIndicator indicator;
    int[] images = new int[]{R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R.drawable.image_5};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        InStructionPagerAdapter adapter = new InStructionPagerAdapter(this, getListImage());
        pager.setAdapter(adapter);
        indicator.setPager(pager);
        pager.setCurrentItem(0);
        btnIgnore.setOnClickListener(this);
    }

    private List<Integer> getListImage() {
        List<Integer> list = new ArrayList<>();
        for (Integer i : images) {
            list.add(i);
        }
        return list;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.instruction_activity;
    }

    @Override
    public ActionBar getAppSupportActionBar() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnIgnore:
                if (AppPreferences.getInstance(getAppContext()).getString(Constant.KEY_MORE).equals(Constant.KEY_MORE)) {
                    finish();
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        ActivityUtils.startActivity(MainActivity.class);
                        finish();
                    }, 2 * 100);
                }

                break;
            default:
                break;
        }
    }
}
