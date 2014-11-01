package com.project.weijinwen.qihong.QH_AVOS;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.project.weijinwen.qihong.QH_Activity_Signin;
import com.project.weijinwen.qihong.QH_DeviceInfo;
import com.project.weijinwen.qihong.QH_Log;

/**
 * Created by weijinwen on 14-10-7.
 */
public class QH_AVOSUser {

    public QH_AVOSUser(String email, String password, QH_Activity_Signin activity) {
        mEmail = email;
        mPassword = password;
        mQHSigninActivity = activity;
        mDeviceInfo = new QH_DeviceInfo(activity);
    }

    public static boolean CheckCurrentUser() {
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            // 允许用户使用应用
            return true;
        } else {
            //缓存用户对象为空时， 可打开用户注册界面…
            return false;
        }
    }

    public static void ClearUser() {
        AVUser.logOut();             //清除缓存用户对象
        AVUser currentUser = AVUser.getCurrentUser(); // 现在的currentUser是null了
        if (null == currentUser) {
            QH_Log.i("clear is ok!");
        } else {
            QH_Log.e("clear is fail!");
        }
    }

    public void runSignin() {
        AVUser user = new AVUser();
        user.setUsername(mDeviceInfo.GetDeviceID());
        user.setPassword(mPassword);
        user.setEmail(mEmail);
        user.put("platform", mDeviceInfo.GetPlatform());
        user.put("platformName", mDeviceInfo.GetPlatform());

        user.signUpInBackground(new SubSignUpCallback());
    }

    private class SubSignUpCallback extends SignUpCallback {
        @Override
        public void done(AVException e) {
            if(null == e) {
                QH_Log.i("sign up success!");
                mQHSigninActivity.onLoginSuceess();
            }
            else {
                QH_Log.i("sign up failed!");
                QH_Log.i(e.getMessage());
                mQHSigninActivity.onLoginFail();
            }
        }
    }

    public void runLogin() {
        AVUser.logInInBackground(mDeviceInfo.GetDeviceID(), mPassword, new SubLogInCallback());
    }

    private class SubLogInCallback extends LogInCallback {
        public void done(AVUser user, AVException e) {
            if (user != null) {
                QH_Log.i("login success");
                mQHSigninActivity.onLoginSuceess();
            } else {
                QH_Log.i("login fail");
                runSignin();
            }
        }
    }

    private final String mEmail;
    private final String mPassword;
    private QH_DeviceInfo mDeviceInfo;
    private QH_Activity_Signin mQHSigninActivity;
}
