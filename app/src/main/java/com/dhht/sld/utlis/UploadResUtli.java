package com.dhht.sld.utlis;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.dhht.sld.main.identity.view.IdentityActivity;
import com.tamsiree.rxkit.RxFileTool;
import com.tamsiree.rxkit.RxPhotoTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ---------------------------------------------------------------
 * 作者：HanSheng
 * 邮箱：164897033@qq.com
 * 创建时间：2020/7/31  10:55
 * 文件描述：上传相关
 * ---------------------------------------------------------------
 */
public class UploadResUtli {

    private Context mContext;
    private Bitmap photo;
    private String picPath;

    public UploadResUtli(Context context){
        mContext = context;
    }

    public String resFilePath(Intent data){
        Log.e("debug","data"+data);
        if (data == null) return "";
        Uri uri = data.getData();
        if (uri != null) {
            photo = BitmapFactory.decodeFile(uri.getPath());
        }

        Bundle bundle = data.getExtras();
        if (bundle != null) {
            photo = (Bitmap) bundle.get("data");
        } else {
            ((IdentityActivity) mContext).showTip("拍照失败");
            return "";
        }

        FileOutputStream fileOutputStream = null;
        try {
            // 获取本应用图片缓存目录
            String saveDir = RxFileTool.getCacheFolder(mContext) + "";
            // 新建目录
            File dir = new File(saveDir);
            if (!dir.exists()) {
                if (!dir.mkdir()) throw new Exception("文件夹创建失败：" + saveDir);
            }
            // 生成文件名
            SimpleDateFormat t = new SimpleDateFormat("yyyyMMddssSSS");
            String filename = "MT" + (t.format(new Date())) + ".jpg";
            // 新建文件
            File file = new File(saveDir, filename);
            // 打开文件输出流
            fileOutputStream = new FileOutputStream(file);
            // 生成图片文件
            photo.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            // 相片的完整路径
            picPath = file.getPath();
            Log.e("debug","picPath"+picPath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return picPath;
    }
    /**
     * @return 创建缓存图片路径
     */
    public Uri createAppCacheFileUri(){
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        Uri destinationUri = Uri.fromFile(new File(RxFileTool.getCacheFolder(mContext), imageName + ".jpeg"));
        return destinationUri;
    }

    /**
     * @param filePath
     * @param imageSize
     * @return
     */
    public String copyCameraPiccture(String filePath, int imageSize){
        Uri appCacheFileUri = createAppCacheFileUri();
        String targetPath = RxPhotoTool.getImageAbsolutePath(mContext, appCacheFileUri);
        FileOutputStream fileOutputStream = null;
        try {
            if (TextUtils.isEmpty(targetPath))return "";
            // 打开文件输出流
            fileOutputStream = new FileOutputStream(targetPath);
            Bitmap bitmap=compressScale(filePath,imageSize);
            // 生成图片文件
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            // 相片的完整路径
            Log.e("debug","picPath"+picPath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return targetPath;
    }


    /**
     * @param filePath 原文件图片
     * @param imageSize 图片最大大小
     * @return Bitmap
     */
    public static Bitmap compressScale(String filePath,int  imageSize) {
        Bitmap image = BitmapFactory.decodeFile(filePath);
        int degree = getRotateAngle(filePath);//获取相片拍摄角度

        if (degree != 0) {//旋转照片角度，防止头像横着显示
            image = setRotateAngle(degree, image);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        Log.e("debug","h"+h+"         w"+w);
        float hh = 1920;
        float ww = 1080;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be; // 设置缩放比例
        // newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap,imageSize);// 压缩好比例大小后再进行质量压缩
        //return bitmap;
    }

    public static Bitmap compressImage(Bitmap image, int imageSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>imageSize) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    /**
     * 获取图片的旋转角度
     *
     * @param filePath
     * @return
     */
    public static int getRotateAngle(String filePath) {
        int rotate_angle = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate_angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate_angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate_angle = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate_angle;
    }

    /**
     * 旋转图片角度
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap setRotateAngle(int angle, Bitmap bitmap) {

        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(angle);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;

    }

}
