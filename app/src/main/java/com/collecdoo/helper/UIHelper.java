package com.collecdoo.helper;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UIHelper extends Application {

    public static Typeface getFontRegularTypeFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "fonts/roboto_regular.ttf");
    }

    public static Typeface getFontLightTypeFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "fonts/roboto_light.ttf");
    }

    public static String getStringFromEditText(EditText content) {

        String strContent = content.getText().toString().trim();
        while (strContent.length() > 0 && strContent.endsWith("\n")) {
            strContent = strContent.substring(0, strContent.length() - 2);
        }
        strContent = strContent.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n");
        return strContent;

    }

    public static String getStringFromTextView(View masterView, int id) {

        String strContent = ((TextView) masterView.findViewById(id)).getText()
                .toString().trim();

        while (strContent.length() > 0 && strContent.endsWith("\n")) {
            strContent = strContent.substring(0, strContent.length() - 2);
        }
        strContent = strContent.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n");
        return strContent;

    }

    public static String getStringFromTextView(View view) {

        String strContent = ((TextView) view).getText().toString().trim();

        while (strContent.length() > 0 && strContent.endsWith("\n")) {
            strContent = strContent.substring(0, strContent.length() - 2);
        }
        strContent = strContent.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n");
        return strContent;

    }

    public static Boolean validateEmailAddress(String content) {
        Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher m = p.matcher(content.trim());
        return content.trim().equals("") ? false : m.matches();

    }

    private void setPicResize(ImageView imageView, String imageFullPath) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFullPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap resizeBitmap = BitmapFactory
                .decodeFile(imageFullPath, bmOptions);
        imageView.setImageBitmap(resizeBitmap);
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the
        // bitmap object
        byte[] b = baos.toByteArray();
        return b;
    }

    public static Boolean isEmpty(TextView textView) {
        Pattern p = Pattern.compile("\\s*");
        Matcher m = p.matcher(textView.getText().toString().trim());
        return m.matches();
    }

    // validate
    public static Boolean validatePass(String content, int minLength,
                                       int maxLength) {
        Boolean isValid = true;
        if (TextUtils.isEmpty(content)) {

            isValid = false;
        } else if (!UIHelper.validateTextViewMinMax(content, minLength,
                maxLength)) {
            isValid = false;
        } else {
            Pattern common = Pattern.compile("[a-zA-Z0-9]+");
            Pattern alpha = Pattern.compile("[a-zA-Z]");
            Pattern numeric = Pattern.compile("[0-9]");
            Matcher mCommon = common.matcher(content.toString().trim());
            Matcher mAlpha = alpha.matcher(content.toString().trim());
            Matcher mNumeric = numeric.matcher(content.toString().trim());

            if (!mCommon.matches() || !mAlpha.find() || !mNumeric.find()) {
                isValid = false;
            }
        }

        return isValid;
    }

    public static Boolean validateTextViewMinMax(String content, int minLength,
                                                 int maxLength) {
        Pattern p = Pattern.compile(".{" + minLength + "," + maxLength + "}");
        Matcher m = p.matcher(content.trim());

        return m.matches();
    }


}
