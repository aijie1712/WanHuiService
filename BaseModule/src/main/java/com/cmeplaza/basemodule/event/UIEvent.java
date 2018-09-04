package com.cmeplaza.basemodule.event;

import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

/**
 * Created by klx on 2018/7/23.
 * 配合EventBus使用的页面间通信的Event
 */

public class UIEvent {

    public String event;
    private String message;

    private Bundle bundle = new Bundle();

    public UIEvent(String event) {
        this.event = event;
    }

    public String getMessage() {
        return message;
    }

    public UIEvent setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void post() {
        EventBus.getDefault().post(this);
    }

    public void postSticky() {
        EventBus.getDefault().postSticky(this);
    }

    public UIEvent putExtra(String key, Serializable value) {
        bundle.putSerializable(key, value);
        return this;
    }

    public UIEvent setBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public Bundle getBundle() {
        return bundle;
    }

    @Override
    public String toString() {
        return "UIEvent{" +
                "event='" + event + '\'' +
                ", message='" + message + '\'' +
                ", bundle=" + bundle +
                '}';
    }
}
