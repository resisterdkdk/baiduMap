package com.example.administrator.baidumap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;

public class MainActivity extends Activity {
    //按钮
    private ImageButton send;
    private ImageButton bt_friend;
    static LatLng locat_now;
    private ImageButton bt_enemy;
    private ImageButton bt_myp;
    static private boolean if_on=false;
    MapView mMapView = null;
    int i;
    private BaiduMap mBaiduMap;
    private Context context;
    //定位相关
    private LocationClient mLocationClient;
    private  MyLocationListener mLocationListener;
    private boolean isFirstIn = true;
    static private boolean isFirstAdd = true;
    //数据存储
    static public SharedPreferences pref;
    static public int friend_num = 0;

    static public SharedPreferences pref_enemy;
    static public int enemy_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        this.context = this;
        pref = getSharedPreferences("myPref",MODE_PRIVATE);
        pref_enemy = getSharedPreferences("enemyPref",MODE_PRIVATE);
        initview1();
        //获取地图控件引用
        initLocation();
        bt_enemy = (ImageButton)findViewById(R.id.imageButton_enemy_1);
        bt_enemy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,enemys.class);
                startActivity(intent);
            }
        });
        bt_myp = (ImageButton) findViewById(R.id.button_my);
        bt_myp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(locat_now);
                mBaiduMap.animateMapStatus(u);
            }
        });
        bt_friend = (ImageButton) findViewById(R.id.imageButton_friend_1);
        bt_friend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,friends.class);
                startActivity(intent);
            }
        });
        send = (ImageButton) findViewById(R.id.imageButton_send);
        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager manager = SmsManager.getDefault();
                for(int i=0;i<friend_num;i++)
                {
                    String key = String.valueOf(i)+"_num";
                    if(pref.getString(key,null)==null)continue;
                    manager.sendTextMessage(pref.getString(key,null), null, "Where are you?", null, null);
                }
                for(int i=0;i<enemy_num;i++)
                {
                    String key = String.valueOf(i)+"_num";
                    if(pref_enemy.getString(key,null)==null)continue;
                    manager.sendTextMessage(pref_enemy.getString(key,null), null, "Where are you?", null, null);
                }
                Toast.makeText(getApplicationContext(), "发送完毕,收到短信后请刷新页面", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFriend() {
        String str[] = new String[10];
        str[0] = "张三";
        str[1] = "李四";
        str[2] = "王五";
        String str2[]=new String[10];
        str2[0]="18900893717";
        str2[1]="18900893717";
        str2[2]="18900893717";
        float f[] = new float[10];
        f[0]=(float)22.257658;
        f[1]=(float)22.246658;
        f[2]=(float)22.266658;
        float f1[] = new float[10];
        f1[0]=(float)113.522453;
        f1[1]=(float)113.532453;
        f1[2]=(float)113.552453;
        SharedPreferences.Editor editor = pref.edit();
        friend_num = 3;
        for( i=0;i<3;i++)
        {
            String key = String.valueOf(i)+"_num";
            editor.putString(key,str2[i]);
            key = String.valueOf(i)+"_id";
            editor.putString(key,String.valueOf(i));
            key = String.valueOf(i)+"_name";
            String frin = str[i];
            editor.putString(key,frin);
            key = String.valueOf(i)+"_jing";
            editor.putFloat(key, f1[i]);
            key = String.valueOf(i)+"_wei";
            editor.putFloat(key,f[i]);
            editor.commit();
            String jing=String.valueOf(i)+"_jing";
            String wei = String.valueOf(i)+"_wei";
            LatLng ll = new LatLng(pref.getFloat(wei,0),pref.getFloat(jing,0));
            String address = String.valueOf(i)+"_address";
            String altitude = String.valueOf(i)+"_altitude";


        }
    }
    private void addEnemy() {
        String str[] = new String[10];
        str[0] = "龙九";
        str[1] = "赵八";
        String str2[]=new String[10];
        str2[0]="18900893717";
        str2[1]="18900893717";
        float f[] = new float[10];
        f[0]=(float)22.259658;
        f[1]=(float)22.240658;
        float f1[] = new float[10];
        f1[0]=(float)113.529453;
        f1[1]=(float)113.538453;
        SharedPreferences.Editor editor = pref_enemy.edit();
        enemy_num = 2;
        for( i=0;i<2;i++)
        {
            String key = String.valueOf(i)+"_num";
            editor.putString(key,str2[i]);
            key = String.valueOf(i)+"_id";
            editor.putString(key,String.valueOf(i));
            key = String.valueOf(i)+"_name";
            String frin = str[i];
            editor.putString(key,frin);
            key = String.valueOf(i)+"_jing";
            editor.putFloat(key, f1[i]);
            key = String.valueOf(i)+"_wei";
            editor.putFloat(key,f[i]);
            editor.commit();
            String jing=String.valueOf(i)+"_jing";
            String wei = String.valueOf(i)+"_wei";
            LatLng ll = new LatLng(pref_enemy.getFloat(wei,0),pref_enemy.getFloat(jing,0));
            String address = String.valueOf(i)+"_address";
            String altitude = String.valueOf(i)+"_altitude";

        }
    }
    private void markFriend() {
        friends fri = new friends();
        SharedPreferences.Editor editor = pref.edit();

        for(int i=0;i<3;i++)
        {
          //  LatLng ll = new LatLng(location.getLatitude()纬度,location.getLongitude()经度);
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.friend_marker);
            String jing=String.valueOf(i)+"_jing";
            String wei = String.valueOf(i)+"_wei";
            LatLng ll = new LatLng(pref.getFloat(wei,0),pref.getFloat(jing,0));
            double dis = DistanceUtil.getDistance(ll, locat_now);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(ll).icon(bitmap);
            //在地图上添加Marker，并显示
            mMapView.getMap().addOverlay(option);
            //定义文字所显示的坐标点
//构建文字Option对象，用于在地图上添加文字
            String name = String.valueOf(i)+"_name";
            String num = String.valueOf(i)+"_num";
            OverlayOptions textOption = new TextOptions()
                    .bgColor(0xAAFFFF00)
                    .fontSize(24)
                    .fontColor(0xFFFF00FF)
                    .text(pref.getString(name,null)+"\n"+pref.getString(num,null)+" 距离"+String.valueOf((int)dis)+"米")
                    .rotate(-30)
                    .position(ll);
//在地图上添加该文字对象并显示
           mBaiduMap.addOverlay(textOption);

        }

    }
    private void markEnemy() {
        enemys ene = new enemys();
        SharedPreferences.Editor editor = pref_enemy.edit();

        for(int i=0;i<2;i++)
        {
            //  LatLng ll = new LatLng(location.getLatitude()纬度,location.getLongitude()经度);
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.enemy_marker);
            String jing=String.valueOf(i)+"_jing";
            String wei = String.valueOf(i)+"_wei";
            LatLng ll = new LatLng(pref_enemy.getFloat(wei,0),pref_enemy.getFloat(jing,0));
            double dis = DistanceUtil.getDistance(ll, locat_now);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(ll).icon(bitmap);
            //在地图上添加Marker，并显示
            mMapView.getMap().addOverlay(option);
            //定义文字所显示的坐标点
//构建文字Option对象，用于在地图上添加文字
            String name = String.valueOf(i)+"_name";
            String num = String.valueOf(i)+"_num";
            OverlayOptions textOption = new TextOptions()
                    .bgColor(0xAAFFFF00)
                    .fontSize(24)
                    .fontColor(0xFFFF00FF)
                    .text(pref_enemy.getString(name,null)+"\n"+pref_enemy.getString(num,null)+" 距离"+String.valueOf((int)dis)+"米")
                    .rotate(-30)
                    .position(ll);
//在地图上添加该文字对象并显示
            mBaiduMap.addOverlay(textOption);

        }

    }
    private void initview1() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //地图放大
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
    }

    private void initLocation() {
        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onStop() {
        super.onStop();
        //停止定位
        if_on =true;
        mBaiduMap.setMyLocationEnabled(false);
        mBaiduMap.clear();
        mLocationClient.stop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();


       if(if_on==true)
       {
           if_on = false;
           for(int i=0;i<friend_num;i++)
           {
               String jing=String.valueOf(i)+"_jing";
               String wei = String.valueOf(i)+"_wei";
               String name = String.valueOf(i)+"_name";
               String num = String.valueOf(i)+"_num";
               //  LatLng ll = new LatLng(location.getLatitude()纬度,location.getLongitude()经度);
               //构建Marker图标
               if(pref.getFloat(wei,1000)==1000||pref.getString(num,null)==null)
                   continue;
               BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.friend_marker);

               LatLng ll = new LatLng(pref.getFloat(wei,0),pref.getFloat(jing,0));
               double dis = DistanceUtil.getDistance(ll, locat_now);
               //构建MarkerOption，用于在地图上添加Marker
               OverlayOptions option = new MarkerOptions().position(ll).icon(bitmap);
               //在地图上添加Marker，并显示
               mMapView.getMap().addOverlay(option);
               //定义文字所显示的坐标点
//构建文字Option对象，用于在地图上添加文字

               OverlayOptions textOption = new TextOptions()
                       .bgColor(0xAAFFFF00)
                       .fontSize(24)
                       .fontColor(0xFFFF00FF)
                       .text(pref.getString(name,null)+"\n"+pref.getString(num,null)+" 距离"+String.valueOf((int)dis)+"米")
                       .rotate(-30)
                       .position(ll);
//在地图上添加该文字对象并显示
               mBaiduMap.addOverlay(textOption);

           }


           for(int i=0;i<enemy_num;i++)
           {
               String jing=String.valueOf(i)+"_jing";
               String wei = String.valueOf(i)+"_wei";
               String name = String.valueOf(i)+"_name";
               String num = String.valueOf(i)+"_num";
               //  LatLng ll = new LatLng(location.getLatitude()纬度,location.getLongitude()经度);
               //构建Marker图标
               if(pref_enemy.getFloat(wei,1000)==1000||pref_enemy.getString(num,null)==null)
                   continue;
               BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.enemy_marker);

               LatLng ll = new LatLng(pref_enemy.getFloat(wei,0),pref_enemy.getFloat(jing,0));
               double dis = DistanceUtil.getDistance(ll, locat_now);
               //构建MarkerOption，用于在地图上添加Marker
               OverlayOptions option = new MarkerOptions().position(ll).icon(bitmap);
               //在地图上添加Marker，并显示
               mMapView.getMap().addOverlay(option);
               //定义文字所显示的坐标点
//构建文字Option对象，用于在地图上添加文字

               OverlayOptions textOption = new TextOptions()
                       .bgColor(0xAAFFFF00)
                       .fontSize(24)
                       .fontColor(0xFFFF00FF)
                       .text(pref_enemy.getString(name,null)+"\n"+pref_enemy.getString(num,null)+" 距离"+String.valueOf((int)dis)+"米")
                       .rotate(-30)
                       .position(ll);
//在地图上添加该文字对象并显示
               mBaiduMap.addOverlay(textOption);

           }
       }


    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onStart() {
        super.onStart();
        //开启定位
        mBaiduMap.setMyLocationEnabled(true);
      //  if(!mLocationClient.isStarted())
            mLocationClient.start();

    }
    private class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if(location == null || mMapView == null)
                return ;
            MyLocationData data = new MyLocationData.Builder()//
                    .accuracy(location.getRadius()).direction(100)//
                    .latitude(location.getLatitude())//
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(data);

            //MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,arg1,arg2);
            if(isFirstIn)
            {
                //经纬度
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                locat_now = ll;
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
                isFirstIn = false;
                Toast.makeText(context,location.getAddrStr(),Toast.LENGTH_SHORT).show();
                if(isFirstAdd)
                {
                    addFriend();
                    markFriend();
                    addEnemy();
                    markEnemy();
                    isFirstAdd = false;
                }

            }
        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
   public void change(String sender,float jing,float wei)
   {
       SharedPreferences.Editor editor = pref.edit();

       for(int i=0;i<friend_num;i++)
       {
           String num = String.valueOf(i)+"_num";
           String jing1=String.valueOf(i)+"_jing";
           String wei1 = String.valueOf(i)+"_wei";
           String number =pref.getString(num,null);
           if(number == null) continue;
           if(number.compareTo(sender)!=0)continue;
               editor.putFloat(jing1,jing);
               editor.putFloat(wei1,wei);
               editor.commit();
       }
       SharedPreferences.Editor editor_enemy = pref_enemy.edit();

       for(int i=0;i<enemy_num;i++)
       {
           String num = String.valueOf(i)+"_num";
           String jing1=String.valueOf(i)+"_jing";
           String wei1 = String.valueOf(i)+"_wei";
           String number =pref_enemy.getString(num,null);
           if(number == null)continue;
           if(number.compareTo(sender)!=0)continue;
               editor_enemy.putFloat(jing1,jing);
               editor_enemy.putFloat(wei1,wei);
               editor_enemy.commit();
       }
   }
}

// GeoCoder geoCoder = GeoCoder.newInstance();
//
     /*       OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
                // 反地理编码查询结果回调函数
                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                    if (result == null
                            || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        // 没有检测到结果
                        Toast.makeText(MainActivity.this, "抱歉，未能找到结果",
                                Toast.LENGTH_LONG).show();
                    }
                    SharedPreferences.Editor editor = pref.edit();

                    String address = String.valueOf(i)+"_address";
                    editor.putString(address,result.getAddress());
                    editor.commit();
                    Toast.makeText(MainActivity.this,
                            "位置：" + result.getAddress(), Toast.LENGTH_LONG)
                            .show();
                }

                // 地理编码查询结果回调函数
                @Override
                public void onGetGeoCodeResult(GeoCodeResult result) {
                    if (result == null
                            || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        // 没有检测到结果
                    }
                }

            };
            // 设置地理编码检索监听者
            geoCoder.setOnGetGeoCodeResultListener(listener);
            //
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
            //String a =listener.toString();
            // 释放地理编码检索实例
             geoCoder.destroy();*/