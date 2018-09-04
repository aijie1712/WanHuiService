package com.service.wanhui.wanhuiservice;

import android.content.Intent;

import com.cmeplaza.basemodule.base.CommonBaseActivity;
import com.service.wanhui.wanhuiservice.utils.web.SimpleWebFragment;
import com.umeng.socialize.UMShareAPI;

public class MainActivity extends CommonBaseActivity {
    private SimpleWebFragment simpleWebFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        simpleWebFragment = SimpleWebFragment.newInstance(getString(R.string.web_url));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, simpleWebFragment)
                .commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (simpleWebFragment != null) {
            if (simpleWebFragment.onBackPressed()) {
                return;
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
