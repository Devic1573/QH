package com.project.weijinwen.qihong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.GetDataCallback;
import com.project.weijinwen.qihong.QH_AVOS.QH_AVObject;

import java.util.List;

/**
 * Created by weijinwen on 14-10-26.
 */
public class QH_ListAdapter extends BaseAdapter {

    Context mContext;
    List<QH_AVObject> qhobjects;

    public QH_ListAdapter(Context context, List<QH_AVObject> qhobjects) {
        mContext = context;
        this.qhobjects = qhobjects;
    }

    @Override
    public int getCount() {
        return qhobjects != null ? qhobjects.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if (qhobjects != null)
            return qhobjects.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.qh_listview_item, null);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);

            DisplayMetrics displaysMetrics = new DisplayMetrics();
            displaysMetrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
            int iWidth = displaysMetrics.widthPixels;
            LayoutParams ff = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, iWidth);
            holder.imageView.setLayoutParams(ff);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        final QH_AVObject qhobject = qhobjects.get(position);
        if (null == qhobject) {
            return convertView;
        }

        holder.textView.setText(qhobject.getContent());

        QH_Log.i("getviewimage uri:"+qhobject.getImage().getUrl());

        qhobject.getImage().getDataInBackground(new subGetDataCallback(convertView));

        holder.imageView.setBackgroundResource(android.R.drawable.ic_input_get);

        return convertView;
    }

    private class subGetDataCallback extends GetDataCallback {

        private final ViewHolder mholder;

        public subGetDataCallback(View convertView){
            this.mholder = (ViewHolder)convertView.getTag();
        }

        @Override
        public void done(byte[] bytes, AVException e) {
            QH_Log.i("get image status[length:"+bytes.length+"]");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mholder.imageView.setImageBitmap(bitmap);
        }
    }

    static class ViewHolder
    {
        TextView textView;
        ImageView imageView;
    }

}

