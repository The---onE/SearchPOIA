package com.xmx.searchpoi.Tools.Map.AMap.Collection;

import android.content.ContentValues;
import android.database.Cursor;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.avos.avoscloud.AVObject;
import com.xmx.searchpoi.Tools.Data.Cloud.ICloudEntity;
import com.xmx.searchpoi.Tools.Data.SQL.ISQLEntity;
import com.xmx.searchpoi.Tools.Data.Sync.ISyncEntity;

import java.util.Date;

/**
 * Created by The_onE on 2016/12/1.
 */

public class Collection extends PoiItem implements ISQLEntity, ICloudEntity, ISyncEntity {

    public long mId = -1;
    public String mCloudId = null;
    public String mType;
    public int mPrivacy;
    public Date mTime;

    public Collection(String id,
                      LatLonPoint point,
                      String title,
                      String type,
                      String content,
                      int privacy) {
        super(id, point, title, content);
        mType = type;
        mPrivacy = privacy;
        mTime = new Date();
    }

    public Collection(PoiItem o) {
        super(o.getPoiId(), o.getLatLonPoint(), o.getTitle(), o.getSnippet());
        mTime = new Date();
    }

    @Override
    public String tableFields() {
        return "ID integer not null primary key autoincrement, " + //0
                "CLOUD_ID text, " + //1
                "PoiId text, " + //2
                "Latitude real not null, " + //3
                "Longitude real not null, " + //4
                "Title text, " + //5
                "Type text, " + //6
                "Content text, " + //7
                "Privacy integer not null default(1), " + //8
                "Time integer not null default(0)"; //9
    }

    @Override
    public ContentValues getContent() {
        ContentValues content = new ContentValues();
        if (mId > 0) {
            content.put("ID", mId);
        }
        if (mCloudId != null) {
            content.put("CLOUD_ID", mCloudId);
        }
        content.put("PoiId", getPoiId());
        content.put("Latitude", getLatLonPoint().getLatitude());
        content.put("Longitude", getLatLonPoint().getLongitude());
        content.put("Title", getTitle());
        content.put("Type", mType);
        content.put("Content", getSnippet());
        content.put("Privacy", mPrivacy);
        content.put("Time", mTime.getTime());
        return content;
    }

    @Override
    public Collection convertToEntity(Cursor c) {
        long id = c.getLong(0);
        String cloudId = c.getString(1);
        String PoiId = c.getString(2);
        double latitude = c.getDouble(3);
        double longitude = c.getDouble(4);
        String title = c.getString(5);
        String type = c.getString(6);
        String content = c.getString(7);
        int privacy = c.getInt(8);
        Date time = new Date(c.getLong(9));

        Collection entity = new Collection(PoiId, new LatLonPoint(latitude, longitude), title,
                type, content, privacy);
        entity.mId = id;
        entity.mCloudId = cloudId;
        entity.mTime = time;

        return entity;
    }

    @Override
    public AVObject getContent(String tableName) {
        AVObject object = new AVObject(tableName);
        if (mCloudId != null) {
            object.setObjectId(mCloudId);
        }
        if (mId > 0) {
            object.put("id", mId);
        }
        object.put("poiId", getPoiId());
        object.put("latitude", getLatLonPoint().getLatitude());
        object.put("longitude", getLatLonPoint().getLongitude());
        object.put("title", getTitle());
        object.put("type", mType);
        object.put("content", getSnippet());
        object.put("privacy", mPrivacy);
        object.put("time", mTime);

        return object;
    }

    @Override
    public Collection convertToEntity(AVObject object) {
        long id = object.getLong("id");
        String cloudId = object.getObjectId();
        String PoiId = object.getString("poiId");
        double latitude = object.getDouble("latitude");
        double longitude = object.getDouble("longitude");
        String title = object.getString("title");
        String type = object.getString("type");
        String content = object.getString("content");
        int privacy = object.getInt("privacy");
        Date time = object.getDate("time");

        Collection entity = new Collection(PoiId, new LatLonPoint(latitude, longitude), title,
                type, content, privacy);
        entity.mId = id;
        entity.mCloudId = cloudId;
        entity.mTime = time;

        return entity;
    }

    @Override
    public String getCloudId() {
        return mCloudId;
    }

    @Override
    public void setCloudId(String id) {
        mCloudId = id;
    }
}
