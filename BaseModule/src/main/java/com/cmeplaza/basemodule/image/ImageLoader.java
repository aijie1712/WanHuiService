package com.cmeplaza.basemodule.image;

import android.view.View;

/**
 * Created by klx on 2017/8/12.
 */

public interface ImageLoader {
    void showImage(View v, String url, ImageLoaderOptions options);
    void showImage(View v, int drawable, ImageLoaderOptions options);
}
