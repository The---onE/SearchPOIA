package com.xmx.searchpoi.Tools.Data.Sync;

import com.xmx.searchpoi.Tools.Data.Cloud.ICloudEntity;
import com.xmx.searchpoi.Tools.Data.SQL.ISQLEntity;

/**
 * Created by The_onE on 2016/5/29.
 */
public interface ISyncEntity extends ICloudEntity, ISQLEntity {
    String getCloudId();

    void setCloudId(String id);
}
