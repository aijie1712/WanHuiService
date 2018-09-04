package com.cmeplaza.basemodule.image;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.cmeplaza.basemodule.image.listener.ImageLoaderListener;

/**
 * Created by klx on 2017/8/12.
 */

public interface IImageLoaderstrategy {
    void showImage(@NonNull ImageLoaderOptions options);
    void showImage(@NonNull ImageLoaderOptions options, ImageLoaderListener imageLoaderListener);
    void hideImage(@NonNull View view, int visiable);
    void cleanMemory(Context context);
    void pause(Context context);
    void resume(Context context);
    // 在application的oncreate中初始化
    void init(Context context);
}
