package com.project.weijinwen.qihong.QH_AVOS;

import android.content.Context;
import android.text.TextUtils;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.search.AVSearchQuery;
import com.project.weijinwen.qihong.QH_Log;

import java.util.Collections;
import java.util.List;

/**
 * Created by weijinwen on 14-10-26.
 */
public class QH_AVService {
    public static void AVInit(Context ctx, String applicationId, String clientKey) {
        // 初始化应用 Id 和 应用 Key，您可以在应用设置菜单里找到这些信息
        AVOSCloud.initialize(ctx, applicationId, clientKey);
        // 启用崩溃错误报告
        AVAnalytics.enableCrashReport(ctx, true);
        // 注册子类
        AVObject.registerSubclass(QH_AVObject.class);
    }

    public static void fetchQHAVObjectById(String objectId,GetCallback<AVObject> getCallback) {
        QH_AVObject qhAvObject = new QH_AVObject();
        qhAvObject.setObjectId(objectId);
        // 通过Fetch获取content内容
        qhAvObject.fetchInBackground(getCallback);
    }

    public static void createOrUpdateQHAvObject(String objectId, String content, SaveCallback saveCallback) {
        final QH_AVObject qhAvObject = new QH_AVObject();
        if (!TextUtils.isEmpty(objectId)) {
            // 如果存在objectId，保存会变成更新操作。
            qhAvObject.setObjectId(objectId);
        }
        qhAvObject.setContent(content);
        // 异步保存
        qhAvObject.saveInBackground(saveCallback);
    }

    public static List<QH_AVObject> findQHAVObjects() {
        QH_Log.i("findQHAVObjects enter");

        // 查询当前QHAVObject列表
        AVQuery<QH_AVObject> query = AVQuery.getQuery(QH_AVObject.class);
        // 按照更新时间降序排序
        query.orderByDescending("updatedAt");
        // 最大返回10条
        query.limit(10);
        try {
            return query.find();
        } catch (AVException exception) {
            QH_Log.e("Query QH_AVObject failed."+exception.getMessage());
            return Collections.emptyList();
        }
    }

    public static void searchQuery(String inputSearch) {
        AVSearchQuery searchQuery = new AVSearchQuery(inputSearch);
        searchQuery.search();
    }
}
