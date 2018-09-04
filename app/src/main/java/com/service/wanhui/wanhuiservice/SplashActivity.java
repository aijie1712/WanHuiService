package com.service.wanhui.wanhuiservice;

import com.cmeplaza.basemodule.base.CommonBaseActivity;
import com.cmeplaza.basemodule.http.MySubscribe;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by klx on 2018/9/4.
 */

public class SplashActivity extends CommonBaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        nextPage();
    }

    private void nextPage() {
        Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscribe<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        commonStartActivity(MainActivity.class);
                        finish();
                    }
                });
    }
}
