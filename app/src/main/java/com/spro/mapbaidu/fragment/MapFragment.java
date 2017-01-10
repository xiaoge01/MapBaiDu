package com.spro.mapbaidu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.spro.mapbaidu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/1/4.
 */

public class MapFragment extends Fragment {

    @BindView(R.id.iv_located)
    ImageView ivLocated;
    @BindView(R.id.btn_HideHere)
    Button btnHideHere;
    @BindView(R.id.centerLayout)
    RelativeLayout centerLayout;
    @BindView(R.id.iv_scaleUp)
    ImageView ivScaleUp;
    @BindView(R.id.iv_scaleDown)
    ImageView ivScaleDown;
    @BindView(R.id.tv_located)
    TextView tvLocated;
    @BindView(R.id.tv_satellite)
    TextView tvSatellite;
    @BindView(R.id.tv_compass)
    TextView tvCompass;
    @BindView(R.id.ll_locationBar)
    LinearLayout llLocationBar;
    @BindView(R.id.tv_currentLocation)
    TextView tvCurrentLocation;
    @BindView(R.id.iv_toTreasureInfo)
    ImageView ivToTreasureInfo;
    @BindView(R.id.et_treasureTitle)
    EditText etTreasureTitle;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.layout_bottom)
    FrameLayout layoutBottom;
    @BindView(R.id.map_frame)
    FrameLayout mapFrame;

    private BaiduMap map;
    private LocationClient locationClient;
    private LatLng latLng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //初始化百度地图
        initMapView();

        //设置定位
        initLocation();


    }


    /**
     * 初始化百度地图
     */
    private void initMapView() {

        // #4 初始化地图状态
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(16);// 3--21：默认的是12
        builder.overlook(0);// 俯仰的角度
        builder.rotate(0);// 旋转的角度
        // #3 构建状态
        MapStatus mapStatus = builder.build();//设置完成后构建

        // #2 设置百度地图的设置信息
        BaiduMapOptions baiduMapOptions = new BaiduMapOptions();
        baiduMapOptions.mapStatus(mapStatus);//需要设置地图的状态
        baiduMapOptions.compassEnabled(true);// 是否显示指南针
        baiduMapOptions.zoomGesturesEnabled(true);// 是否允许缩放手势
        baiduMapOptions.scaleControlEnabled(false);// 不显示比例尺
        baiduMapOptions.zoomControlsEnabled(false);// 不显示缩放的控件

        // #1 初始化百度地图,需要设置一些属性
        MapView mapView = new MapView(getContext(), baiduMapOptions);

        // #5 在布局上添加地图：0，代表第一位
        mapFrame.addView(mapView, 0);

        //(对地图进行一些操作) 拿到地图的操作类(控制器：操作地图等都是使用这个)
        map = mapView.getMap();

    }

    //一些基础的设置
    @OnClick({R.id.iv_scaleUp, R.id.iv_scaleDown, R.id.tv_satellite, R.id.tv_compass})
    public void onClick(View view) {
        switch (view.getId()) {
            //放大
            case R.id.iv_scaleUp:
                //重新对地图设置状态
                map.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            //缩小
            case R.id.iv_scaleDown:
                map.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
            //切换卫星和普通地图
            case R.id.tv_satellite:
                //获取原地图
                int mapType = map.getMapType();
                //判断原地图是是否是卫星地图(获取切换后的地图类型)
                mapType = mapType == BaiduMap.MAP_TYPE_NORMAL ? BaiduMap.MAP_TYPE_SATELLITE : BaiduMap.MAP_TYPE_NORMAL;
                map.setMapType(mapType);
                // 根据地图类型改变卫星和普通的文字的显示
                String msg = mapType == BaiduMap.MAP_TYPE_NORMAL ? "卫星" : "普通";
                tvSatellite.setText(msg);
                break;
            //打开关闭指南针
            case R.id.tv_compass:
                Log.e("AAA", "指南针");
                //判断指南针是否开着
                boolean compassEnabled = map.getUiSettings().isCompassEnabled();
                //取反
                map.getUiSettings().setCompassEnabled(!compassEnabled);
                break;
        }
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        // 前置：激活定位图层
        map.setMyLocationEnabled(true);
        // 第一步，初始化LocationClient类:LocationClient类必须在主线程中声明，需要Context类型的参数。
        locationClient = new LocationClient(getContext().getApplicationContext());

        //添加参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开GPS
        option.setCoorType("bd09ll");// 设置百度坐标类型，默认gcj02，会有偏差，bd9ll百度地图坐标类型，将无偏差的展示到地图上
        option.setIsNeedAddress(true);// 需要地址信息

        // 第二步，配置定位SDK参数(需要在 LocationClientOption 中添加)
        locationClient.setLocOption(option);

        // 第三步，实现BDLocationListener接口(对定位设置监听)
        locationClient.registerLocationListener(bdLocationListener);

        // 第四步，开始定位
        locationClient.start();
    }

    /**
     * 定位监听
     */
    private BDLocationListener bdLocationListener = new BDLocationListener() {
        // 获取到定位结果
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // 如果没有拿到结果，重新请求
            if (bdLocation == null) {
                locationClient.requestLocation();
                return;
            }

            // 定位结果的经纬度
            double latitude = bdLocation.getLatitude();//纬度
            double longitude = bdLocation.getLongitude();//经度

            //获取到经纬度
            latLng = new LatLng(latitude, longitude);
            //定位的位置
            String currentAddr = bdLocation.getAddrStr();

            Log.i("TAG", "定位的位置：" + currentAddr + "，经纬度：" + latitude + "," + longitude);

            // 设置定位图层展示的数据
            MyLocationData data = new MyLocationData.Builder()

                    // 定位数据展示的经纬度
                    .latitude(latitude)
                    .longitude(longitude)
                    .accuracy(100f)// 定位精度的大小
                    .build();

            // 定位数据展示到地图上
            map.setMyLocationData(data);

//            // 移动到定位的地方，在地图上展示定位的信息：位置
//            moveToLocation();

        }
    };
    /**
     * 定位监听，定位的按钮：移动到定位的地方
     */
    @OnClick(R.id.tv_located)
    public void moveToLocation(){

        // 地图状态的设置：设置到定位的地方
        MapStatus mapStatus = new MapStatus.Builder()
                .target(latLng)// 定位的位置(需要经纬度，在上面获取)
                .rotate(0)//旋转角度
                .zoom(17)//缩放大小
                .overlook(0)// 俯仰的角度
                .build();

        // 更新状态
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(mapStatus);

        // 更新展示的地图的状态
        map.animateMapStatus(update);
    }

}
