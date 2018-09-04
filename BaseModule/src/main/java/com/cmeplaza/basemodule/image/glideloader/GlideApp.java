package com.cmeplaza.basemodule.image.glideloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.cmeplaza.basemodule.R;

import java.util.concurrent.ExecutionException;

/**
 * Created by klx on 2018/7/27.
 * glide加载工具类
 */

/**
 * Created by wjj on 2018/7/10 10:49
 * E-Mail ：
 * 描述：Glide工具类（glide 4.x）
 * 功能包括加载图片，圆形图片，圆角图片，指定圆角图片，模糊图片，灰度图片等等。
 * 目前我只加了这几个常用功能，其他请参考glide-transformations这个开源库。
 * https://github.com/wasabeef/glide-transformations
 */
public class GlideApp {


    public static final int placeholderSoWhite = R.color.white;
    public static final int errorSoWhite = R.color.white;

    /*
     *加载图片(默认)
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(placeholderSoWhite) //占位图
                .error(errorSoWhite)       //错误图
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /*
     *加载图片(默认)
     */
    public static void loadImageWithNoCache(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(placeholderSoWhite) //占位图
                .error(errorSoWhite)       //错误图
                .priority(Priority.HIGH)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void loadLocalImage(Context context, String path, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(android.R.color.black) //占位图
                .priority(Priority.IMMEDIATE)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context)
                .load(path)
                .thumbnail(0.1f)
                .apply(options)
                .into(imageView);
    }

    public static void loadGif(Context context, String url, ImageView imageView) {
        loadGif(context, url, imageView, placeholderSoWhite);
    }

    public static void loadGif(Context context, String url, ImageView imageView, int placeholder) {
        loadGif(context, url, imageView, placeholder, true);
    }

    /**
     * Glide.with(this).asGif()    //强制指定加载动态图片
     * 如果加载的图片不是gif，则asGif()会报错， 当然，asGif()不写也是可以正常加载的。
     * 加入了一个asBitmap()方法，这个方法的意思就是说这里只允许加载静态图片，不需要Glide去帮我们自动进行图片格式的判断了。
     * 如果你传入的还是一张GIF图的话，Glide会展示这张GIF图的第一帧，而不会去播放它。
     *
     * @param context
     * @param url       例如：https://image.niwoxuexi.com/blog/content/5c0d4b1972-loading.gif
     * @param imageView
     */
    public static void loadGif(Context context, String url, ImageView imageView,
                               int placeholder, boolean isAsGif) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .error(placeholder);

        if (!isAsGif) {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .apply(options)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(url)
                    .apply(options)
                    .into(imageView);
        }

    }

    public static Bitmap getBitmap(final Context context, final String url) {
        FutureTarget<Bitmap> futureTarget = Glide.with(context)
                .asBitmap()
                .load(url)
                .submit();
        try {
            return futureTarget.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
