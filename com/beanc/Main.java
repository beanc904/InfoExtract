package com.beanc;

import com.beanc.DataConversion.Conversion;
import com.beanc.DataConversion.EasyConversion;
import com.beanc.Extract.SolveToOriginalInfo;
import com.beanc.Extract.UTC;
import com.beanc.Extract.GRADUATION;

public class Main {
    public static void main(String[] args) {
        SolveToOriginalInfo info = new SolveToOriginalInfo("$GNGGA,051122.00,3640.4242551,N,11711.1128930,E,1,49,1.0,111.887,M,-2.200,M,0.0,0000*49");
        //new Link();

        System.out.println("数据检测：" +info.getCheckGNGGA()+
                "\n时间：" +info.getUtcTime(UTC.HOURS)+"h "+
                info.getUtcTime(UTC.MINUTES)+"m "+
                info.getUtcTime(UTC.SECONDS)+"s "+
                "\n纬度：" +info.getLatitude(GRADUATION.DEGREES)+"d "+
                info.getLatitude(GRADUATION.MINUTES)+"m "+ "\t纬度方向：" + info.getLatitudeDirection()+
                "\t纬度报文："+info.getLatitude()+
                "\n经度：" +info.getLongitude(GRADUATION.DEGREES)+"d "+
                info.getLongitude(GRADUATION.MINUTES)+"m "+"\t经度方向：" +info.getLongitudeDirection()+
                "\t经度报文："+info.getLongitude()+
                "\n定位质量指示符：" +info.getFixQuality()+
                "\n使用卫星数量：" +info.getNumberOfSatellites()+
                "\n水平精度因子：" +info.getHorizontalDilution()+
                "\n海拔高度：" +info.getAltitude()+ "\t高度单位：" +info.getAltitudeUnits()+
                "\n大地水准面高度：" +info.getGeoidSeparation()+ "\t大地水准面高度单位：" +info.getGeoidSeparationUnits()+
                "\n校验和：" +info.getChecksum());

        double[] ddmm = new double[] {info.getLongitude(), info.getLatitude()};
        double[] ddd = Conversion.DdmmToDdd(ddmm);
        if (ddd != null) {
            System.out.println("\nWGS-84坐标信息解析如上，下面进行坐标转换\n" +
                    "原报文经度："+info.getLongitude()+"\t原报文纬度："+info.getLatitude() +
                    "\nddd格式报文：" +
                    "\n(lng)" + ddd[0] +
                    "\t(lat)" + ddd[1] +
                    "\nGCJ-02坐标信息如下：" +
                    "\n(lng)" + Conversion.WGS84ToGCJ02(ddd)[0] +
                    "\t(lat)" + Conversion.WGS84ToGCJ02(ddd)[1]);
        }

        System.out.println("经度："+ EasyConversion.GNGGA_To_GCJ02("$GNGGA,051122.00,3640.4242551,N,11711.1128930,E,1,49,1.0,111.887,M,-2.200,M,0.0,0000*49")[0] +
                "\t纬度："+ EasyConversion.GNGGA_To_GCJ02("$GNGGA,051122.00,3640.4242551,N,11711.1128930,E,1,49,1.0,111.887,M,-2.200,M,0.0,0000*49")[1]);
    }
}