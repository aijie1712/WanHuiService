package com.service.wanhui.wanhuiservice.utils.web;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cmeplaza.basemodule.base.MyBaseRxFragment;
import com.cmeplaza.basemodule.base.mvp.BaseContract;
import com.cmeplaza.basemodule.utils.LogUtils;
import com.just.agentweb.AgentWeb;
import com.service.wanhui.wanhuiservice.R;

/**
 * Created by klx on 2018/1/12.
 * webFragment
 */

public class SimpleWebFragment extends MyBaseRxFragment implements View.OnClickListener {
    public static final String TAG = "SimpleWebFragment";
    public static final String FROM_LOAD_URL = "from_load_url";
    public static final String TITLE_NAME = "title_name";
    public static final String CAN_BACK = "can_back";
    public static final String IS_FULL = "is_full";//是否全屏自适应
    private static final int FILTER_AD = 0x001;
    LinearLayout ll_rootView;
    RelativeLayout rl_no_net;
    private AgentWeb mAgentWeb;

    private String fromUrl = "";
    private String fromTitle;
    private boolean canBack = true;
    private boolean isReload = false;
    private String isRead = "";//判断消息是否一直有未读
    private boolean isFull = true;//是否全屏自适应

    private boolean isHome = false;

    public static SimpleWebFragment newInstance(String url) {
        return newInstance(url, "");
    }

    public static SimpleWebFragment newInstance(String url, String name) {
        return newInstance(url, name, true);
    }

    public static SimpleWebFragment newInstance(String url, String name, boolean canBack) {
        SimpleWebFragment simpleWebFragment = new SimpleWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FROM_LOAD_URL, url);
        bundle.putString(TITLE_NAME, name);
        bundle.putBoolean(CAN_BACK, canBack);
        simpleWebFragment.setArguments(bundle);
        return simpleWebFragment;
    }

    public static SimpleWebFragment newInstance(String url, String name, boolean canBack, boolean isFull) {
        SimpleWebFragment simpleWebFragment = new SimpleWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FROM_LOAD_URL, url);
        bundle.putString(TITLE_NAME, name);
        bundle.putBoolean(CAN_BACK, canBack);
        bundle.putBoolean(IS_FULL, isFull);
        simpleWebFragment.setArguments(bundle);
        return simpleWebFragment;
    }

    public WebView getWebView() {
        if (mAgentWeb == null) {
            return null;
        }
        return mAgentWeb.getWebCreator().getWebView();
    }

    @Override
    protected BaseContract.BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_simple_web;
    }

    @Override
    protected void initView() {
        ll_rootView = (LinearLayout) rootView.findViewById(R.id.ll_rootView);
        rl_no_net = (RelativeLayout) rootView.findViewById(R.id.rl_no_net);
        rootView.findViewById(R.id.iv_title_right).setOnClickListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            fromUrl = bundle.getString(FROM_LOAD_URL);
            fromTitle = bundle.getString(TITLE_NAME);
            canBack = bundle.getBoolean(CAN_BACK, true);
            isFull = bundle.getBoolean(CAN_BACK, true);
            if (!TextUtils.isEmpty(fromTitle)) {
                RelativeLayout commonTitle = (RelativeLayout) rootView.findViewById(R.id.commonTitle);
                visible(commonTitle);
                setTitleCenter(fromTitle);
            }
        }
        rl_no_net.setOnClickListener(new View.OnClickListener() {//重新加载
            @Override
            public void onClick(View v) {
                mAgentWeb.getWebCreator().getWebView().reload();
                rl_no_net.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void initData() {
        try {
            LogUtils.i(TAG, "要打开的连接：  " + fromUrl);
            mAgentWeb = AgentWeb.with(getActivity())//传入Activity or Fragment
                    .setAgentWebParent(ll_rootView, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                    .useDefaultIndicator()// 使用默认进度条
                    .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                    .createAgentWeb()
                    .ready()  //此方法重置了WebViewClient ChromeClient 如果自定义 需写在初始化之后
                    .go(fromUrl);

            mAgentWeb.getWebCreator().getWebView().setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            //为防止webview重置 需在mAgentWeb创建之后写
            mAgentWeb.getWebCreator().getWebView().setWebViewClient(new MyWebViewClient());
            mAgentWeb.getWebCreator().getWebView().setWebChromeClient(new WebChromeClient() {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    setTitleCenter(title);
                }
            });
            mAgentWeb.getAgentWebSettings().getWebSettings().setSupportZoom(true);
            //默认加载缓存
            mAgentWeb.getAgentWebSettings().getWebSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mAgentWeb.getAgentWebSettings().getWebSettings().setBuiltInZoomControls(true);
            mAgentWeb.getAgentWebSettings().getWebSettings().setAppCacheEnabled(true);
            mAgentWeb.getAgentWebSettings().getWebSettings().setDatabaseEnabled(true);
            mAgentWeb.getAgentWebSettings().getWebSettings().setDisplayZoomControls(false);

            if (isFull) { //设置加载进来的页面自适应手机屏幕
                mAgentWeb.getAgentWebSettings().getWebSettings().setUseWideViewPort(true);
                mAgentWeb.getAgentWebSettings().getWebSettings().setLoadWithOverviewMode(true);
            }

            //web js调用Android本地方法
            mAgentWeb.getJsInterfaceHolder().addJavaObject("webView", new AndroidInterface(getActivity(), mAgentWeb));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

    @Override
    public void onFirstUserVisible() {

    }

    @Override
    public void onDestroy() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }

    public boolean onBackPressed() {
        if (isHome) {
            return false;
        }
        return mAgentWeb != null && mAgentWeb.back();
    }

    /**
     * java 传值给js判断商圈聊天是否未读已读
     *
     * @param result 1没有未读商圈数据  0有未读商圈数据
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void isRead(final String result) {
        if (isRead.equals(result)) {
            isRead = result;
        } else {
            if (mAgentWeb != null) {
                mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:isRead('" + result + "')");
            }
        }
    }

    public void changeUrl(String url) {
        if (TextUtils.equals(url, fromUrl)) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }
        mAgentWeb.getUrlLoader().loadUrl(url);
    }

    private void onReceiveError() {
        rl_no_net.setVisibility(View.VISIBLE);
        isReload = true;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.d(TAG, "shouldOverrideUrlLoading() url:" + url);
            if (url == null || url.length() == 0) {
                return false;
            }
            if (!TextUtils.isEmpty(url) && url.contains("wifi.shouji.360.cn")) {
                return true;
            }
            if (!TextUtils.isEmpty(url) && !url.startsWith("http://") && !url.startsWith("https://")) {
                return true;
            }

            if (url.endsWith(".apk")) {
                // 在默认浏览器中下载apk
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    startActivity(intent);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (url.startsWith("http://www.zgwhjf.com/index/index/index.html")) {
                isHome = true;
            }else{
                isHome = false;
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (isReload) {
                mAgentWeb.getWebCreator().getWebView().setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (isReload) {
                mAgentWeb.getWebCreator().getWebView().setVisibility(View.VISIBLE);
            }
            isReload = false;
            if (TextUtils.isEmpty(fromTitle)) {
                setTitleCenter(view.getTitle());
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            LogUtils.d(TAG, "onReceivedError() errorCode:" + errorCode + "----failingUrl" + failingUrl);
            onReceiveError();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @TargetApi(21)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            LogUtils.d(TAG, "onReceivedHttpError() errorCode:" + errorResponse.getStatusCode() + " reason: " + errorResponse.getReasonPhrase());
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }
    }    public void doShare() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:initShare()");
        }
    }



    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.iv_title_right:
                doShare();
                break;
        }
    }
}
