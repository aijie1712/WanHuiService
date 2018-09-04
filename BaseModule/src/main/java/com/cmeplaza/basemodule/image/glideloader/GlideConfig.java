package com.cmeplaza.basemodule.image.glideloader;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.cmeplaza.basemodule.utils.FileUtils;

import java.io.File;

/**
 * Created by klx on 2017/12/12.
 * glide缓存配置
 */
public class GlideConfig extends AppGlideModule {
    private int diskSize = 1024 * 1024 * 100;
    private int memorySize = (int) (Runtime.getRuntime().maxMemory()) / 8;  // 取1/8最大内存作为最大缓存

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 定义缓存大小和位置
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskSize));  // 内存中
//        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "cache", diskSize)); // sd卡中
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                // Careful: the external cache directory doesn't enforce permissions
                File cacheLocation = new File(FileUtils.getCacheFile(true, true));
                if (!cacheLocation.exists()) {
                    cacheLocation.mkdirs();
                }
                return DiskLruCacheWrapper.get(cacheLocation, diskSize);
            }
        });

        // 默认内存和图片池大小
        // MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        // int defaultMemoryCacheSize = calculator.getMemoryCacheSize(); // 默认内存大小
        // int defaultBitmapPoolSize = calculator.getBitmapPoolSize(); // 默认图片池大小
        // builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize));
        // builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));

        // 自定义内存和图片池大小
        builder.setMemoryCache(new LruResourceCache(memorySize)); // 自定义内存大小
        builder.setBitmapPool(new LruBitmapPool(memorySize)); // 自定义图片池大小

        // 定义图片格式
        // builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
//        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565); // 默认
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565).disallowHardwareConfig());
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }
}
