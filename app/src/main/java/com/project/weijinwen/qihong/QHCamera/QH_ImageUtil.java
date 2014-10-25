package com.project.weijinwen.qihong.QHCamera;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by weijinwen on 14-10-13.
 */
public class QH_ImageUtil {

    /**
     * 旋转Bitmap
     * @param b
     * @param rotateDegree
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate((float)rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }
}
