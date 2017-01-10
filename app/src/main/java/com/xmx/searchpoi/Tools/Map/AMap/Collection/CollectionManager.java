package com.xmx.searchpoi.Tools.Map.AMap.Collection;

import com.xmx.searchpoi.Tools.Data.Cloud.BaseCloudEntityManager;
import com.xmx.searchpoi.Tools.Map.AMap.POI.POI;

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
}
