package com.project.weijinwen.qihong.QH_AVOS;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

/**
 * Created by weijinwen on 14-10-26.
 */

@AVClassName(QH_AVObject.QH_OBJECT_CLASS)
public class QH_AVObject extends AVObject {

    static final String QH_OBJECT_CLASS = "Post";
    private static final String IMAGE_KEY = "image";
    private static final String DETAIL_KEY = "detail";

    public String getContent() {
        return this.getString(DETAIL_KEY);
    }

    public void setContent(String content) {
        this.put(DETAIL_KEY, content);
    }

    public AVFile getImage() { return this.getAVFile(IMAGE_KEY);}
}
