package com.cmeplaza.basemodule.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.cmeplaza.basemodule.CoreLib;

/**
 * Created by klx on 2018/7/25.
 */

public class CommonUtils {
        /**
     * 获取VersionName
     *
     * @return
     */
    public static String getVersionName() {
        String result = "";
        PackageManager packageManager = CoreLib.getContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(CoreLib.getContext().getPackageName(), 0);
            result = String.valueOf(packageInfo.versionName);
            return result;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
