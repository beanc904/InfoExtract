package com.beanc.DataConversion;

import com.beanc.Extract.SolveToOriginalInfo;

public class EasyConversion {
    /**
     * 直接将原始$GNGGA报文数据转换为GCJ-02ddd经纬坐标数据
     * 请确认数据正确
     * @param gnggaData 原始报文数据
     * @return  GCJ-02(高德)可用数据
     */
    public static double[] GNGGA_To_GCJ02(String gnggaData) {
        SolveToOriginalInfo info = new SolveToOriginalInfo(gnggaData);
        double[] ddmm = new double[] {info.getLongitude(), info.getLatitude()};
        double[] ddd = Conversion.DdmmToDdd(ddmm);
        double[] gcj02 = Conversion.WGS84ToGCJ02(ddd);
        return gcj02;
    }
}
