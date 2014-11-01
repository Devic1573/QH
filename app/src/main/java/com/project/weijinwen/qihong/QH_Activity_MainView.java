package com.project.weijinwen.qihong;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.project.weijinwen.qihong.QH_AVOS.QH_AVObject;
import com.project.weijinwen.qihong.QH_AVOS.QH_AVService;

import java.util.List;


public class QH_Activity_MainView extends ListActivity {

    private volatile List<QH_AVObject> qhobjects;
    private EditText searchInput;

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        // Override this method to do custom remote calls
        @Override
        protected Void doInBackground(Void... params) {
            qhobjects = QH_AVService.findQHAVObjects();
            return null;
        }

        @Override
        protected void onPreExecute() {
//            QH_Activity_MainView.this.progressDialog =
//                    ProgressDialog.show(ToDoListActivity.this, "", "Loading...", true);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // 展现ListView
            QH_ListAdapter adapter = new QH_ListAdapter(QH_Activity_MainView.this, qhobjects);
            setListAdapter(adapter);
            registerForContextMenu(getListView());
//            QH_Activity_MainView.this.progressDialog.dismiss();
            TextView empty = (TextView) findViewById(android.R.id.empty);
            if (qhobjects != null && !qhobjects.isEmpty()) {
                empty.setVisibility(View.INVISIBLE);
            } else {
                empty.setVisibility(View.VISIBLE);
            }
        }
    }


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

        TextView empty = (TextView) findViewById(android.R.id.empty);
        empty.setVisibility(View.VISIBLE);
        searchInput = new EditText(this);
        new RemoteDataTask().execute();
    }
}
