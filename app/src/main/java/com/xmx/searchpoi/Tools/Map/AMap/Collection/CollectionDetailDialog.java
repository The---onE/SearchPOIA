package com.xmx.searchpoi.Tools.Map.AMap.Collection;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.xmx.searchpoi.Constants;
import com.xmx.searchpoi.R;
import com.xmx.searchpoi.Tools.Data.Callback.InsertCallback;

import java.util.UUID;

import static com.xmx.searchpoi.Tools.Utils.ExceptionUtil.filterException;

/**
 * Created by The_onE on 2016/9/21.
 */
public class CollectionDetailDialog extends Dialog {

    Collection collection;

    public CollectionDetailDialog(Context context, Collection collection) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        this.collection = collection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_collection_detail);

        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(collection.getTitle());

        TextView typeView = (TextView) findViewById(R.id.tv_type);
        typeView.setText(collection.mType);

        TextView contentView = (TextView) findViewById(R.id.tv_content);
        contentView.setText(collection.getSnippet());

        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
