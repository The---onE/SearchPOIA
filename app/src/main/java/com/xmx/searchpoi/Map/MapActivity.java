package com.xmx.searchpoi.Map;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.avos.avoscloud.AVException;
import com.xmx.searchpoi.R;
import com.xmx.searchpoi.Tools.Data.Callback.SelectCallback;
import com.xmx.searchpoi.Tools.Map.AMap.Activity.BaseLocationDirectionActivity;
import com.xmx.searchpoi.Tools.Map.AMap.Collection.AddCollectionCallback;
import com.xmx.searchpoi.Tools.Map.AMap.Collection.AddCollectionDialog;
import com.xmx.searchpoi.Tools.Map.AMap.Collection.Collection;
import com.xmx.searchpoi.Tools.Map.AMap.Collection.CollectionDetailDialog;
import com.xmx.searchpoi.Tools.Map.AMap.Collection.CollectionView;
import com.xmx.searchpoi.Tools.Map.AMap.Collection.CollectionManager;
import com.xmx.searchpoi.Tools.Map.AMap.Utils.AMapServicesUtil;
import com.xmx.searchpoi.Tools.Map.AMap.Utils.AMapUtil;
import com.xmx.searchpoi.Tools.Map.AMap.Utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.UUID;

@ContentView(R.layout.activity_map)
public class MapActivity extends BaseLocationDirectionActivity {

    private Marker currentMarker;
    private LatLng currentLatLng;
    private Marker currentCollect;

    private CollectionView collectionView;

    @ViewInject(R.id.input_edittext)
    private EditText mSearchText;

    @ViewInject(R.id.btn_collect)
    private Button collectButton;

    @ViewInject(R.id.btn_collect_cancel)
    private Button cancelCollectButton;

    @Event(R.id.btn_location)
    private void onLocationClick(View view) {
        focusLocation();
    }

    @Event(R.id.btn_search)
    private void onSearchClick(View view) {
        String keyword = mSearchText.getText().toString().trim();
        LatLng position = mLocation;
        if (currentLatLng != null) {
            position = currentLatLng;
        }

        collectionView.clearCollection();

        CollectionManager.getInstance().selectPublic(keyword, keyword,
                new SelectCallback<Collection>() {
            @Override
            public void success(List<Collection> poiList) {
                for (Collection poi : poiList) {
                    collectionView.addCollection(poi);
                }
            }

            @Override
            public void syncError(int error) {
                CollectionManager.defaultError(error, getBaseContext());
            }

            @Override
            public void syncError(AVException e) {
                showToast(R.string.get_failure);
                filterException(e);
            }
        });

        CollectionManager.getInstance().selectPrivate(keyword, keyword,
                new SelectCallback<Collection>() {
                    @Override
                    public void success(List<Collection> poiList) {
                        if (poiList.size() > 0) {
                            for (Collection poi : poiList) {
                                collectionView.addCollection(poi);
                            }
                        }
                        showToast(R.string.get_success);
                    }

                    @Override
                    public void syncError(int error) {
                        CollectionManager.defaultError(error, getBaseContext());
                    }

                    @Override
                    public void syncError(AVException e) {
                        showToast(R.string.get_failure);
                        filterException(e);
                    }
                });
    }

