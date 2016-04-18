package com.martn.weekend;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.martn.weekend.base.BaseActivity;
import com.qmusic.common.IAsyncDataCallback;
import com.qmusic.localplugin.BaiduMapPlug;
import com.qmusic.localplugin.PluginManager;
import com.baidu.mapapi.map.MapStatus.Builder;

import com.qmusic.uitls.Helper;
import com.socks.library.KLog;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("地图位置查看")
 * Date 2016/4/18 11:42
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class MapActivity extends BaseActivity implements View.OnClickListener, IAsyncDataCallback<BDLocation> {
    public static OnLocationSelectListener onLocationSelectListener;
    String address;
    BaiduMapPlug baiduMapPlug;
    BaiduMap mBaiduMap;
    MapView mMapView;
    Button mNavigationButton;
    SDKReceiver mReceiver;
    LatLng point;


    public interface OnLocationSelectListener {
        void onLocationSelect(String str, String str2, String str3);
    }

    class SDKReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                KLog.e("key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                KLog.e("网络出错");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();
        mMapView = (MapView) findViewById(R.id.activity_map_bmapView);
        mNavigationButton = (Button) findViewById(R.id.activity_map_navigation_button);

        mNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNavigation();
            }
        });

        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);

        mBaiduMap = mMapView.getMap();
        Bundle bundle = getIntent().getExtras();
        boolean location = false;
        BDLocation loc = ((BaiduMapPlug) PluginManager.getPlugin(BaiduMapPlug.class.getSimpleName())).getLocation();
        if (bundle != null) {
            address = bundle.getString("address", "");
            location = bundle.getBoolean("location", false);
            try {
                point = new LatLng(Double.parseDouble(bundle.getString("lat")), Double.parseDouble(bundle.getString("lng")));
            } catch (Exception e) {
                point = new LatLng(loc.getLatitude(), loc.getLongitude());
            }
        } else {
            point = new LatLng(loc.getLatitude(), loc.getLongitude());
        }
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        mBaiduMap.addOverlay(option);
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(new Builder().target(point).zoom(15.0f).build());
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mBaiduMap.setMyLocationEnabled(true);
        baiduMapPlug = (BaiduMapPlug) PluginManager.getPlugin(BaiduMapPlug.class.getSimpleName());
        if (location) {
            Helper.showToast("长按地图选择上课地点");
        } else {
            onLocationSelectListener = null;
        }
        if (location) {
            mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    point = latLng;
                    OverlayOptions option = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding));
                    mBaiduMap.clear();
                    mBaiduMap.addOverlay(option);
                }
            });
        }
    }

    private void initView() {
        findViewById();
    }

    private void findViewById() {
        ((TextView) findViewById(R.id.topbar_mid_textview)).setText(getIntent().getStringExtra("title"));
    }

    @Override
    public void callback(int result, BDLocation location) {
        if (result == 0 && location != null) {
            mBaiduMap.setMyLocationData(
                    new MyLocationData.Builder()
                            .accuracy(location.getRadius())
                            .direction(100.0f)
                            .latitude(location.getLatitude())
                            .longitude(location.getLongitude())
                            .build());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        if (baiduMapPlug != null) {
            baiduMapPlug.updateLocation(this, 600000, -1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        mBaiduMap.setMyLocationEnabled(false);
        if (baiduMapPlug != null) {
            baiduMapPlug.removeCallback(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topbar_left_textview:
                finish();
            default:
        }
    }

    @SuppressLint({"InflateParams"})
    private void saveLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = (EditText) ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.item_address_input, null);
        builder.setTitle("请输入地址");
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                String text = editText.getText().toString();
                if (text == null || text.trim().length() == 0) {
                    Helper.showToast("地址不能为空");
                    return;
                }
                address = text.trim();
                if (onLocationSelectListener != null) {
                    onLocationSelectListener.onLocationSelect(address, String.valueOf(point.latitude), String.valueOf(point.longitude));
                }
                finish();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.setCancelable(true);
        builder.create().show();
    }

    private void openNavigation() {
        BDLocation loc = ((BaiduMapPlug) PluginManager.getPlugin(BaiduMapPlug.class.getSimpleName())).getLocation();
        LatLng startPoint = new LatLng(loc.getLatitude(), loc.getLongitude());
        try {
            BaiduMapNavigation.openBaiduMapNavi(
                    new NaviParaOption()
                            .startPoint(startPoint)
                            .endPoint(new LatLng(point.latitude, point.longitude))
                            .startName("从这里开始")
                            .endName("到这里结束"), this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(activity);
            }
        });
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    public static void comeBady(Context context, String title, String lat, String lng) {
        Intent intent = new Intent();
        intent.setClass(context, MapActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        context.startActivity(intent);
    }
}