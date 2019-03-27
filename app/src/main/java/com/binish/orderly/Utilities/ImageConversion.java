package com.binish.orderly.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ImageConversion {
    public static byte[] getBlob(Bitmap bitmap)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
        byte[] bArray = bos.toByteArray();
        return bArray;
    }
    public static Bitmap getBitmap(byte[] byteArray)
    {
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
    }
}
