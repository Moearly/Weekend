package com.qmusic.localplugin;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.qmusic.base.BaseApplication;
import com.qmusic.common.IAsyncDataCallback;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

public class BaiduMapPlug extends BasePlug implements IAsyncDataCallback<BDLocation> {
    WeakHashMap<IAsyncDataCallback<BDLocation>, Integer> callbackList;
    Context ctx;
    long lastupdateTime;
    BDLocation location;
    LocationClient mLocationClient;
    MyLocationListener mMyLocationListener;

    /* renamed from: com.qmusic.localplugin.BaiduMapPlug.1 */
    class AnonymousClass1 implements Runnable {
        private final /* synthetic */ IAsyncDataCallback val$callback;

        AnonymousClass1(IAsyncDataCallback iAsyncDataCallback) {
            this.val$callback = iAsyncDataCallback;
        }

        public void run() {
            this.val$callback.callback(0, BaiduMapPlug.this.location);
        }
    }

    class MyLocationListener implements BDLocationListener {
        MyLocationListener() {
        }

        public void onReceiveLocation(BDLocation location) {
            BaiduMapPlug.this.location = location;
            BaiduMapPlug.this.lastupdateTime = System.currentTimeMillis();
            Set<IAsyncDataCallback<BDLocation>> cs = BaiduMapPlug.this.callbackList.keySet();
            ArrayList<IAsyncDataCallback<BDLocation>> removeList = new ArrayList();
            for (IAsyncDataCallback<BDLocation> c : cs) {
                try {
                    c.callback(0, location);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Integer repeat = (Integer) BaiduMapPlug.this.callbackList.get(c);
                if (repeat == null || repeat.intValue() == 0 || repeat.intValue() == 1) {
                    removeList.add(c);
                } else if (repeat.intValue() > 1) {
                    BaiduMapPlug.this.callbackList.put(c, Integer.valueOf(repeat.intValue() - 1));
                }
            }
            Iterator it = removeList.iterator();
            while (it.hasNext()) {
                BaiduMapPlug.this.callbackList.remove((IAsyncDataCallback) it.next());
            }
            if (BaiduMapPlug.this.callbackList.isEmpty()) {
                BaiduMapPlug.this.stop();
            }
        }
    }

    public void init(Context ctx) {
        this.ctx = ctx;
        this.mLocationClient = new LocationClient(ctx);
        this.mMyLocationListener = new MyLocationListener();
        this.mLocationClient.registerLocationListener(this.mMyLocationListener);
        this.callbackList = new WeakHashMap();
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(10000);
        option.setIsNeedAddress(false);
        this.mLocationClient.setLocOption(option);
        updateLocation(this, 1000, 1);
    }

    public void destory() {
        if (this.mLocationClient.isStarted()) {
            this.mLocationClient.stop();
        }
        this.mLocationClient.unRegisterLocationListener(this.mMyLocationListener);
    }

    public void callback(int result, BDLocation data) {
        if (result == 0 && data != null) {
            this.location = data;
        }
    }

    public BDLocation getLocation() {
        return this.location;
    }

    public void updateLocation(IAsyncDataCallback<BDLocation> callback, long millis, int repeat) {
        this.callbackList.put(callback, Integer.valueOf(repeat));
        if (System.currentTimeMillis() - this.lastupdateTime <= millis && this.location != null) {
            BaseApplication.post(new AnonymousClass1(callback));
        }
        start();
    }

    public void removeCallback(IAsyncDataCallback<BDLocation> callback) {
        this.callbackList.remove(callback);
        if (this.callbackList.isEmpty()) {
            stop();
        }
    }

    void start() {
        if (!this.mLocationClient.isStarted()) {
            this.mLocationClient.start();
        }
    }

    void stop() {
        if (this.mLocationClient.isStarted()) {
            this.mLocationClient.stop();
        }
    }

    public LocationClient getmLocationClient() {
        return this.mLocationClient;
    }

    public void setmLocationClient(LocationClient mLocationClient) {
        this.mLocationClient = mLocationClient;
    }
}