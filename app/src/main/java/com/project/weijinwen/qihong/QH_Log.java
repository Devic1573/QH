package com.project.weijinwen.qihong;

/**
 * Created by weijinwen on 14-9-27.
 */

import android.util.Log;

public final class QH_Log {

    public static int i(String msg) {
        return Log.i(TAG, msg);
    }

    public static int e(String msg) {
        return Log.e(TAG, msg);
    }

    public static int d(String msg) {
        return Log.d(TAG, msg);
    }

    public static int v(String msg) {
        return Log.v(TAG, msg);
    }

    private static final String TAG = "qihong";
}
