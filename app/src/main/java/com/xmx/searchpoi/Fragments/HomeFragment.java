package com.xmx.searchpoi.Fragments;

import android.os.Bundle;
import android.view.View;

import com.xmx.searchpoi.Map.MapActivity;
import com.xmx.searchpoi.R;
import com.xmx.searchpoi.Tools.FragmentBase.xUtilsFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

@ContentView(R.layout.fragment_home)
public class HomeFragment extends xUtilsFragment {

    @Event(value = R.id.btn_map)
    private void onClickAMapPOITest(View view) {
        startActivity(MapActivity.class);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

}
