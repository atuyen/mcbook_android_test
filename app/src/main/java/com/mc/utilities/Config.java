package com.mc.utilities;

/**
 * Created by dangpp on 2/9/2018.
 */

public interface Config {
    // base url
    String BASE_URL = Constant.SERVER_LIVE ? "https://kong.mentor.vn/production/kong/" : "https://kong.mentor.vn/kong/";

    // retry policy
    boolean RETRY_POLICY = true;

    // timeout
    int REQUEST_FILE_TIMEOUT = 100;
    int REQUEST_TIMEOUT_LONG = 60;
    int REQUEST_TIMEOUT = 30;


}
