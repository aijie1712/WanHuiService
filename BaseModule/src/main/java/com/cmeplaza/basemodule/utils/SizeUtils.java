package com.cmeplaza.basemodule.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.text.DecimalFormat;

/**
 * 作者：Android_AJ on 2017/4/7.
 * 邮箱：ai15116811712@163.com
 * 版本：v1.0
 * 尺寸相关工具类
 */
public class SizeUtils {
    /* 屏幕dp转px */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    public static int px2sp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取屏幕的宽度px
     *
     * @param context 上下文
     * @return 屏幕宽px
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕的高度px
     *
     * @param context 上下文
     * @return 屏幕高px
     */
    public static int getScreenHeight(Context context) {
//        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
//        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
//        return outMetrics.heightPixels;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public static String getMbSize(long kb) {
        if (kb < 1024) {
            return kb + "kb";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String result = df.format(kb / 1024);
        return result + "MB";
    }

    public static int getViewX(View v) {
        int[] position = new int[2];
        v.getLocationOnScreen(position);
        return position[0];
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public static RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }

    public static Rect getLocationInView(View parent, View child) {
        if (child == null || parent == null) {
            throw new IllegalArgumentException("parent and child can not be null .");
        }

        View decorView = null;
        Context context = child.getContext();
        if (context instanceof Activity) {
            decorView = ((Activity) context).getWindow().getDecorView();
        }

        Rect result = new Rect();
        Rect tmpRect = new Rect();

        View tmp = child;

        if (child == parent) {
            child.getHitRect(result);
            return result;
        }
        while (tmp != decorView && tmp != parent) {
            tmp.getHitRect(tmpRect);
            {
                result.left += tmpRect.left;
                result.top += tmpRect.top;
                tmp = (View) tmp.getParent();

                //added by isanwenyu@163.com fix bug #21 the wrong rect user will received in ViewPager
                if (tmp != null && tmp.getParent() != null && (tmp.getParent() instanceof ViewPager)) {
                    tmp = (View) tmp.getParent();
                }
            }
        }
        result.right = result.left + child.getMeasuredWidth();
        result.bottom = result.top + child.getMeasuredHeight();
        return result;
    }

    public static int getViewHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredHeight();
    }

    public static int getViewWidth(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredWidth();
    }
}