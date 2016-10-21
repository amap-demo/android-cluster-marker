package com.amap.apis.cluster;

import com.amap.api.maps.model.Marker;

import java.util.List;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public interface ClusterClickListener{
        /**
         * 点击聚合点的回调处理函数
         *
         * @param marker
         *            点击的聚合点
         * @param clusterItems
         *            聚合点所包含的元素
         */
        public void onClick(Marker marker, List<ClusterItem> clusterItems);
}
