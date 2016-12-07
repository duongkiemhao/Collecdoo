package com.collecdoo.helper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.collecdoo.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by ACER4830 on 8/6/13.
 */
public class ImageHelper {

    public static Bitmap getBitmapFromURL(Context ctx, String url)
            throws IOException {
        InputStream is = (InputStream) fetch(url);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        return bitmap;

    }

    public static Bitmap getBitmapFromURL(Context ctx, String url,
                                          int reqWidth, int reqHeight) throws IOException {

        Bitmap bitmap = getBitmapFromURL(ctx, url);
        if (bitmap == null)
            return null;
        return getResizedBitmap(bitmap, reqWidth, reqHeight);
    }

    public static Bitmap getResizedBitmap(Bitmap image, int newHeight,
                                          int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map

        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        Log.d("-----------",
                resizedBitmap.getHeight() + "_" + resizedBitmap.getWidth());
        return resizedBitmap;
    }

    static Object fetch(String address) throws MalformedURLException,
            IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }

    //
    // public static String encodeBitmapBase64(Bitmap bitmap) {
    // if (bitmap == null)
    // return "";
    //
    // return Base64.encodeToString(UIHelper.convertBitmapToByteArray(bitmap),
    // Base64.NO_WRAP);
    // }
    //
    // public static Bitmap decodeBitmapBase64(String id) {
    // if (TextUtils.isEmpty(id))
    // return null;
    // byte[] bitmapdata = Base64.decode(id, Base64.DEFAULT);
    // return BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
    // }

    public static int getOrientationFromAlbum(Context context, Uri photoUri) {
        // normal landscape: 0
        // normal portrait: 90
        // upside-down landscape: 180
        // upside-down portrait: 270
        // image not found: -1
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                null, null, null);

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            } else {
                return -1;
            }
        } finally {
            cursor.close();
        }
    }

    public static int getOrientationFromCamera(Context context,
                                               String cameraImageFullPath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(cameraImageFullPath);
        } catch (IOException e) {
            Utility.showMessage(context, e.getMessage());
        }
        String orientstring = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientstring != null ? Integer.parseInt(orientstring)
                : ExifInterface.ORIENTATION_NORMAL;
        int rotateangle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
            rotateangle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
            rotateangle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
            rotateangle = 270;
        return rotateangle;
    }

    public static File createFile(Context context, String fileName) {
        File storageDir = context.getExternalCacheDir();
        File image = new File(storageDir, fileName);
        // ameraImageFullPath = image.getAbsolutePath();
        return image;
    }

    public static void saveBitmapToLocal(Bitmap bitmap, String filePath) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your
            // Bitmap
            // instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
                                                      int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        int heightRatio = (int) Math.ceil(height / (float) reqHeight);
        int widthRatio = (int) Math.ceil(width / (float) reqWidth);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                inSampleSize = heightRatio;
            } else {
                inSampleSize = widthRatio;
            }
        }

        return inSampleSize;
    }

    public static String getImagePathFromAlbum(Context context, Uri photoUri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(photoUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }
}
