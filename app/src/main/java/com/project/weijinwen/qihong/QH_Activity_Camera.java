package com.project.weijinwen.qihong;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.project.weijinwen.qihong.QHCamera.QH_CameraInterface;
import com.project.weijinwen.qihong.QHCamera.QH_CameraSurfaceView;
import com.project.weijinwen.qihong.QHCamera.QH_DisplayUtil;

/**
 * Created by weijinwen on 14-10-16.
 */
public class QH_Activity_Camera extends Activity implements QH_CameraInterface.CamOpenOverCallback {

    QH_CameraSurfaceView surfaceView = null;
    ImageButton shutterBtn;
    ImageView imageView;
    float previewRate = -1f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread openThread = new Thread(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                QH_CameraInterface.getInstance().doOpenCamera(QH_Activity_Camera.this);
            }
        };
        openThread.start();
        setContentView(R.layout.avtivity_qh_camera);
        initUI();
        initViewParams();

        shutterBtn.setOnClickListener(new BtnListeners());
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.camera, menu);
//        return true;
//    }

    private void initUI(){
        surfaceView = (QH_CameraSurfaceView)findViewById(R.id.camera_surfaceview);
        shutterBtn = (ImageButton)findViewById(R.id.btn_shutter);
        imageView = (ImageView)findViewById(R.id.camera_ImageView);
    }
    private void initViewParams(){
        LayoutParams params = surfaceView.getLayoutParams();
        Point p = QH_DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = QH_DisplayUtil.getScreenRate(this); //默认全屏的比例预览
        surfaceView.setLayoutParams(params);

        //手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
        LayoutParams p2 = shutterBtn.getLayoutParams();
        p2.width = QH_DisplayUtil.dip2px(this, 80);
        p2.height = QH_DisplayUtil.dip2px(this, 80);;
        shutterBtn.setLayoutParams(p2);

    }

    @Override
    public void cameraHasOpened() {
        // TODO Auto-generated method stub
        SurfaceHolder holder = surfaceView.getSurfaceHolder();
        QH_CameraInterface.getInstance().doStartPreview(holder, previewRate);
    }
    private class BtnListeners implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch(v.getId()){
                case R.id.btn_shutter:
                    QH_CameraInterface.getInstance().doTakePicture();
                    break;
                default:break;
            }
        }

    }

}