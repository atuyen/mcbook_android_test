package com.mc.common.presenters;

import android.os.Bundle;

/**
 * Created by dangpp on 2/21/2018.
 */

public interface IBasePresenter {
    void processArguments(Bundle arguments);

    void saveInstanceState(Bundle bundle);

    void restoreInstanceState(Bundle bundle);

    void onStart();

    void onStop();
}
