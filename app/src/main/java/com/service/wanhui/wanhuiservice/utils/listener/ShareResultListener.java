package com.service.wanhui.wanhuiservice.utils.listener;

/**
 * Created by klx on 2018/9/1.
 */

public interface ShareResultListener {
    void onShareStart();
    void onShareSuccess();
    void onShareCancel();
    void onShareFailed(String reason);
}
