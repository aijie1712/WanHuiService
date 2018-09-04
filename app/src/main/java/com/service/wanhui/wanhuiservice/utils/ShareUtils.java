package com.service.wanhui.wanhuiservice.utils;

import android.app.Activity;

import com.service.wanhui.wanhuiservice.utils.listener.ShareResultListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by klx on 2018/9/1.
 */

public class ShareUtils {
    /**
     * 分享链接
     *
     * @param activity
     * @param url      分享链接
     * @param title    标题
     * @param desc     描述
     */
    public static void shareLink(Activity activity, String url, String title, String desc, final ShareResultListener shareResultListener) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setDescription(desc);//描述
        new ShareAction(activity)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        if (shareResultListener != null) {
                            shareResultListener.onShareStart();
                        }
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        if (shareResultListener != null) {
                            shareResultListener.onShareSuccess();
                        }
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        if (shareResultListener != null) {
                            shareResultListener.onShareFailed(throwable.getMessage());
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        if (shareResultListener != null) {
                            shareResultListener.onShareCancel();
                        }
                    }
                })
                .open();
    }
}
