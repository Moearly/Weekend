package com.martn.weekend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatus.Builder;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.utils.DistanceUtil;
import com.martn.weekend.adapter.ActMapAdapter;
import com.martn.weekend.adapter.ActsPagerAdapter;
import com.martn.weekend.base.BaseActivity;
import com.martn.weekend.model.TcrModel;
import com.martn.weekend.request.IRecommendServlet;
import com.martn.weekend.result.FindCourseForScopeResult;
import com.martn.weekend.result.TagsResult;
import com.martn.weekend.utility.AnimateFirstDisplayListener;
import com.martn.weekend.view.CusTextView;
import com.martn.weekend.view.TagListPopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.qmusic.base.BaseApplication;
import com.qmusic.common.BEnvironment;
import com.qmusic.uitls.AppUtils;
import com.qmusic.uitls.Helper;
import com.socks.library.KLog;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("地图位置")
 * Date 2014/10/5 0:00
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class ActMapActivity extends BaseActivity {

    private final String SEARCH_INDE = "search_model";
    private final String SEARCH_MODEL = "search_index";


    @Bind(R.id.tv_search)
    CusTextView tvSearch;

    @Bind(R.id.map_view)
    MapView mapView;
    @Bind(R.id.tv_pre)
    TextView tvPre;
    @Bind(R.id.tv_next)
    TextView tvNext;
    @Bind(R.id.iv_drag)
    ImageView ivDrag;
    @Bind(R.id.vp_acts)
    ViewPager vpActs;
    @Bind(R.id.tv_info)
    TextView tvInfo;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;

    @Bind(R.id.tv_classily)
    TextView tvClassily;

    @Bind(R.id.ll_page)
    View llPage;

    private FindCourseForScopeResult result;
    private Bitmap defaultLocationBit;
    private double nowx;
    private double nowy;
    private double centerLatitude;
    private double centerLongitude;
    private double lastLatitude;
    private double lastLongitude;
    private BaiduMap bmap;
    private List<TcrModel> list;
    private boolean isMove;
    private InfoWindow infoWindow;
    private TagListPopupWindow tagListDialog;
    private int tagId;
    private TagsResult tagsResult;
    private boolean upFlag = false; //向上刷新的标志
    private boolean downFlag = false;

    private double lastScopeRange;

    private int pageIndex = 1;

    private boolean isDownloadDone = false;
    private int downloadSize;
    private int downloadSizeTemp;
    private int lastPosition;
    private String searchName;

    private Response.Listener<JSONObject> findCourseForScopeListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            KLog.json(response.toString());
            result = new FindCourseForScopeResult();
            result.parse(response);
            list.clear();
            int selectedMarker = 0;
            if (result.success) {
                list = result.tcrArr;
                isDownloadDone = false;
                downloadSize = 0;
                downloadSizeTemp = 0;
                if (upFlag) {
                    pageIndex = pageIndex + 1;
                } else if (downFlag) {
                    pageIndex = pageIndex - 1;
                }
                if (result.scopeRange > getScopeRange() + 100.0d && bmap.getMapStatus().zoom > 12.0f) {
                    float zoomTmp = bmap.getMapStatus().zoom - (((float) result.scopeRange) / ((float) getScopeRange()));
                    KLog.e("MapStatus : " + zoomTmp + " | " + bmap.getMapStatus().zoom + " | " + result.scopeRange + " | " + getScopeRange() + " | ");
                    setMapZoomLevel(zoomTmp);
                }
                lastPosition = 0;
            } else {
                selectedMarker = -1;
                Helper.showToast(result.description);
            }
            upFlag = false;
            downFlag = false;
            adapter.setList(list);
            initViewPager();
            initAllMarker();
            initPageUpAndDownTV();
            setSelectedMarker(selectedMarker, false);
            if (list.isEmpty()) {
                ivDrag.setVisibility(View.INVISIBLE);
                findViewById(R.id.act_info_layout).setVisibility(View.INVISIBLE);
                tvInfo.setVisibility(View.VISIBLE);
                slidingLayout.setEnabled(false);
            } else {
                ivDrag.setVisibility(View.VISIBLE);
                findViewById(R.id.act_info_layout).setVisibility(View.INVISIBLE);
                tvInfo.setVisibility(View.INVISIBLE);
                slidingLayout.setEnabled(true);
            }
            dismissLoading();
        }

    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Helper.showToast("服务器异常，请稍后再试");
            dismissLoading();
        }
    };
    private ActMapAdapter adapter;
    private ActsPagerAdapter pagerAdapter;


    public static void comeBady(Context context) {
        context.startActivity(new Intent(context, ActMapActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_map);
        ButterKnife.bind(this);
        result = new FindCourseForScopeResult();
        defaultLocationBit = BitmapFactory.decodeResource(getResources(), R.drawable.img_default_location_icon);
        initView();
        bmap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                nowx = Double.parseDouble(AppUtils.getLatitude());
                nowy = Double.parseDouble(AppUtils.getLongitude());
                centerLatitude = Double.parseDouble(AppUtils.getLatitude());
                centerLongitude = Double.parseDouble(AppUtils.getLongitude());
                lastLatitude = centerLatitude;
                lastLongitude = centerLongitude;
                Builder mapBuilder = new Builder();
                mapBuilder.target(new LatLng(centerLatitude, centerLongitude));
                mapBuilder.zoom(15.0f);
                bmap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapBuilder.build()));
                mapView.setScaleControlPosition(new Point(10, 10));
                initAllMarker();
                getCourseForMap(tagId, centerLatitude, centerLongitude);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.bmap.clear();
        this.bmap = null;
        this.mapView.onDestroy();
        this.mapView = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SearchMapActivity.REQUEST_CODE && resultCode == -1) {
            PoiInfo poiInfo = data.getParcelableExtra(SearchMapActivity.REQUEST_POIINFO);
            centerLatitude = poiInfo.location.latitude;
            centerLongitude = poiInfo.location.longitude;
            tagId = 0;
            tvClassily.setText(tagsResult.getTagName(tagId));
            //searchName---
            searchName = data.getStringExtra("searchName");
            moveMap(this.centerLatitude, this.centerLongitude);
        }
    }





    private void initAllMarker() {
        bmap.clear();
        initCenterMarker();
        initOtherMarker();

    }

    /**
     * 初始化其他的覆盖物
     */
    private void initOtherMarker() {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                BitmapDescriptor bitmap;
                int index = i;
                LatLng point = new LatLng(list.get(index).coursePlaceX, list.get(index).coursePlaceY);
                if (ImageLoader.getInstance().getDiskCache()
                        .get(BEnvironment.SERVER_IMG_URL +  list.get(index).courseFenleiPhoto) == null) {
                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.img_default_location_icon);
                    if (!TextUtils.isEmpty( list.get(index).courseFenleiPhoto)) {
                        downloadSize++;
                    }
                } else {
                    bitmap = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(ImageLoader.getInstance().getDiskCache().get(BEnvironment.SERVER_IMG_URL + ((TcrModel) list.get(index)).courseFenleiPhoto).getPath()), defaultLocationBit.getWidth(), defaultLocationBit.getHeight(), true));
                }
                addOverlay(point, bitmap, index, list.get(i));
            }
            setSelectedMarker(lastPosition, false);
            if (downloadSize > 0 && !isDownloadDone) {
                downImage(downloadSize);
            }
        }

    }


    private void downImage(final int size) {
        int i = 0;
        while (i < list.size()) {
            if (!TextUtils.isEmpty(list.get(i).courseFenleiPhoto)
                    && ImageLoader.getInstance().getDiskCache()
                    .get(BEnvironment.SERVER_IMG_URL + list.get(i).courseFenleiPhoto) == null) {
                ImageLoader.getInstance()
                        .loadImage(BEnvironment.SERVER_IMG_URL + list.get(i).courseFenleiPhoto,
                                AnimateFirstDisplayListener.getOptions(),
                                new ImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String s, View view) {

                                    }

                                    @Override
                                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                                        //下载--缓存--数量增加
                                        downloadSizeTemp++;
                                        if (downloadSizeTemp == size) {
                                            //图片下载完成
                                            isDownloadDone = true;
                                            //初始化所有的覆盖物展示
                                            initAllMarker();
                                        }

                                    }

                                    @Override
                                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                        //下载--缓存--数量增加
                                        downloadSizeTemp++;
                                        if (downloadSizeTemp == size) {
                                            //图片下载完成
                                            isDownloadDone = true;
                                            //初始化所有的覆盖物展示
                                            initAllMarker();
                                        }

                                    }

                                    @Override
                                    public void onLoadingCancelled(String s, View view) {
                                        //下载--缓存--数量增加
                                        downloadSizeTemp++;
                                        if (downloadSizeTemp == size) {
                                            //图片下载完成
                                            isDownloadDone = true;
                                            //初始化所有的覆盖物展示
                                            initAllMarker();
                                        }

                                    }
                                });
            }
            i++;
        }
    }


    private void initCenterMarker() {
        addOverlay(new LatLng(nowx, nowy),
                BitmapDescriptorFactory.fromResource(R.drawable.img_my_location_icon), 999, null);
    }

    private void addOverlay(LatLng point, BitmapDescriptor bitmap, int index, TcrModel model) {
        Bundle b = new Bundle();
        b.putSerializable("search_model", model);
        b.putInt("search_index", index);
        bmap.addOverlay(new MarkerOptions().animateType(MarkerOptions.MarkerAnimateType.grow).position(point).icon(bitmap).extraInfo(b));
    }


    private void initView() {
        initclassily();
        initMapView();
        initBottomPLayout();
        initBottomActsVPAndList();
        initViewPager();
        vpActs.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                isMove = true;
                setSelectedMarker(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    /**
     * 初始化地图
     */
    private void initMapView() {
        new BaiduMapOptions().compassEnabled(false);
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.showZoomControls(false);
        mapView.showScaleControl(true);
        mapView.setLogoPosition(LogoPosition.logoPostionRightTop);
        bmap = mapView.getMap();
        bmap.setMaxAndMinZoomLevel(21.0f, 12.0f);
        bmap.getUiSettings().setCompassEnabled(false);
        setMapZoomLevel(15.0f);
        bmap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int index = marker.getExtraInfo().getInt("search_index");
                if (index >= 0) {
                    vpActs.setCurrentItem(index);
                }
                return false;
            }
        });
        bmap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                double nowLatitude = mapStatus.bound.getCenter().latitude;
                double nowLongitude = mapStatus.bound.getCenter().longitude;
                DistanceUtil distanceUtil = new DistanceUtil();
                double distance = DistanceUtil.getDistance(new LatLng(lastLatitude, lastLongitude), new LatLng(nowLatitude, nowLongitude));
                if (mapStatus.zoom < 12.0f) {
                    setMapZoomLevel(12.0f);
                    return;
                }
                if (getZoomDis() >= 500.0f && distance > ((double) getZoomDis()) && !isMove) {
                    centerLatitude = nowLatitude;
                    centerLongitude = nowLongitude;
                    lastLatitude = nowLatitude;
                    lastLongitude = nowLongitude;
                    initSearch(searchName);
                    getCourseForMap(tagId, mapStatus.target.latitude, mapStatus.target.longitude);
                }
                isMove = false;
            }
        });
    }

    private void initclassily() {
        try {
//            String tags = Utils.getTags();
            String tags = "";
            if (!TextUtils.isEmpty(tags)) {
                tvClassily.setVisibility(View.VISIBLE);
                tagListDialog = new TagListPopupWindow(this, new TagListPopupWindow.TagListDialogCallback() {
                    @Override
                    public void click(int id) {
                        tagId = id;
                        tvClassily.setText(tagsResult.getTagName(tagId));
                        getCourseForMap(tagId, centerLatitude, centerLongitude);
                    }

                    @Override
                    public void dismiss() {
                        tvClassily.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_arrow_down_white, 0);
                    }
                });
                tagsResult = new TagsResult(new JSONObject(tags));
                tagListDialog.setTagsResult(tagsResult);
            }
        } catch (JSONException e) {
        }
    }


    private synchronized void setSelectedMarker(int position, boolean isMove) {
        if (!(list == null || list.isEmpty())) {
            View view = View.inflate(this, R.layout.item_location, null);
            ((TextView) view.findViewById(R.id.tv_info)).setText(list.get(position).title);
            infoWindow = new InfoWindow(view, new LatLng(list.get(position).coursePlaceX, list.get(position).coursePlaceY), (int) (((double) (-AppUtils.getScreenHeight())) * 0.04d));
            bmap.showInfoWindow(infoWindow);
            if (isMove) {
                moveMap(list.get(position).coursePlaceX, list.get(position).coursePlaceY);
            }
        }
        List<Marker> list = bmap.getMarkersInBounds(bmap.getMapStatus().bound);
        if (!(list == null || list.isEmpty())) {
            for (int i = 0; i < list.size(); i++) {
                if (position == list.get(i).getExtraInfo().getInt("search_index")) {
                    list.get(i).setToTop();
                }
            }
        }
    }

    private void moveMap(double latitude, double longitude) {
        bmap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new Builder().target(new LatLng(latitude, longitude)).build()));
    }

    /**
     * 网络获取周围信息
     *
     * @param tagId
     * @param latitude
     * @param longitude
     */
    private void getCourseForMap(int tagId, double latitude, double longitude) {
        showLoading();
        int pageIndexTmp = pageIndex;
        if (upFlag) {
            pageIndexTmp++;
        } else if (downFlag) {
            pageIndexTmp--;
        } else {
            pageIndex = 1;
            pageIndexTmp = 1;
            lastScopeRange = getScopeRange();
        }
        if (this.upFlag || this.downFlag) {
            IRecommendServlet.findCourseForScope(tagId, pageIndexTmp,
                    lastScopeRange, getMaxScopeRange(),
                    this.nowx, this.nowy,
                    this.result.scopex, this.result.scopey,
                    findCourseForScopeListener, errorListener);
            return;
        }
        double d = latitude;
        double d2 = longitude;
        IRecommendServlet.findCourseForScope(tagId, pageIndexTmp, getScopeRange(), getMaxScopeRange(), this.nowx, this.nowy, d, d2, this.findCourseForScopeListener, this.errorListener);
    }

    /**
     * 获取范围
     *
     * @return
     */
    private double getScopeRange() {
        LatLng disLatLn = new LatLng(bmap.getMapStatus().bound.getCenter().latitude, bmap.getMapStatus().bound.southwest.longitude);
        DistanceUtil distanceUtil = new DistanceUtil();
        return DistanceUtil.getDistance(bmap.getMapStatus().bound.getCenter(), disLatLn);
    }


    /**
     * 获取最大的范围
     *
     * @return
     */
    private double getMaxScopeRange() {
        if (bmap.getMapStatus().zoom == bmap.getMinZoomLevel()) {
            return getScopeRange();
        }
        return 0.0d;
    }

    /**
     * 设置地图的缩放级别
     * @param zoom
     */
    private void setMapZoomLevel(float zoom) {
        Builder mapBuilder = new Builder();
        mapBuilder.zoom(zoom);
        bmap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapBuilder.build()));
    }

    private float getZoom(float zoomM) {
        float zoom = bmap.getMapStatus().zoom;
        if (zoomM < 5000.0f && zoomM >= 2000.0f) {
            return 13.0f - ((zoom - 2000.0f) / 2000.0f);
        }
        if (zoomM < 2000.0f && zoomM >= 1000.0f) {
            return 14.0f - ((zoom - 1000.0f) / 1000.0f);
        }
        if (zoomM >= 1000.0f || zoomM < 500.0f) {
            return 12.0f;
        }
        return 15.0f - ((zoom - 500.0f) / 500.0f);
    }

    private float getZoomDis() {
        switch ((int) this.bmap.getMapStatus().zoom) {
            case 12 /*12*/:
                return 5000.0f;
            case 13 /*13*/:
                return 2000.0f;
            case 14 /*14*/:
                return 1000.0f;
            case 15 /*15*/:
                return 500.0f;
            default:
                return 0.0f;
        }
    }


    private void initSearch(String searchName) {
        if (TextUtils.isEmpty(searchName)) {
            tvSearch.setText("搜索地点");
        } else {
            tvSearch.setText(searchName);
        }
        if (!TextUtils.isEmpty(searchName)) {
            this.searchName = "";
        }
    }

    /**
     * 初始化底部的===拉出区域
     */
    private void initBottomPLayout() {
        slidingLayout.setCoveredFadeColor(0);
        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    ivDrag.setImageResource(R.drawable.img_map_up);
                } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    ivDrag.setImageResource(R.drawable.img_map_down);
                }
            }
        });
    }

    private void initBottomActsVPAndList() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                setSelectedMarker(position, false);
                vpActs.setCurrentItem(position, false);
                ActDetailActivity.comeBady(ActMapActivity.this, ((TcrModel) ActMapActivity.this.list.get(position)).courseid);
            }
        });
        list = new ArrayList();
        adapter = new ActMapAdapter(this, this.list);
        listview.setAdapter(this.adapter);
    }

    private void initViewPager() {
        pagerAdapter = new ActsPagerAdapter(this, this.list);
        vpActs.setAdapter(pagerAdapter);
    }

    private void initPageUpAndDownTV() {
        int i = R.color.main_text_gray;
        if (result.pageCount > 1) {
            llPage.setVisibility(View.VISIBLE);
            tvPre.setTextColor(getResources().getColor(pageIndex <= 1 ? R.color.main_text_gray : R.color.blue));
            if (pageIndex < this.result.pageCount) {
                i = R.color.blue;
            }
            tvNext.setTextColor(BaseApplication.resources().getColor(i));
            return;
        }
        llPage.setVisibility(View.GONE);
    }



}
