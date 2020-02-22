package com.siti.utils;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by 62787 on 2018/10/10.
 * 经纬度坐标转换类
 */
public class CoordinateTransfer {


    public static class MapPointV2 {

        private Double x;   // 经度
        private Double y;   // 纬度

        public MapPointV2(Double x, Double y) {
            this.x = x;
            this.y = y;
        }

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }
    }

    /**
     * 84->高德地图（GCJ-02）
     */
    public static MapPointV2 transformFromWGSToGCJ(Double x, Double y) {

        //如果在国外，则默认不进行转换
        //if (outOfChina(geographicPoint.latitude, wgLoc.longitude))
        //{
        //    return new LatLng(wgLoc.latitude, wgLoc.longitude);
        //}
        double pLongitude = x;
        double pLatitude = y;
        double dLat = transformLat(pLongitude - 105.0, pLatitude - 35.0);
        double dLon = transformLon(pLongitude - 105.0, pLatitude - 35.0);
        Map<String, Object> map = Delta(pLatitude, pLongitude, dLat, dLon);
        dLat = (double) map.get("dLat");
        dLon = (double) map.get("dLon");
        return new MapPointV2(pLongitude + dLon,
                pLatitude + dLat);

    }

    public void demo() {
        MapPointV2 mapPointV2 = transformFromWGSToGCJ(121.460752, 31.200805);
        System.out.println(mapPointV2.getX() + " " + mapPointV2.getY());
    }

    static double a = 6378245.0;//卫星椭球坐标投影到平面地图坐标系的投影因子。
    static double ee = 0.00669342162296594323;// 椭球的偏心率。

    private static Map<String, Object> Delta(double lat, double lon, double dLat, double dLon) {
        Map<String, Object> map = new HashMap<String, Object>();
        /*dLat = transformLat(lon - 105.0, lat - 35.0);
        dLon = transformLon(lon - 105.0, lat - 35.0);*/
        double radLat = lat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);
        map.put("dLat", dLat);
        map.put("dLon", dLon);
        return map;
    }


    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(x > 0 ? x : -x);
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x
                * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0
                * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y
                * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(x > 0 ? x : -x);
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x
                * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0
                * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x
                / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

    public static Boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }
}
