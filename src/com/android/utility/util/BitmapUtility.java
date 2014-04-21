
package com.android.utility.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;

public class BitmapUtility {

    public static String toBase64(Bitmap bitmap) {
        if (bitmap == null)
        {
            throw new IllegalArgumentException();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        try
        {
            byteArrayOutputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Drawable toDrawable(Context context, Bitmap bitmap) {
        if (bitmap == null)
        {
            throw new IllegalArgumentException();
        }
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable == null)
        {
            throw new IllegalArgumentException();
        }
        if (drawable instanceof BitmapDrawable)
        {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap toBitmap(Context context, int resource) {
        if ((context == null) || (resource == 0))
        {
            throw new IllegalArgumentException();
        }
        return BitmapFactory.decodeResource(context.getResources(), resource);
    }

    public final static Bitmap Bitmap(String bitmap) {
        if (TextUtils.isEmpty(bitmap))
        {
            throw new IllegalArgumentException();
        }
        byte[] bytes = Base64.decode(bitmap, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
