package com.project.weijinwen.qihong;

/**
 * Created by weijinwen on 14-10-6.
 */

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

public class QH_Application extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, getString(R.string.avos_app_id), getString(R.string.avos_app_key));
    }
}
