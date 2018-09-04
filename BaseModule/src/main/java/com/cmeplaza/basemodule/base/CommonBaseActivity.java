package com.cmeplaza.basemodule.base;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmeplaza.basemodule.CoreLib;
import com.cmeplaza.basemodule.R;
import com.cmeplaza.basemodule.base.mvp.BaseContract;
import com.cmeplaza.basemodule.event.UIEvent;
import com.cmeplaza.basemodule.http.MySubscribe;
import com.cmeplaza.basemodule.utils.CommonDialogUtils;
import com.cmeplaza.basemodule.utils.NetworkUtils;
import com.cmeplaza.basemodule.utils.PhotoUtils;
import com.cmeplaza.basemodule.utils.SizeUtils;
import com.cmeplaza.basemodule.utils.UiUtil;
import com.cmeplaza.basemodule.widget.MyLoadMoreWrapper;
import com.cmeplaza.basemodule.widget.recyclerview.DividerDecoration;
import com.cmeplaza.basemodule.widget.stateview.StateView;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by klx on 2017/9/5.
 * 通用CommonBaseActivity
 */

public abstract class CommonBaseActivity extends RxAppCompatActivity
        implements MyLoadMoreWrapper.OnLoadMoreListener, BaseContract.BaseView {
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    // 下拉刷新
    protected SwipeRefreshLayout swipe_refresh;
    protected boolean canLoadMore = false;
    // 加载更多
    protected MyLoadMoreWrapper loadMoreWrapper;
    protected StateView stateView;
    protected boolean shouldHideSoftOnTouch = true; // 触摸非EditText是否关闭软键盘
    private Unbinder unbinder;
    private AlertDialog progressDialog;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        CoreLib.activityList.add(this);
        EventBus.getDefault().register(this);
        beforeSetContentView(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        afterSetContentView(savedInstanceState);
        initBack();
        initRefreshLayout();
        initView();
        initData();
    }

    protected void beforeSetContentView(Bundle savedInstanceState) {
    }

    protected abstract int getLayoutId();

    protected void afterSetContentView(Bundle savedInstanceState) {

    }

    private void initBack() {
        TextView tv_back = ButterKnife.findById(this, R.id.tv_back);
        if (tv_back != null) {
            if (tv_back.getVisibility() == View.VISIBLE) {
                tv_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }

    protected void initRefreshLayout() {
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        if (swipe_refresh == null) {
            return;
        }
        swipe_refresh.setColorSchemeResources(R.color.pink_dark, R.color.pink_light,
                R.color.colorAccentDark);
        swipe_refresh.setProgressViewOffset(false, -100, getResources().getDisplayMetrics().heightPixels / 10);
        swipe_refresh.setRefreshing(false);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isAvailable(getApplicationContext())) {
                    onLayoutRefresh();
                } else {
                    UiUtil.showToast(R.string.net_error);
                    swipe_refresh.setRefreshing(false);
                }
            }
        });
    }

    protected abstract void initView();

    protected void initData() {
    }

    /**
     * 下拉刷新后调用的方法
     */
    protected void onLayoutRefresh() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        CoreLib.isResume = true;
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        CoreLib.isResume = false;
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        CoreLib.activityList.remove(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    protected void disableSwipe_refresh() {
        if (swipe_refresh != null) {
            swipe_refresh.setEnabled(false);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (shouldHideSoftOnTouch && v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置支持StateView
     *
     * @param view 要包裹的View
     */
    protected void supportStateView(View view) {
        stateView = StateView.inject(view);
    }

    public void onLoadMoreRequested() {
        if (!canLoadMore) {
            return;
        }
    }

    protected void initLoadMoreWrapper(RecyclerView.Adapter commonAdapter) {
        loadMoreWrapper = new MyLoadMoreWrapper(commonAdapter);
        loadMoreWrapper.setLoadMoreView(R.layout.layout_load_more_recycler);
        loadMoreWrapper.setOnLoadMoreListener(this);
    }

    /**
     * @param flag 能否加载
     */
    protected void hasMore(boolean flag) {
        if (canLoadMore == flag) {
            return;
        }
        loadMoreWrapper.setLoadOver(flag);
        canLoadMore = flag;
        loadMoreWrapper.notifyDataSetChanged();
    }

    /**
     * 默认加载框
     */
    protected void showProgress(int id) {
        String message = getString(id);
        showProgress(message);
    }

    protected void showBack() {
        TextView tv_back = ButterKnife.findById(this, R.id.tv_back);
        if (tv_back != null) {
            visible(tv_back);
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    protected void setTitleCenter(int res) {
        setTitleCenter(getString(res));
    }

    protected void setTitleCenter(String title) {
        TextView tv_title_center = ButterKnife.findById(this, R.id.tv_title_center);
        if (tv_title_center != null) {
            tv_title_center.setText(title);
        }
    }

    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void inVisible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    protected void enable(boolean enable, View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setEnabled(enable);
                }
            }
        }
    }

    protected void commonStartActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        commonStartActivity(intent);
    }

    protected void commonStartActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showError(String message) {
        UiUtil.showToast(this, message);
    }

    /**
     * 默认加载框
     */
    public void showProgress() {
        showProgress(getString(R.string.loading));
    }

    /**
     * 显示加载框
     */
    public void showProgress(String message) {
        progressDialog = CommonDialogUtils.getProgressDialog(this, message);
        progressDialog.show();
    }

    @Override
    public Dialog getDialog(String message) {
        if (TextUtils.isEmpty(message)) {
            message = "加载中...";
        } else {
            progressDialog = CommonDialogUtils.getProgressDialog(this, message);
        }
        if (progressDialog == null) {
            progressDialog = CommonDialogUtils.getProgressDialog(this, message);
        }
        return progressDialog;
    }

    /**
     * 隐藏加载框
     */
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (swipe_refresh != null) {
            swipe_refresh.setRefreshing(false);
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bind() {
        return bindToLifecycle();
    }

    protected void hideRefresh() {
        if (swipe_refresh != null) {
            swipe_refresh.setRefreshing(false);
        }
    }

    protected void showOnlyConfirmDialog(String message) {
//        CommonDialogUtils.showOnlyConfirmDialog(this, message, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    protected boolean isEmpty(String content) {
        return TextUtils.isEmpty(content);
    }

    public void initCommonRecyclerView(RecyclerView recyclerView) {
        DividerDecoration dividerDecoration = new DividerDecoration(CoreLib.getContext(),
                CoreLib.getContext().getResources().getColor(R.color.global_split_line_color),
                SizeUtils.dp2px(CoreLib.getContext(), 0.5f));
        dividerDecoration.setLinePadding(10);
        recyclerView.addItemDecoration(dividerDecoration);
        recyclerView.setHasFixedSize(true);
    }

    protected boolean isCompatible(int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUiEvent(UIEvent uiEvent) {
    }

    protected String getText(TextView textView) {
        if (textView == null) {
            return "";
        }
        return textView.getText().toString().trim();
    }

    /**
     * 显示选择图片的弹窗
     */
    protected void showChosePicDialog() {
        CommonDialogUtils.showChoosePicDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/8/7 拍照
                new RxPermissions(CommonBaseActivity.this)
                        .request(Manifest.permission.CAMERA)
                        .subscribe(new MySubscribe<Boolean>() {
                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {
                                    takePhoto();
                                } else {
                                    CommonDialogUtils.showSetPermissionDialog(CommonBaseActivity.this, getString(R.string.takePicPermissionTip));
                                }
                            }
                        });
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/8/7 从相册中选择
                new RxPermissions(CommonBaseActivity.this)
                        .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new MySubscribe<Boolean>() {
                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {
                                    PhotoUtils.openPic(CommonBaseActivity.this, CODE_GALLERY_REQUEST);
                                } else {
                                    CommonDialogUtils.showSetPermissionDialog(CommonBaseActivity.this, getString(R.string.choosePicPermissionTip));
                                }
                            }
                        });
            }
        });
    }

    protected void takePhoto() {
        imageUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(CommonBaseActivity.this, "com.cmeplaza.factorymanager.fileprovider", fileUri);
        PhotoUtils.takePicture(CommonBaseActivity.this, imageUri, CODE_CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int output_X = 480, output_Y = 480;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.cmeplaza.factorymanager.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        Toast.makeText(CommonBaseActivity.this, "设备没有SD卡!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
//                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    onGetImageResult(cropImageUri.toString());
                    break;
            }
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    protected void onGetImageResult(String imagePath) {

    }
}
