package com.xmx.searchpoi.Tools.Map.AMap.Collection;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
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
public class AddCollectionDialog extends Dialog {

    private LatLng position;
    private int privacy = Constants.PRIVACY_PRIVATE;
    private AddCollectionCallback callback;

    public AddCollectionDialog(Context context, LatLng latLng, AddCollectionCallback callback) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        position = latLng;
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_collection);

        RadioGroup privacyGroup = (RadioGroup) findViewById(R.id.rg_privacy);
        privacyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_private:
                        privacy = Constants.PRIVACY_PRIVATE;
                        break;
                    case R.id.rb_public:
                        privacy = Constants.PRIVACY_PUBLIC;
                        break;
                    default:
                        privacy = Constants.PRIVACY_PRIVATE;
                        break;
                }
            }
        });

        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText titleEdit = (EditText) findViewById(R.id.edit_title);
                String title =  titleEdit.getText().toString();
                if (title.equals("")) {
                    showToast("标题不能为空");
                    return;
                }

                EditText typeEdit = (EditText) findViewById(R.id.edit_type);
                String type =  typeEdit.getText().toString();
                if (type.equals("")) {
                    showToast("类型不能为空");
                    return;
                }

                EditText contentEdit = (EditText) findViewById(R.id.edit_content);
                String content =  contentEdit.getText().toString();

                final Collection c = new Collection(UUID.randomUUID().toString(),
                        new LatLonPoint(position.latitude, position.longitude),
                        title, type, content, privacy);

                CollectionManager.getInstance().addCollection(c, new InsertCallback() {
                    @Override
                    public void success(AVObject user, String objectId) {
                        dismiss();
                        callback.success(c);
                    }

                    @Override
                    public void syncError(int error) {
                        CollectionManager.defaultError(error, getContext());
                    }

                    @Override
                    public void syncError(AVException e) {
                        showToast(R.string.add_failure);
                        filterException(e);
                    }
                });
            }
        });

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    protected void showToast(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String content) {
        Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
    }
}
