package com.cmeplaza.basemodule.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmeplaza.basemodule.R;
import com.cmeplaza.basemodule.utils.RegularUtils;

/**
 * Created by klx on 2017/8/12.
 * 发送验证码的View
 * 发送中吊钟  onSending()
 * 发送成功调用onSendSuccess()
 * 发送失败调用onSendFail()
 */

public class SendVerifyCodeView extends AppCompatTextView {
    private MyCountDownTimer myCountDownTimer;

    private String beforeSendText = "获取验证码";  // 发送前的文本提示
    private String sendingText = "发送中...";  // 发送中的文本提示
    private String afterSendText = " 后重新发送";  // 发送成功之后的文本提示
    private int waitTime = 60;  // 发送成功之后等待的时间

    private EditText editText;  // 输入手机号的EditText

    public SendVerifyCodeView(Context context) {
        this(context, null);
    }

    public SendVerifyCodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SendVerifyCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SendVerifyCodeView);
        String text1 = a.getString(R.styleable.SendVerifyCodeView_before_send_text);
        String text2 = a.getString(R.styleable.SendVerifyCodeView_send_ing);
        String text3 = a.getString(R.styleable.SendVerifyCodeView_send_after);
        int time = a.getInteger(R.styleable.SendVerifyCodeView_wait_time, 60);
        int bg = a.getResourceId(R.styleable.SendVerifyCodeView_background, -1);
        if (!TextUtils.isEmpty(text1)) {
            beforeSendText = text1;
        }
        if (!TextUtils.isEmpty(text2)) {
            sendingText = text2;
        }
        if (!TextUtils.isEmpty(text3)) {
            afterSendText = text3;
        }
        if (time > 0) {
            waitTime = time;
        }
        if (bg > 0) {
            setBackgroundResource(bg);
        }
        a.recycle();
        this.setText(beforeSendText);
    }

    /**
     * 发送验证码之前检查手机号格式
     */
    public boolean check() {
        if (editText == null) {
            Toast.makeText(getContext(), "请先绑定控件", Toast.LENGTH_SHORT).show();
            return false;
        }
        String phone = editText.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), "请填写手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!RegularUtils.isMobileSimple(phone)) {
            Toast.makeText(getContext(), "手机号格式错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 绑定需要输入手机号的EditText
     */
    public void bindEditText(EditText editText) {
        this.editText = editText;
    }

    /**
     * 发送成功
     */
    public void onSendSuccess() {
        this.setEnabled(false);
        if (myCountDownTimer == null) {
            myCountDownTimer = new MyCountDownTimer(waitTime * 1000, 1000, this);
        } else {
            myCountDownTimer.cancel();
        }
        myCountDownTimer.start();
    }

    /**
     * 发送失败
     */
    public void onSendFail() {
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
        this.setEnabled(true);
        this.setText(beforeSendText);
    }

    /**
     * 发送中
     */
    public void onSending() {
        this.setEnabled(false);
        this.setText(sendingText);
    }

    private class MyCountDownTimer extends CountDownTimer {

        private MyCountDownTimer(long millisInFuture, long countDownInterval, TextView target) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setText(String.valueOf(millisUntilFinished / 1000) + "s" + afterSendText);
        }

        @Override
        public void onFinish() {
            onSendFail();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
    }
}
