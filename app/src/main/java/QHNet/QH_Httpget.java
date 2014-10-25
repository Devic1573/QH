package QHNet;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by weijinwen on 14-10-2.
 */
public class QH_Httpget {

    private static AsyncHttpClient mAsyncclient = new AsyncHttpClient();
    private static SyncHttpClient mSyncclient = new SyncHttpClient();

    static
    {
        mAsyncclient.setTimeout(11000);   //设置链接超时，如果不设置，默认为10s
        mSyncclient.setTimeout(11000);   //设置链接超时，如果不设置，默认为10s
    }
    public static void get(boolean bIsSync, String urlString,AsyncHttpResponseHandler res)    //用一个完整url获取一个string对象
    {
        if (bIsSync) {
            mSyncclient.get(urlString, res);
        }
        else {
            mAsyncclient.get(urlString, res);
        }
    }
    public static void get(boolean bIsSync, String urlString,RequestParams params,AsyncHttpResponseHandler res)   //url里面带参数
    {
        if (bIsSync) {
            mSyncclient.get(urlString, params,res);
        }
        else {
            mAsyncclient.get(urlString, params,res);
        }
    }
    public static void get(boolean bIsSync, String urlString,JsonHttpResponseHandler res)   //不带参数，获取json对象或者数组
    {
        if (bIsSync) {
            mSyncclient.get(urlString,res);
        }
        else {
            mAsyncclient.get(urlString,res);
        }
    }
    public static void get(boolean bIsSync, String urlString,RequestParams params,JsonHttpResponseHandler res)   //带参数，获取json对象或者数组
    {
        if (bIsSync) {
            mSyncclient.get(urlString, params,res);
        }
        else {
            mAsyncclient.get(urlString, params,res);
        }
    }
    public static void get(boolean bIsSync, String urlString, BinaryHttpResponseHandler res)   //下载数据使用，会返回byte数据
    {
        if (bIsSync) {
            mSyncclient.get(urlString,res);
        }
        else {
            mAsyncclient.get(urlString,res);
        }
    }
}
