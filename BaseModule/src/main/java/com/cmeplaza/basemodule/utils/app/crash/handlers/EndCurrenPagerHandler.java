package com.cmeplaza.basemodule.utils.app.crash.handlers;

import android.app.Activity;

import com.cmeplaza.basemodule.utils.app.crash.IHandlerException;
import com.cmeplaza.basemodule.utils.app.crash.WindowManagerGlobal;


/**
 * Created by zhangzheng on 2017/4/5.
 */

public class EndCurrenPagerHandler implements IHandlerException {
    @Override
    public boolean handler(Throwable e) {
        Activity currenActivity = WindowManagerGlobal.getInstance().getCurrenActivity();
        if (currenActivity != null) {
            currenActivity.finish();
        }
        return false;
    }
}
