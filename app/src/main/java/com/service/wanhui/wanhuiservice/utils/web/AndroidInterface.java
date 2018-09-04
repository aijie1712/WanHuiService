package com.service.wanhui.wanhuiservice.utils.web;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.cmeplaza.basemodule.utils.GsonUtils;
import com.cmeplaza.basemodule.utils.LogUtils;
import com.just.agentweb.AgentWeb;
import com.service.wanhui.wanhuiservice.bean.ShareBean;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by klx on 2018/9/4.
 */

public class AndroidInterface {
    private Activity activity;
    private AgentWeb mAgentWeb;

    public AndroidInterface(Activity activity, AgentWeb mAgentWeb) {
        this.activity = activity;
        this.mAgentWeb = mAgentWeb;
    }

    @JavascriptInterface
    public void share(String object) {
        LogUtils.i("js调用了我的方法String: " + object);
        ShareBean shareBean = GsonUtils.parseJsonWithGson(object, ShareBean.class);
        if (shareBean != null) {
            UMWeb web = new UMWeb(shareBean.getHref());
            web.setTitle(shareBean.getTitle());//标题
            web.setDescription(shareBean.getDesc());//描述
            web.setThumb(new UMImage(activity, shareBean.getThumb()));
            new ShareAction(activity)
                    .withMedia(web)
                    .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                    .setCallback(new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {

                        }

                        @Override
                        public void onResult(SHARE_MEDIA share_media) {

                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media) {

                        }
                    })
                    .open();
        }
    }
}
