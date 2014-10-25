package com.project.weijinwen.qihong;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by weijinwen on 14-10-18.
 */
public class QH_Activity_Public_Editer extends Activity {

    private final static int REQUESTCODE_TAKE_PHOTO = 0;
    private final static int REQUESTCODE_PICK_PHOTO = 1;
    private final static int REQUESTCODE_TAKEPICK_PHOTO = 2;

    private final static int QH_SUBMIT_STATUS_IDLE = 0;
    private final static int QH_SUBMIT_STATUS_UPLOADING = 1;
    private final static int QH_SUBMIT_STATUS_FAILED = 2;
    private final static int QH_SUBMIT_STATUS_SUCCESS = 3;

    private QH_Activity_SelectPicPopupWindow menuWindow;
    private ImageButton mbtImage;
    private EditText mEditText;
    private Button mbtSubmit;

    private Uri mTakePicUri;
    private Uri mPickPicUri;
    private static Uri mSubmitPicUri = Uri.EMPTY;
    private static int msSubmitStatus = QH_SUBMIT_STATUS_IDLE;

    private static void clearTmpFile() {

        if (msSubmitStatus == QH_SUBMIT_STATUS_UPLOADING
                || msSubmitStatus == QH_SUBMIT_STATUS_FAILED) {
            return;
        }

        String string = UritoAbsPath(mSubmitPicUri);
        if (string.isEmpty()) {
            return;
        }

        File file = new File(string);
        if (null != file) {
            file.delete();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DisplayMetrics displaysMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
        int iWidth = displaysMetrics.widthPixels;


        LinearLayout layout= new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);//显示方向

        mbtImage = new ImageButton(this);
        LayoutParams iamgeparams = new LayoutParams(iWidth/2, iWidth/2);
        mbtImage.setLayoutParams(iamgeparams);
        mbtImage.setBackgroundResource(android.R.drawable.ic_menu_add);

        mEditText = new EditText(this);
        LayoutParams textparams = new LayoutParams((iWidth - iWidth/2), iWidth/2);
        mEditText.setLayoutParams(textparams);
        mEditText.setHint(R.string.qh_addpichint_comment);
        mEditText.setBackground(null);
        mEditText.setEnabled(false);

        QH_Log.i("Metrics width:"+displaysMetrics.widthPixels);

        layout.addView(mbtImage);
        layout.addView(mEditText);

        View view = getPartView();
        layout.addView(view);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(layout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_public_editer_titlebar);

        super.onCreate(savedInstanceState);

        mbtImage.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                menuWindow = new QH_Activity_SelectPicPopupWindow(QH_Activity_Public_Editer.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.layout_public_editer), Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        mbtSubmit = (Button) findViewById(R.id.button_public);
        mbtSubmit.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {

                if (msSubmitStatus == QH_SUBMIT_STATUS_UPLOADING) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.qh_submit_toast_uploading_comment),
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                if (mSubmitPicUri == Uri.EMPTY) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.qh_submit_toast_noimage_comment),
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    return;
                }

                if (mEditText.getText().toString().isEmpty()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(QH_Activity_Public_Editer.this);
                    alertDialog.setMessage(R.string.qh_submit_toast_nodetail_comment);
                    alertDialog.setPositiveButton(R.string.qh_submit_toast_nodetail_comment_ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        uploadImageDetail(UritoAbsPath(mSubmitPicUri), mEditText.getText().toString());
                                    }
                                });

                    alertDialog.setNegativeButton(R.string.qh_submit_toast_nodetail_comment_no,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        mEditText.setEnabled(true);
                                    }
                                });
                    alertDialog.setInverseBackgroundForced(true);
                    alertDialog.show();

                    return;
                }
                uploadImageDetail(UritoAbsPath(mSubmitPicUri), mEditText.getText().toString());
             }
        });
    }

    //通过加载xml文件将view添加到布局中
    private View getPartView() {
        //将xml布局文件生成view对象通过LayoutInflater
        LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //将view对象挂载到那个父元素上，这里没有就为null
        return inflater.inflate(R.layout.activity_public_editer, null);
    }

    //为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener(){

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo: {
                    mTakePicUri = CreateImageUri();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mTakePicUri);
                    startActivityForResult(intent, REQUESTCODE_TAKE_PHOTO);//or TAKE_SMALL_PICTURE

                    break;
                }
                case R.id.btn_pick_photo: {
                    mPickPicUri = CreateImageUri();
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                    editPhoto(intent, mPickPicUri, REQUESTCODE_PICK_PHOTO);
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        switch (requestCode) {
            case REQUESTCODE_TAKE_PHOTO: {
                QH_Log.e("uri:"+mTakePicUri.toString());
                Intent intent = new Intent("com.android.camera.action.CROP");
                editPhoto(intent, mTakePicUri, REQUESTCODE_TAKEPICK_PHOTO);
                QH_Log.e("REQUESTCODE_TAKE_PHOTO");
                break;
            }
            case REQUESTCODE_PICK_PHOTO: {
                QH_Log.e("REQUESTCODE_PICK_PHOTO-->width:"+mbtImage.getWidth());
                editPhotoEnd(mPickPicUri);
                break;
            }
            case REQUESTCODE_TAKEPICK_PHOTO: {
                QH_Log.e("REQUESTCODE_TAKEPICK_PHOTO-->width:"+mbtImage.getWidth());
                editPhotoEnd(mTakePicUri);
                break;
            }
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void editPhoto(Intent intent, Uri uri, int requestCode){
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    private Uri CreateImageUri() {
        File parentPath = Environment.getExternalStorageDirectory();
        long dataTake = System.currentTimeMillis();
        String storagePath = parentPath.getAbsolutePath()+"/" + "QHPIC";
        String jpegName = storagePath + "/" + dataTake +".jpg";

        File file = new File(jpegName);
        Uri uri = Uri.fromFile(file);

        return uri;
    }

    private static String UritoAbsPath(Uri uri) {
        QH_Log.d("uri is " + uri);
        if (uri.getScheme().equals("file")) {
            return uri.getEncodedPath();
        }
        else {
            QH_Log.e("uri format error:"+uri);
            return "";
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private void editPhotoEnd(Uri picuri) {
        Bitmap bitmap = decodeUriAsBitmap(picuri);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, mbtImage.getWidth(), mbtImage.getWidth());
        mbtImage.setImageBitmap(bitmap);

        mEditText.setEnabled(true);

        mSubmitPicUri = picuri;
    }

    private void uploadImageDetail(String filepath, String subText) {

        QH_Log.i("filepath:"+filepath);
        QH_Log.i("text:"+subText);

        AVFile avfile = null;
        try {
            avfile = AVFile.withAbsoluteLocalPath("image.jpg", filepath);
        } catch (IOException e) {
            QH_Log.e(e.getMessage());
        }
        assert avfile != null;
        AVObject avObject = new AVObject(getString(R.string.qh_service_postclass));
        avObject.put(getString(R.string.qh_service_postitem_file), avfile);
        avObject.put(getString(R.string.qh_service_postitem_detail), new String(subText));

        msSubmitStatus = QH_SUBMIT_STATUS_UPLOADING;
        mEditText.setEnabled(false);
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e!=null){
                    QH_Log.i("upload failed:"+e.getMessage());
                    msSubmitStatus = QH_SUBMIT_STATUS_FAILED;
                    finish();
                    //上传失败
                }else{
                    QH_Log.i("upload success");
                    msSubmitStatus = QH_SUBMIT_STATUS_SUCCESS;
                    QH_Activity_Public_Editer.clearTmpFile();
                    finish();
                    //上传成功
                }
            }
        });
    }
}
