package com.beanc;

import com.beanc.DataConversion.Conversion;
import com.beanc.Extract.SolveToOriginalInfo;
import com.beanc.RMSE.Calculation;
import com.beanc.SerialPort.Link;

public class TestRMSE {
    public static void main(String[] args) {
        new Link(); //打开串口控制

        System.out.println("现有串口有：");
        for (String s: Link.getSystemPortName()) {
            System.out.println(s);
        }

        while (true) {
            if (Link.getGnggaData() != null) {
                SolveToOriginalInfo info = new SolveToOriginalInfo(Link.getGnggaData());
                double[] ddmm = new double[] {info.getLongitude(), info.getLatitude()};
                double[] ddd = Conversion.DdmmToDdd(ddmm);
                double height = info.getAltitude() + info.getGeoidSeparation();

                if (ddd != null) {
                    double[] XYZ = Calculation.BLHtoXYZ(ddd[1], ddd[0], height);
                    Calculation.setXYZ(XYZ[0], XYZ[1], XYZ[2]);
                    System.out.println("RMSE值：" + Calculation.RMSE());
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
