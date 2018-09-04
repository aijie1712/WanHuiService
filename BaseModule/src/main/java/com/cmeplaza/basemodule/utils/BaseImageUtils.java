package com.cmeplaza.basemodule.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.cmeplaza.basemodule.CoreLib;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by klx on 2018/8/3.
 */

public class BaseImageUtils {
       /**
     * 保存View为图片的方法
     */
    public static String saveViewToBitmap(View v, String name) {
        String fileName = name + ".png";
        Bitmap bm = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        v.draw(canvas);
        return saveBitmap(bm, fileName, true);
    }

    /**
     * 保存bitmap到SD卡
     *
     * @param bitmap
     * @param imageName
     */
    public static String saveBitmap(Bitmap bitmap, String imageName, boolean isShowTip) {
        return saveBitmap(bitmap, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), imageName, isShowTip);
    }

    /**
     * 保存bitmap到SD卡
     *
     * @param bitmap
     * @param imageName
     */
    public static String saveBitmap(Bitmap bitmap, String pathStr, String imageName, boolean isShowTip) {
        File path = new File(pathStr);
        FileOutputStream fos = null;
        File targetFile = new File(path, imageName);
        if (bitmap == null && targetFile.exists()) {
            return targetFile.getAbsolutePath();
        }
        try {
            if (bitmap != null) {
                fos = new FileOutputStream(new File(path, imageName));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
                // 最后通知图库更新
                CoreLib.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(new File(path.getPath()))));
                if (isShowTip) {
                    Toast.makeText(CoreLib.getContext(), "图片已保存到" + targetFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                }
                return targetFile.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
