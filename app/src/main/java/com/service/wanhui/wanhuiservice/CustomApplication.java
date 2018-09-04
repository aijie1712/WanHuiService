package com.service.wanhui.wanhuiservice;

import android.support.multidex.MultiDexApplication;

import com.cmeplaza.basemodule.CoreLib;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

/**
 * Created by klx on 2018/9/4.
 */

public class CustomApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CoreLib.init(this);
        initShareSdk();
    }

    private void initShareSdk(){
        UMConfigure.init(this, getString(R.string.umeng_app_id)
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(true);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        PlatformConfig.setWeixin(getString(R.string.weixin_app_id), getString(R.string.weixin_app_secret));
    }
}
