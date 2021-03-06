package com.xmx.searchpoi.Tools.Data.Callback;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.xmx.searchpoi.Tools.Data.Cloud.ICloudEntity;

import java.util.List;

/**
 * Created by The_onE on 2016/5/29.
 */
public abstract class SelectLoginCallback<Entity extends ICloudEntity> {

    public abstract void success(AVObject user, List<Entity> entities);

    public abstract void syncError(int error);

    public abstract void syncError(AVException e);
}
