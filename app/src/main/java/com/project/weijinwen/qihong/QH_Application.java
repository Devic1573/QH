package com.project.weijinwen.qihong;

/**
 * Created by weijinwen on 14-10-6.
 */

import android.app.Application;

import com.project.weijinwen.qihong.QH_AVOS.QH_AVService;

public class QH_Application extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        QH_AVService.AVInit(this, getString(R.string.avos_app_id), getString(R.string.avos_app_key));
    }
}
