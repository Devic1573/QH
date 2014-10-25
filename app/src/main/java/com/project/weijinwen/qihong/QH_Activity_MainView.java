package com.project.weijinwen.qihong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class QH_Activity_MainView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_qh_mainview);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_qh_mainview_titlebar);

        super.onCreate(savedInstanceState);
        QH_Log.i("QH_Activity_View onCreate");

        Button mbtPublic = (Button) findViewById(R.id.button_public);

        mbtPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QH_Activity_MainView.this, QH_Activity_Public_Editer.class));
            }
        });
    }
}
