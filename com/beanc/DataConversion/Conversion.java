package com.beanc.DataConversion;

public class Conversion {
    private static final double x_pi = 3.14159265358979324*3000.0/180.0;
    private static final double pi = 3.1415926535897932384626;
    private static final double a = 6378245.0; //半长轴
    private static final double ee = 0.00669342162296594323; //扁率

    /**
     * 纬度的中间值公式
     */
    private static double transformlat(double lng, double lat) {
        double ret_lat = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret_lat += (20.0 * Math.sin(6.0 * lng * pi) + 20.0 * Math.sin(2.0 * lng * pi)) * 2.0 / 3.0;
        ret_lat += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
        ret_lat += (160.0 * Math.sin(lat / 12.0 * pi) + 320 * Math.sin(lat * pi / 30.0)) * 2.0 / 3.0;
        return ret_lat;
    }

    /**
     * 经度的中间值公式
     */
    private static double transformlng(double lng, double lat) {
        double ret_lng = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret_lng += (20.0 * Math.sin(6.0 * lng * pi) + 20.0 * Math.sin(2.0 * lng * pi)) * 2.0 / 3.0;
        ret_lng += (20.0 * Math.sin(lng * pi) + 40.0 * Math.sin(lng / 3.0 * pi)) * 2.0 / 3.0;
        ret_lng += (150.0 * Math.sin(lng / 12.0 * pi) + 300.0 * Math.sin(lng / 30.0 * pi)) * 2.0 / 3.0;
        return ret_lng;
    }

    /**
     * 判断是否在国内，不在国内不做偏移
     * @param lng ddd格式的经度
     * @param lat ddd格式的纬度
     * @return  是否非国内坐标
     */
    private static boolean OutOfChina(double lng, double lat) {
        if (lng>73.66 && lng<135.05 && lat>3.86 && lat<53.55) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 将ddmm格式的经纬度报文数据转化为ddd格式
     * @param info 经纬度报文数组，info[0]为经度，info[1]为纬度
     * @return  格式为double[]类型的数组，[0]为ddd经度[1]为ddd纬度，不符合格式则返回null
     */
    public static double[] DdmmToDdd(double[] info) {
        if (info.length != 2) {
            return null;
        }

        //经度转换
        int lngDeg = (int) (info[0] / 100);
        double lngMin = info[0] - (lngDeg * 100);
        double lngDdd = lngDeg + (lngMin / 60.0);

        //纬度转换
        int latDeg = (int) (info[1] / 100);
        double latMin = info[1] - (latDeg * 100);
        double latDdd = latDeg + (latMin / 60.0);

        return new double[] {lngDdd, latDdd};
    }

    /**
     * GNGGA报文GPS标准坐标系 WGS-84 转Google Maps、高德 GCJ-02 火星坐标系
     * @param info 经纬度报文数组，info[0]为经度，info[1]为纬度
     * @return  GCJ-02的double[]数组，[0]为经度[1]为纬度
     */
    public static double[] WGS84ToGCJ02(double[] info) {
        //如果不在国内，不进行坐标转换
        if (OutOfChina(info[0], info[1])) {
            return new double[] {info[0], info[1]};
        }

        double dlat, dlng, radlat, magic, sqrtmagic, mglat, mglng;
        dlat = transformlat(info[0] - 105.0, info[1] - 35.0);
        dlng = transformlng(info[0] - 105.0, info[1] - 35.0);
        radlat = info[1] / 180.0 * pi;
        magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * pi);
        mglat = info[1] + dlat;
        mglng = info[0] + dlng;
        return new double[] {mglng, mglat};
    }
}
