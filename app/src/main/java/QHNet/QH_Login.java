package QHNet;

import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.weijinwen.qihong.QH_Activity_Signin;
import com.project.weijinwen.qihong.QH_DeviceInfo;
import com.project.weijinwen.qihong.QH_Log;
import com.project.weijinwen.qihong.R;

import org.apache.http.Header;

/**
 * Created by weijinwen on 14-9-27.
 */
public class QH_Login extends AsyncTask<Void, Void, Boolean> {

    QH_Login(String email, String password, QH_Activity_Signin activity) {
        mEmail = email;
        mPassword = password;
        mQHSigninActivity = activity;
        mDeviceInfo = new QH_DeviceInfo(activity);
    }

    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {

            RequestParams reqParams = new RequestParams();
            reqParams.put("username", mEmail);
            reqParams.put("password", mPassword);

            reqParams.put("objectId", mDeviceInfo.GetDeviceID());
            reqParams.put("platform", mDeviceInfo.GetPlatform());

            QH_Httpget.get(true, mQHSigninActivity.getString(R.string.urlString), reqParams, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    QH_Log.i("onStart");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    QH_Log.i("statusCode:" + statusCode + "onSuccess:" + new String(response));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] response, java.lang.Throwable throwable) {
                    QH_Log.i("statusCode:" + statusCode + "onFailure:" + response.toString());
                }

            });
        } catch (Exception e) {
            QH_Log.e(e.toString());
            return false;
        }

//        for (String credential : DUMMY_CREDENTIALS) {
//            String[] pieces = credential.split(":");
//            if (pieces[0].equals(mEmail)) {
//                // Account exists, return true if the password matches.
//                return pieces[1].equals(mPassword);
//            }
//        }

        // TODO: register the new account here.
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        mQHSigninActivity.showProgress(false);

        if (success) {
            mQHSigninActivity.onLoginSuceess();
        } else {
            mQHSigninActivity.onLoginFail();
        }
    }

    @Override
    protected void onCancelled() {
//        mAuthTask = null;
//        showProgress(false);
    }


    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    private final String mEmail;
    private final String mPassword;
    private QH_DeviceInfo mDeviceInfo;
    private QH_Activity_Signin mQHSigninActivity;
}
