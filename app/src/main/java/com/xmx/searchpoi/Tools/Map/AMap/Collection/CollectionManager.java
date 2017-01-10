package com.xmx.searchpoi.Tools.Map.AMap.Collection;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.xmx.searchpoi.Constants;
import com.xmx.searchpoi.Tools.Data.Callback.SelectCallback;
import com.xmx.searchpoi.Tools.Data.Cloud.BaseCloudEntityManager;
import com.xmx.searchpoi.Tools.Data.DataConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by The_onE on 2016/12/1.
 */

public class CollectionManager extends BaseCloudEntityManager<Collection> {
    private static CollectionManager instance;

    public synchronized static CollectionManager getInstance() {
        if (null == instance) {
            instance = new CollectionManager();
        }
        return instance;
    }

    private CollectionManager() {
        tableName = "Collection";
        entityTemplate = new Collection(null, null, null, null, null, 0);
        //userField = "";
    }

    public void selectPublic(String title, String type,
                             final SelectCallback<Collection> callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        AVQuery<AVObject> publicType = new AVQuery<>(tableName);
        publicType.whereEqualTo("privacy", Constants.PRIVACY_PUBLIC);

        AVQuery<AVObject> titleLike = new AVQuery<>(tableName);
        titleLike.whereContains("title", title);

        AVQuery<AVObject> typeLike = new AVQuery<>(tableName);
        typeLike.whereContains("type", type);

        AVQuery<AVObject> publicCondition = AVQuery.or(Arrays.asList(titleLike, typeLike));
        AVQuery<AVObject> publicQuery = AVQuery.and(Arrays.asList(publicType, publicCondition));

        publicQuery.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    List<Collection> entities = new ArrayList<>();
                    for (AVObject object : avObjects) {
                        Collection entity = entityTemplate.convertToEntity(object);
                        entities.add(entity);
                    }
                    callback.success(entities);
                } else {
                    callback.syncError(e);
                }
            }
        });
    }

    public void selectPrivate(String title, String type,
                             final SelectCallback<Collection> callback) {
        if (!checkDatabase()) {
            callback.syncError(DataConstants.NOT_INIT);
            return;
        }
        AVQuery<AVObject> privateType = new AVQuery<>(tableName);
        privateType.whereEqualTo("privacy", Constants.PRIVACY_PRIVATE);

        AVQuery<AVObject> titleEqual = new AVQuery<>(tableName);
        titleEqual.whereEqualTo("title", title);

        AVQuery<AVObject> typeEqual = new AVQuery<>(tableName);
        typeEqual.whereEqualTo("type", type);

        AVQuery<AVObject> privateCondition = AVQuery.or(Arrays.asList(titleEqual, typeEqual));
        AVQuery<AVObject> privateQuery = AVQuery.and(Arrays.asList(privateType, privateCondition));

        privateQuery.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    List<Collection> entities = new ArrayList<>();
                    for (AVObject object : avObjects) {
                        Collection entity = entityTemplate.convertToEntity(object);
                        entities.add(entity);
                    }
                    callback.success(entities);
                } else {
                    callback.syncError(e);
                }
            }
        });
    }
}