    @Event(value = R.id.btn_search, type = View.OnLongClickListener.class)
    private boolean onSearchLongClick(View view) {
        String keyword = mSearchText.getText().toString().trim();
        String[] info = keyword.split(" ");
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);

        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (i == 1000) {
                    if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null
                            && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                        String address = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                        showToast("地址:" + address);
//                        List<PoiItem> list = regeocodeResult.getRegeocodeAddress().getPois();
//                        if (list != null) {
//                            List<POI> poiList = POIManager.convertPOIList(list);
//                            //清除POI信息显示
//                            whetherToShowDetailInfo(false);
//                            focusPOISearch();
//                            poiView.showPOI(poiList);
//                        }
                    } else {
                        showToast(R.string.no_result);
                    }
                } else {
                    ToastUtil.showError(getBaseContext(), i);
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                if (i == 1000) {
                    if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null
                            && geocodeResult.getGeocodeAddressList().size() > 0) {
                        GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
                        LatLonPoint position = address.getLatLonPoint();
                        LatLng latLng = AMapUtil.convertToLatLng(position);

                        whetherToShowDetailInfo(false);
//                        poiView.resetMarker();
                        setCurrentPosition(latLng);
                        focusLocation(latLng);
                    } else {
                        showToast(R.string.no_result);
                    }
                } else {
                    ToastUtil.showError(getBaseContext(), i);
                }
            }
        });
        if (keyword.equals("") || info.length < 2) {
            LatLng position = mLocation;
            if (currentLatLng != null) {
                position = currentLatLng;
            }

            LatLonPoint point = AMapServicesUtil.convertToLatLonPoint(position);
            RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);
            geocodeSearch.getFromLocationAsyn(query);
        } else {
            GeocodeQuery query = new GeocodeQuery(info[1], info[0]);
            geocodeSearch.getFromLocationNameAsyn(query);
        }

        return true;
    }

    @Event(R.id.btn_cancel)
    private void onCancelClick(View view) {
        mSearchText.setText("");
//        poiView.removeFromMap();
        whetherToShowDetailInfo(false);

        collectionView.clearCollection();
        CollectionManager.getInstance().selectPublic("", "", new SelectCallback<Collection>() {
            @Override
            public void success(List<Collection> poiList) {
                for (Collection poi : poiList) {
                    collectionView.addCollection(poi);
                }
            }

            @Override
            public void syncError(int error) {
                CollectionManager.defaultError(error, getBaseContext());
            }

            @Override
            public void syncError(AVException e) {
                showToast(R.string.get_failure);
                filterException(e);
            }
        });
    }

    @Event(R.id.btn_collect)
    private void onCollectClick(View view) {
        if (currentMarker == null || currentLatLng == null) {
            return;
        }
        if (currentCollect != null) {
            showToast("已添加收藏");
            //删除收藏
        } else {
            AddCollectionDialog dialog = new AddCollectionDialog(MapActivity.this, currentLatLng,
                    new AddCollectionCallback() {
                        @Override
                        public void success(Collection collection) {
                            showToast("收藏成功");
                            collectionView.addCollection(collection);
                        }
                    });
            dialog.show();
        }
    }

    @Event(R.id.btn_collect_cancel)
    private void onCancelCollectClick(View view) {
        if (currentMarker != null) {
            currentMarker.remove();
            currentMarker = null;
            currentLatLng = null;
        }
        cancelCollectButton.setVisibility(View.GONE);
        collectButton.setVisibility(View.GONE);
    }

    @Override
    protected void getMapView() {
        mMapView = getViewById(R.id.map);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        collectionView = new CollectionView(this, mAMap);

        strokeColor = Color.argb(180, 3, 145, 255);
        fillColor = Color.argb(64, 128, 192, 192);
        markerFlag = "myLocation";
        //aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
    }

    @Override
    protected void setListener() {
        mAMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                whetherToShowDetailInfo(false);
//                poiView.resetMarker();

                setCurrentPosition(latLng);
            }
        });
        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                setCurrentPosition(marker.getPosition());
                Object o = marker.getObject();
                if (o != null) {
                    if (collectionView.isCollect(marker)) {
                        Collection collection = (Collection) o;
                        CollectionDetailDialog dialog
                                = new CollectionDetailDialog(MapActivity.this, collection);
                        dialog.show();
                        currentCollect = marker;
                        return true;
                    }
                    whetherToShowDetailInfo(true);
                } else {
                    whetherToShowDetailInfo(false);
//                    poiView.resetMarker();
                }
                return true;
            }
        });
//        mAMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//
//            }
//        });
//
//        mAMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                return null;
//            }
//        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        super.processLogic(savedInstanceState);

        mAMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);//设置定位的类型为定位模式

        CollectionManager.getInstance().selectPublic("", "", new SelectCallback<Collection>() {
            @Override
            public void success(List<Collection> poiList) {
                for (Collection poi : poiList) {
                    collectionView.addCollection(poi);
                }
            }

            @Override
            public void syncError(int error) {
                CollectionManager.defaultError(error, getBaseContext());
            }

            @Override
            public void syncError(AVException e) {
                showToast(R.string.get_failure);
                filterException(e);
            }
        });
    }

//    private void setPoiItemDisplayContent(final POI mCurrentPoi) {
//        mPoiName.setText(mCurrentPoi.getTitle());
//        mPoiAddress.setText(mCurrentPoi.getSnippet());
//    }

    private void whetherToShowDetailInfo(boolean isToShow) {
//        if (isToShow) {
//            mPoiDetail.setVisibility(View.VISIBLE);
//        } else {
//            mPoiDetail.setVisibility(View.GONE);
//        }
    }

    private void setCurrentPosition(LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.remove();
            currentMarker = null;
        }
        MarkerOptions m = new MarkerOptions()
                .position(
                        new LatLng(latLng.latitude, latLng.longitude))
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                getResources(),
                                R.drawable.selected)))
                .anchor(0.5f, 1f);
        currentMarker = mAMap.addMarker(m);
        currentLatLng = latLng;

        currentCollect = null;

        collectButton.setVisibility(View.VISIBLE);
        cancelCollectButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setLocationOption(AMapLocationClientOption locationOption) {
        //设置为高精度定位模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    }

    @Override
    protected void whenFirstLocation(AMapLocation aMapLocation) {
        focusLocation();
    }

    @Override
    protected void whenNewLocation(AMapLocation aMapLocation) {
        //mListener.onLocationChanged(aMapLocation); //系统默认定位事件
    }

    @Override
    protected void whenLocationError(int errorCode, String errorInfo) {
        String errText = "定位失败," + errorCode + ": " + errorInfo;
        showToast(errText);
        showLog("AMapErr", errText);
    }

    private void focusPOISearch() {
        if (currentLatLng != null) {
            focusLocation(currentLatLng, 14);
        } else {
            focusLocation(14);
        }
    }

    @Override
    protected void whenResume() {
    }

    @Override
    protected void whenPause() {
    }

    @Override
    protected void whenDestroy() {
    }
}
