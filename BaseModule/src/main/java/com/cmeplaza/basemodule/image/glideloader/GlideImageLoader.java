package com.cmeplaza.basemodule.image.glideloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cmeplaza.basemodule.R;
import com.cmeplaza.basemodule.image.BitmapUtils;
import com.cmeplaza.basemodule.image.IImageLoaderstrategy;
import com.cmeplaza.basemodule.image.ImageLoaderOptions;
import com.cmeplaza.basemodule.image.listener.ImageLoaderListener;

/**
 * Created by klx on 2017/8/12.
 * glide 模式
 */

public class GlideImageLoader implements IImageLoaderstrategy {
    private static final String TAG = "GlideImageLoader";
    private Handler mainHandler = new Handler();

    @Override
    public void showImage(@NonNull ImageLoaderOptions options) {
        showImage(options, null);
    }

    @Override
    public void showImage(@NonNull ImageLoaderOptions options, ImageLoaderListener imageLoaderListener) {
        RequestBuilder mGenericRequestBuilder = init(options);
        if (mGenericRequestBuilder != null) {
            showImageLast(mGenericRequestBuilder, options, imageLoaderListener);
        }
    }

    @Override
    public void hideImage(@NonNull View view, int isVisible) {
        view.setVisibility(isVisible);
    }

    @Override
    public void cleanMemory(Context context) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Glide.get(context).clearMemory();
        }
    }

    @Override
    public void pause(Context context) {
        Glide.with(context).pauseRequests();
    }

    @Override
    public void resume(Context context) {
        Glide.with(context).resumeRequests();
    }

    @Override
    public void init(Context context) {
        ViewTarget.setTagId(R.id.tag_glide);
    }

    public RequestBuilder init(ImageLoaderOptions options) {
        View v = options.getViewContainer();

        RequestOptions requestOptions = new RequestOptions()
                .error(options.getErrorDrawable())
                .placeholder(options.getHolderDrawable());
        if (options.getImageSize() != null) {
            ImageLoaderOptions.ImageSize imageSize = options.getImageSize();
            requestOptions = requestOptions.override(imageSize.getWidth(), imageSize.getHeight());
        }
        requestOptions = requestOptions.skipMemoryCache(options.isSkipMemoryCache());
        if (options.getDiskCacheStrategy() != ImageLoaderOptions.DiskCacheStrategy.DEFAULT) {
            switch (options.getDiskCacheStrategy()) {
                case NONE:
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                    break;
                case All:
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                    break;
                case SOURCE:
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                    break;
                default:
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                    break;
            }
        }

        RequestManager manager = getRequestManager(v.getContext());
        if (v instanceof ImageView) {
            RequestBuilder mDrawableTypeRequest = getGenericRequestBuilder(manager, options);
            //装载参数
            mDrawableTypeRequest = mDrawableTypeRequest.apply(requestOptions);
            return mDrawableTypeRequest;
        }
        return null;
    }

    private void showImageLast(RequestBuilder mDrawableTypeRequest, final ImageLoaderOptions options, final ImageLoaderListener imageLoaderListener) {
        final ImageView img = (ImageView) options.getViewContainer();
        mDrawableTypeRequest = mDrawableTypeRequest.thumbnail(0.1f);
        // 是否使用高斯模糊
        if (options.isBlurImage()) {
            // 具体的高斯模糊这里就不实现了，直接展示图片
            mDrawableTypeRequest.into(new SimpleTarget<Bitmap>() {

                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    Log.e("iamgeloader", "resource load failed");
                }

                @Override
                public void onStop() {
                    super.onStop();
                }

                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    if (img != null) {
                        try {
                            final Bitmap result = BitmapUtils.bitmapSetSize(resource, options.getImageSize().getWidth(), options.getImageSize().getHeight());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    img.setImageBitmap(result);
                                }
                            });
                        } catch (OutOfMemoryError e) {
                            img.setImageBitmap(resource);
                        }
                    }
                }
            });
            return;
        }
        if (options.getTarget() != null) {
            mDrawableTypeRequest.into(options.getTarget());
            return;
        }
        mDrawableTypeRequest.into(new DrawableImageViewTarget(img) {

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                super.onResourceReady(resource, transition);
                if (imageLoaderListener != null) {
                    imageLoaderListener.onLoadFinish();
                }
            }

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                if (imageLoaderListener != null) {
                    imageLoaderListener.onLoadStart();
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                if (imageLoaderListener != null) {
                    imageLoaderListener.onLoadFinish();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onStop() {
                super.onStop();
            }
        });
    }

    public RequestManager getRequestManager(Context context) {
        return Glide.with(context);

    }

    public RequestBuilder<Drawable> getGenericRequestBuilder(RequestManager manager, ImageLoaderOptions options) {

        if (!TextUtils.isEmpty(options.getUrl())) {
            return manager.load(options.getUrl());
        }
        return manager.load(options.getResource());
    }
}
