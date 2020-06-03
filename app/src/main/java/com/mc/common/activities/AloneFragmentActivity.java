package com.mc.common.activities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.bon.interfaces.Optional;
import com.bon.sharepreferences.AppPreferences;
import com.mc.books.R;
import com.mc.common.Keys;

import butterknife.BindView;
import java8.util.function.Consumer;

/**
 * Created by dangpp on 2/21/2018.
 */

public class AloneFragmentActivity extends BaseAppCompatActivity {
    private static final String FRAGMENT_NAME = "fragment_name";
    private static final String TRANSLUCENT = "translucent";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // fragment
    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getIntent().getExtras().getBoolean(TRANSLUCENT)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        Optional.from(savedInstanceState)
                .doIfEmpty(b -> {
                    Bundle bundle = getIntent().getExtras();
                    getFragmentForOpen(bundle, fr -> replaceFragment(fr, false));
                });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.alone_fragment_activity;
    }

    /**
     * @param bundle
     * @return
     */
    protected Bundle getArgsForFragment(Bundle bundle) {
        if (bundle == null) return null;
        return bundle.getBundle(Keys.ARGS);
    }

    /**
     * @param bundle
     * @param fragmentForOpen
     */
    protected void getFragmentForOpen(Bundle bundle, Consumer<Fragment> fragmentForOpen) {
        String fragment;

        if (bundle.getString(FRAGMENT_NAME) == null) {
            fragment = AppPreferences.getInstance(getAppContext()).getString("FRAGMENT_INTENT");
        } else {
            fragment = bundle.getString(FRAGMENT_NAME);
        }

        fragmentForOpen.accept(Fragment.instantiate(getBaseContext(), fragment, getArgsForFragment(bundle)));
    }

    /**
     * @param fragment
     * @param addToBackStack
     */
    public void replaceFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        this.fragment = fragment;
        FragmentTransaction replace = getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, fragment);
        if (addToBackStack) {
            replace.addToBackStack(null);
        }
        replace.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //for handle in fragment first
            if (fragment != null) {
                if (fragment.onOptionsItemSelected(item)) {
                    return true;
                }
            }

            setResult(Activity.RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public ActionBar getAppSupportActionBar() {
        return getSupportActionBar();
    }

    /**
     * @param context
     * @return
     */
    public static Builder with(Context context) {
        return new Builder(context);
    }

    /**
     * @param fragment
     * @return
     */
    public static Builder with(Fragment fragment) {
        return new Builder(fragment);
    }

    public static class Builder {
        private final Context contextForOpen;

        private final Fragment fragmentForOpen;
        private boolean translucent;
        private boolean overrideAnim;
        private Integer requestCode;
        private Bundle params;

        private Builder(Context context) {
            this.contextForOpen = context;
            this.fragmentForOpen = null;
        }

        private Builder(Fragment fragment) {
            this.fragmentForOpen = fragment;
            this.contextForOpen = null;
        }

        public Builder setStatusTransluent(boolean translucent) {
            this.translucent = translucent;
            return this;
        }

        @Deprecated
        public Builder overrideStartAnimation(boolean override) {
            this.overrideAnim = override;
            return this;
        }

        public Builder forResult(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public Builder parameters(Bundle params) {
            this.params = params;
            return this;
        }

        public Intent createIntentForStart(Class<? extends Fragment> fragmentClass) {
            Intent intent = getIntent(contextForOpen, fragmentForOpen, params, AloneFragmentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(FRAGMENT_NAME, fragmentClass.getName());
            intent.putExtra(TRANSLUCENT, translucent);
            return intent;
        }

        public void start(Class<? extends Fragment> fragmentClass) {
            Intent intent = createIntentForStart(fragmentClass);
            if (contextForOpen != null) {
                if (requestCode != null) {
                    ((Activity) contextForOpen).startActivityForResult(intent, requestCode);
                } else {
                    contextForOpen.startActivity(intent);
                }
            } else {
                if (requestCode != null) {
                    fragmentForOpen.startActivityForResult(intent, requestCode);
                } else {
                    fragmentForOpen.startActivity(intent);
                }
            }

            if (overrideAnim) {
                if (contextForOpen != null) {
                    Context realContext;
                    if (contextForOpen instanceof Activity) {
                        realContext = contextForOpen;
                    } else if (contextForOpen instanceof ContextWrapper) {
                        realContext = ((ContextWrapper) contextForOpen).getBaseContext();
                    } else {
                        realContext = contextForOpen;
                    }
                    ((Activity) realContext).overridePendingTransition(R.anim.slide_up, R.anim.stay);
                } else {
                    fragmentForOpen.getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
                }
            }
        }
    }

    @NonNull
    protected static Intent getIntent(Context contextForOpen, Fragment fragmentForOpen, Bundle params, Class<? extends AloneFragmentActivity> clazz) {
        Intent intent = new Intent(contextForOpen == null ? fragmentForOpen.getContext() : contextForOpen, clazz);
        if (params != null) {
            intent.putExtra(Keys.ARGS, params);
        }
        return intent;
    }
}
