package com.cmeplaza.basemodule.http.bean;

import android.text.TextUtils;

import com.umeng.socialize.bean.StatusCode;

/**
 * Created by Allen on 2017/10/23.
 *
 * @author Allen 返回数据基类
 */

public class BaseData<T> {
    /**
     * 错误码
     */
    private String statusCode;
    private String status;
    /**
     * 错误描述
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    public boolean isSuccess(){
        return TextUtils.equals("200", statusCode) && !TextUtils.equals("error",status);
    }

    public String getStatusCode() {
        return statusCode == null ? "" : statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseData{" +
                "statusCode='" + statusCode + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
