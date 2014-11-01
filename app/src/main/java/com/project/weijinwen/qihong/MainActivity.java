package com.project.weijinwen.qihong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.project.weijinwen.qihong.QH_AVOS.QH_AVOSUser;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(QH_AVOSUser.CheckCurrentUser()) {
            //TODO: regist avtivity
            QH_Log.i("main activity avosuser is ok!");
            startActivity(new Intent(this, QH_Activity_MainView.class));
        }
        else {
            QH_AVOSUser.ClearUser();
            startActivity(new Intent(this, QH_Activity_Signin.class));
        }

        finish();
    }
}
