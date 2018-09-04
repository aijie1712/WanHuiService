package com.cmeplaza.basemodule.base;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;

import com.cmeplaza.basemodule.R;
import com.cmeplaza.basemodule.base.mvp.BaseContract;
import com.cmeplaza.basemodule.http.MySubscribe;
import com.cmeplaza.basemodule.utils.CommonDialogUtils;
import com.cmeplaza.basemodule.utils.LogUtils;
import com.cmeplaza.basemodule.utils.UiUtil;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by klx on 2017/9/15.
 */

public abstract class MyBaseRxFragment<T extends BaseContract.BasePresenter>
        extends BaseLazyFragment implements BaseContract.BaseView {

    private static final int REQUEST_CLIP_IMAGE = 2028;
    private static final int REQUEST_CHOOSE_PICTURE_AND_CROP = 0xac22;
    private static final int REQUEST_CHOOSE_CAMERA_AND_CROP = 0xac23;
    private static final int REQUEST_CHOOSE_CAMERA = 0xac24;
    private static String mCameraPicturePath;
    protected T mPresenter;
    protected String uploadImage;

    @Override
    public void showError(String message) {
        UiUtil.showToast(getContext(), message);
    }

    @Override
    public Dialog getDialog(String message) {
        if (TextUtils.isEmpty(message)) {
            message = "加载中...";
        } else {
            dialog = CommonDialogUtils.getProgressDialog(getActivity(), message);
        }
        if (dialog == null) {
            dialog = CommonDialogUtils.getProgressDialog(getActivity(), message);
        }
        return dialog;
    }

    @Override
    public <T> LifecycleTransformer<T> bind() {
        return bindToLifecycle();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        attachMvpView();
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * [此方法不可再重写]
     */
    public final void attachMvpView() {

        if (mPresenter == null) {
            mPresenter = createPresenter();
            if (mPresenter != null) {
                mPresenter.attachView(this);
            }
        }
    }

    protected abstract T createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    /**
     * 显示选择图片的弹窗
     */
    protected void showChosePicDialog() {

    }

    public void requestTakePicPermissions(final boolean crop) {
        new RxPermissions(getActivity())
                .request(Manifest.permission.CAMERA)
                .subscribe(new MySubscribe<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            takePhoto(crop);
                        }
                    }
                });
    }

    private void takePhoto(boolean crop) {
        String dir = getActivity().getExternalCacheDir() + "/baby/Camera/";
        File destDir = new File(dir);
        if (!destDir.exists()) {
            boolean isCreateSuccess = destDir.mkdirs();
            if (!isCreateSuccess) {
                return;
            }
        }
        File file = new File(dir, new DateFormat().format(
                "yyyy_MMdd_hhmmss", Calendar.getInstance(Locale.CHINA))
                + ".jpg");
        mCameraPicturePath = file.getAbsolutePath();
        try {
//            CameraUtils.openCamera(this, crop ? REQUEST_CHOOSE_CAMERA_AND_CROP : REQUEST_CHOOSE_CAMERA, file);
        } catch (ActivityNotFoundException anf) {
            anf.printStackTrace();
            UiUtil.showToast(getActivity(), R.string.camera_not_prepared);
        }
    }

    public void requestChoosePicPermissions() {
        new RxPermissions(getActivity())
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new MySubscribe<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
//                        if (aBoolean) {
//                            CameraUtils.openPhotos(MyBaseRxFragment.this, REQUEST_CHOOSE_PICTURE_AND_CROP);
//                        } else {
//                            CommonDialogUtils.showSetPermissionDialog(getActivity(), getString(R.string.choosePicPermissionTip));
//                        }
                    }
                });
    }

    protected void onChoosePicResult(String path, Uri result) {
        this.uploadImage = path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i("MyBaseRxFragment : onActivityResult");

    }

}
