package com.cmeplaza.basemodule;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klx on 2018/7/23.
 */

public class CoreLib {
    public static List<Activity> activityList = new ArrayList<>();
    public static boolean isResume = false;
    private static Application application;
    private static String BASE_URL;

    public static void init(Application app) {
        application = app;
    }

    public static void initNet(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public static Application getContext() {
        if (application == null) {
            throw new RuntimeException("you should call init() method first");
        }
        return application;
    }

    public static String getBaseUrl() {
        if (TextUtils.isEmpty(BASE_URL)) {
            throw new RuntimeException("You should call initNet method first");
        }
        return BASE_URL;
    }

    public static String getFileBaseUrl() {
        if (TextUtils.isEmpty(BASE_URL)) {
            throw new RuntimeException("You should call initNet method first");
        }
        return BASE_URL.substring(0, BASE_URL.length() - 1);
    }


}
