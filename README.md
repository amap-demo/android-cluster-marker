本工程为基于高德地图Android SDK进行封装，实现了Marker聚合效果的例子
## 前述 ##
- [高德官网申请key](http://lbs.amap.com/dev/#/).
- 阅读[参考手册](http://a.amap.com/lbs/static/unzip/Android_Map_Doc/index.html).
- 工程基于Android 3D地图SDK实现

## 功能描述 ##
基于3D地图SDK进行封装，在大量点需要显示的场景下可以对点进行聚合显示，提高地图响应显示效率。在之前的开源工程版本基础上做了一些修改，提高了显示速度和内存优化使用，支持的聚合点更多，效果更好。主要修改包括：
- 将marker处理线程和聚合算法线程进行分离，提高效率。
- 聚合过程中减少不必要的运算，提高聚合显示速度。
- 适配新版SDK应用场景，对用到的图标，统一管理，减少内存使用和提高效率，
- 加入了动画效果，聚合切换过程更为平滑 

## 效果图如下 ##

![Screenshot](https://raw.githubusercontent.com/amap-demo/android-cluster-marker/master/resource/Screenshot.png)  

## 扫一扫安装##
![Screenshot]( https://raw.githubusercontent.com/amap-demo/android-cluster-marker/master/resource/download.png)  


## 使用方法##
###1:配置搭建AndroidSDK工程###
- [Android Studio工程搭建方法](http://lbs.amap.com/api/android-sdk/guide/creat-project/android-studio-creat-project/#add-jars).
- [通过maven库引入SDK方法](http://lbsbbs.amap.com/forum.php?mod=viewthread&tid=18786).

###2:接口使用###

- 初始化聚合和加入元素
1:批量初始化，适合一次大量加入
``` java
     
      List<ClusterItem> items = new ArrayList<ClusterItem>();

       //随机10000个点
       for (int i = 0; i < 10000; i++) {

           double lat = Math.random() + 39.474923;
           double lon = Math.random() + 116.027116;

           LatLng latLng = new LatLng(lat, lon, false);
           RegionItem regionItem = new RegionItem(latLng,
                   "test" + i);
           items.add(regionItem);

       }
       mClusterOverlay = new ClusterOverlay(mAMap, items,
               dp2px(getApplicationContext(), clusterRadius),
                     getApplicationContext());
```
2:初始化之后，有新的聚合点加入
``` java
     
     double lat = Math.random() + 39.474923;
     double lon = Math.random() + 116.027116;

     LatLng latLng1 = new LatLng(lat, lon, false);
     RegionItem regionItem = new RegionItem(latLng1,
             "test");
     mClusterOverlay.addClusterItem(regionItem);
```
- 设置渲染render和聚合点点击事件监听
``` java
      mClusterOverlay.setClusterRenderer(MainActivity.this);
      mClusterOverlay.setOnClusterClickListener(MainActivity.this);
```
- 自定义渲染
``` java
          public Drawable getDrawAble(int clusterNum) {
        int radius = dp2px(getApplicationContext(), 80);
        if (clusterNum == 1) {
            Drawable bitmapDrawable = mBackDrawAbles.get(1);
            if (bitmapDrawable == null) {
                bitmapDrawable =
                        getApplication().getResources().getDrawable(
                        R.drawable.icon_openmap_mark);
                mBackDrawAbles.put(1, bitmapDrawable);
            }

            return bitmapDrawable;
        } else if (clusterNum < 5) {

            Drawable bitmapDrawable = mBackDrawAbles.get(2);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(159, 210, 154, 6)));
                mBackDrawAbles.put(2, bitmapDrawable);
            }

            return bitmapDrawable;
        } else if (clusterNum < 10) {
            Drawable bitmapDrawable = mBackDrawAbles.get(3);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(199, 217, 114, 0)));
                mBackDrawAbles.put(3, bitmapDrawable);
            }

            return bitmapDrawable;
        } else {
            Drawable bitmapDrawable = mBackDrawAbles.get(4);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(235, 215, 66, 2)));
                mBackDrawAbles.put(4, bitmapDrawable);
            }

            return bitmapDrawable;
        }
    }
```
- 聚合点击事件
``` java
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {
        mClusterItems = clusterItems;
        mCenterLat = marker.getPosition();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (ClusterItem clusterItem : clusterItems) {
            builder.include(clusterItem.getPosition());


        }
        LatLngBounds latLngBounds = builder.build();
        mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0)
        );
    }
```