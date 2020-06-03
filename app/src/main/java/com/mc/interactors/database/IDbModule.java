package com.mc.interactors.database;

import java.util.List;

/**
 * Created by dangpp on 2/9/2018.
 */

public interface IDbModule {
    List<String> getUsers();

    void insertUsers(List<String> users);
}
