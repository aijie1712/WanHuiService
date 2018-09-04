package com.cmeplaza.basemodule.base;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;

import com.cmeplaza.basemodule.base.mvp.RxPresenter;
import com.cmeplaza.basemodule.http.MySubscribe;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by klx on 2017/9/15.
 * 基 Activity
 */

public abstract class MyBaseRxActivity<T extends RxPresenter> extends CommonBaseActivity {
    private static final int REQUEST_CLIP_IMAGE = 2028;
    private static final int REQUEST_CHOOSE_PICTURE_AND_CROP = 0xac22;
    private static final int REQUEST_CHOOSE_CAMERA_AND_CROP = 0xac23;
    private static final int REQUEST_CHOOSE_CAMERA = 0xac24;
    private static final String EXTRA_RESTORE_PHOTO = "extra_restore_photo";
    private static String mCameraPicturePath;
    protected T mPresenter;
    protected String uploadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        attachView();
        super.onCreate(savedInstanceState);
    }

    /**
     * [此方法不可再重写]
     */
    protected final void attachView() {
        if (mPresenter == null) {
            mPresenter = createPresenter();
            mPresenter.attachView(this);
        }
    }

    protected abstract T createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
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

    /**
     * 显示选择图片的弹窗
     */
    protected void showChosePicDialog() {
//        CommonDialogUtils.showChoosePicDialog(this, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestTakePicPermissions(true);
//            }
//        }, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 从相册中选择
//                requestChoosePicPermissions();
//            }
//        });
    }

    public void requestTakePicPermissions(final boolean crop) {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA)
                .subscribe(new MySubscribe<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            takePhoto(crop);
                        } else {
//                            CommonDialogUtils.showSetPermissionDialog(MyBaseRxActivity.this, getString(R.string.takePicPermissionTip));
                        }
                    }
                });
    }

    private void takePhoto(boolean crop) {
        String dir = getExternalCacheDir() + "/baby/Camera/";
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
//        try {
//            CameraUtils.openCamera(this, crop ? REQUEST_CHOOSE_CAMERA_AND_CROP : REQUEST_CHOOSE_CAMERA, file);
//        } catch (ActivityNotFoundException anf) {
//            anf.printStackTrace();
//            UiUtil.showToast(this, R.string.camera_not_prepared);
//        }
    }

    public void requestChoosePicPermissions() {
        new RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new MySubscribe<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
//                        if (aBoolean) {
//                            CameraUtils.openPhotos(MyBaseRxActivity.this, REQUEST_CHOOSE_PICTURE_AND_CROP);
//                        } else {
//                            CommonDialogUtils.showSetPermissionDialog(MyBaseRxActivity.this, getString(R.string.choosePicPermissionTip));
//                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case REQUEST_CHOOSE_CAMERA_AND_CROP:
//                    LogUtils.i("aaa1");
//                    if (TextUtils.isEmpty(mCameraPicturePath)) {
//                        UiUtil.showToast(this, R.string.camera_not_prepared);
//                    } else {
//                        Uri parse = Uri.parse("file://" + mCameraPicturePath);
//                        String par = FileUtils.getPath(this, parse);
//                        Uri croppedAvatar = Uri.fromFile(new File(getCacheDir(), "croppedAvatar"));
//                        String photoPath = FileUtils.getPath(this, croppedAvatar);
//                        ClipImageActivity.prepare()
//                                .aspectX(3).aspectY(3)
//                                .inputPath(par).outputPath(photoPath)
//                                .startForResult(this, REQUEST_CLIP_IMAGE);
//                    }
//                    break;
//                case REQUEST_CHOOSE_PICTURE_AND_CROP:
//                    LogUtils.i("aaa2");
//                    Uri source = data.getData();
//                    String original = FileUtils.getPath(this, source);
//                    if (original != null) {
//                        source = Uri.parse("file://" + original);
//                    }
//                    Uri destination = Uri.fromFile(new File(getCacheDir(), "croppedAvatar"));
//                    String url = FileUtils.getPath(this, destination);
//                    ClipImageActivity.prepare()
//                            .aspectX(3).aspectY(3)
//                            .inputPath(original).outputPath(url)
//                            .startForResult(this, REQUEST_CLIP_IMAGE);
//                    break;
//                case REQUEST_CHOOSE_CAMERA:
//                    LogUtils.i("aaa3");
//                    if (TextUtils.isEmpty(mCameraPicturePath)) {
//                        UiUtil.showToast(this, R.string.camera_not_prepared);
//                    } else {
//                        Uri parse = Uri.parse("file://" + mCameraPicturePath);
////                        String par = FileUtils.getPath(this, parse);
////                        Uri croppedAvatar = Uri.fromFile(new File(getCacheDir(), "croppedAvatar"));
//                        String photoPath = FileUtils.getPath(this, parse);
//                        onChoosePicResult(photoPath, Uri.parse("file://" + photoPath));
//                    }
//                    break;
//                case REQUEST_CLIP_IMAGE://剪切图片
//                    LogUtils.i("aaa4");
//                    Uri avatarUri1 = Uri.fromFile(new File(getCacheDir(), "croppedAvatar"));
//                    String photoPath1 = FileUtils.getPath(this, avatarUri1);
//                    Bitmap bitmap1 = BitmapLoader.getBitmapFromFile(photoPath1, 720,
//                            1280);
//                    final String thumbnailPath1 = BitmapLoader.saveBitmapToLocal(this, bitmap1);
//                    onChoosePicResult(thumbnailPath1, Uri.parse("file://" + thumbnailPath1));
//                    break;
//
//            }
//        } else {
////            if (requestCode == REQUEST_CROP && data != null) {
////                Throwable throwable = Crop.getError(data);
////                if (throwable != null && throwable instanceof OutOfMemoryError) {
////                    Toast.makeText(this, R.string.selection_too_large, Toast.LENGTH_SHORT).show();
////                }
////            }
//        }
    }

    protected void onChoosePicResult(String path, Uri result) {
        this.uploadImage = path;
    }
}
