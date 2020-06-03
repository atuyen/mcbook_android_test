package com.mc.interactors;

import android.os.Handler;

import java.util.List;

import java8.util.function.Consumer;

/**
 * Created by dangpp on 2/9/2018.
 */

public interface IDataModule {
    Handler getHandler();

    void getUsers(Consumer<List<String>> consumer);
}
