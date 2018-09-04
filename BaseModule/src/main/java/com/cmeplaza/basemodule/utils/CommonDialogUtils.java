package com.cmeplaza.basemodule.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.cmeplaza.basemodule.R;
import com.cmeplaza.basemodule.widget.dialog.CustomDialog;

/**
 * Created by klx on 2018/7/25.
 * dialogUtils
 */

public class CommonDialogUtils {
    public static void showSetPermissionDialog(final Activity activity, String title) {
        showSetPermissionDialog(activity, title, null, null);
    }

    public static void showSetPermissionDialog(final Activity activity, String title,
                                               final DialogInterface.OnClickListener cancelClick,
                                               final DialogInterface.OnClickListener confirmClick) {
        new AlertDialog.Builder(activity)
                .setCancelable(true)
                .setTitle(title)
                .setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (cancelClick != null) {
                            cancelClick.onClick(dialog, which);
                        }
                    }
                })
                .setPositiveButton(activity.getString(R.string.goSetting), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (confirmClick != null) {
                            confirmClick.onClick(dialog, which);
                            return;
                        }
                        Intent intent = UiUtil.getAppDetailSettingIntent(activity);
                        activity.startActivity(intent);
                    }
                })
                .show();
    }

    /**
     * 选择图片的弹框
     *
     * @param context
     * @param onTackFromCamera 拍照
     * @param onPickFromDic    从相册中选择
     */
    public static void showChoosePicDialog(Activity context, final View.OnClickListener onTackFromCamera, final View.OnClickListener onPickFromDic) {
        showBottomChooseDialog(R.string.pic_tack_from_album, R.string.pic_tack_camera, context, onPickFromDic, onTackFromCamera);
    }

    /**
     * @param first            第一个显示的文本
     * @param second           第二个显示的文本
     * @param context
     * @param oneClickListener 第一个点击的监听
     * @param twoClickListener 第二个点击的监听
     */
    public static void showBottomChooseDialog(int first, int second, Activity context,
                                              final View.OnClickListener oneClickListener, final View.OnClickListener twoClickListener) {
        View pview = LayoutInflater.from(context).inflate(
                R.layout.layout_choose_photo_select, null);
        TextView tv_pick_from_take = (TextView) pview
                .findViewById(R.id.tv_pick_from_take);
        TextView tv_pick_from_dicm = (TextView) pview
                .findViewById(R.id.tv_pick_from_dicm);
        TextView tv_show_cancel = (TextView) pview
                .findViewById(R.id.tv_show_cancel);

        tv_pick_from_take.setText(first);
        if (-1 != second) {
            tv_pick_from_dicm.setVisibility(View.VISIBLE);
            tv_pick_from_dicm.setText(second);
        } else {
            tv_pick_from_dicm.setVisibility(View.GONE);
        }

        final CustomDialog builder = new CustomDialog(context, R.style.my_dialog)
                .create(pview, true, 1f, 0.25f, 1);
        builder.show();

        tv_show_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        tv_pick_from_take.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (oneClickListener != null) {
                    oneClickListener.onClick(v);
                }
                builder.dismiss();
            }
        });
        tv_pick_from_dicm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (twoClickListener != null) {
                    twoClickListener.onClick(v);
                }
                builder.dismiss();
            }
        });
        tv_show_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
    }

    public static void showConfirmDialog(Activity activity, String message, View.OnClickListener onConfirmClick) {
        showConfirmDialog(activity, "", "", message, null, onConfirmClick);
    }

    /**
     * 显示确认弹窗
     *
     * @param activity
     * @param message        提示消息
     * @param onConfirmClick 确认点击监听
     */
    public static void showConfirmDialog(Activity activity, String leftText, String rightText, String message,
                                         final View.OnClickListener onCancelClick, final View.OnClickListener onConfirmClick) {
        final CustomDialog dialog = new CustomDialog(activity, R.style.Dialog);
        View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_confirm, null);
        dialog.addContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tv_message = (TextView) layout.findViewById(R.id.tv_message);
        TextView tv_cancel = (TextView) layout.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) layout.findViewById(R.id.tv_confirm);

        tv_message.setText(message);
        if (!TextUtils.isEmpty(leftText)) {
            tv_cancel.setText(leftText);
        }
        if (!TextUtils.isEmpty(rightText)) {
            tv_confirm.setText(rightText);
        }

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onCancelClick != null) {
                    onCancelClick.onClick(v);
                }
            }
        });

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onConfirmClick != null) {
                    onConfirmClick.onClick(v);
                }
            }
        });
        dialog.show();
    }

    public static void setDialogSize(Activity context, float widthProportion, float heightProportion,
                                     View rootView) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeigh = dm.heightPixels;
        int screenWidth = dm.widthPixels;

        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
        if (heightProportion >= 0) {
            layoutParams.height = (int) (screenHeigh * heightProportion);// 高度设置为屏幕高度比
        }
        layoutParams.width = (int) (screenWidth * widthProportion);// 宽度设置为屏幕的宽度比
        rootView.setLayoutParams(layoutParams);
    }

    /**
     * 显示加载进度
     *
     * @param message
     */
    public static AlertDialog getProgressDialog(Activity context, String message) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_progressing, null);
        TextView tv_message = rootView.findViewById(R.id.tv_message);
        tv_message.setText(message);
        AlertDialog alertDialog = createDialog(context, rootView);
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    public static AlertDialog createDialog(Context context, View rootView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.default_dialog_style);
        builder.setView(rootView);
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        return alertDialog;
    }
}
