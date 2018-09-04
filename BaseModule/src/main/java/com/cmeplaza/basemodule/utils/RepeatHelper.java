package com.cmeplaza.basemodule.utils;

/**
 * 双击退出工具类
 *
 */
public class RepeatHelper {
    private static final long DEFAULT_TIME_MILLIS = 1500;//双击时间间隔
    private static long lastTimeMillis = 0L;

    public static boolean isFastDoubleAction(long maxTimeMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        long diff = currentTimeMillis - lastTimeMillis;
        if (diff < maxTimeMillis) {
            lastTimeMillis = 0L;
            return true;
        } else {
            lastTimeMillis = currentTimeMillis;
            return false;
        }
    }

    public static boolean isFastDoubleAction() {
        return isFastDoubleAction(DEFAULT_TIME_MILLIS);
    }

}
