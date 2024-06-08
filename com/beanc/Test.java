package com.beanc;

import com.beanc.DataConversion.EasyConversion;
import com.beanc.SerialPort.Link;

public class Test {
    public static void main(String[] args) {
        new Link(); //打开串口控制

        System.out.println("现有串口有：");
        for (String s: Link.getSystemPortName()) {
            System.out.println(s);
        }

        while (true) {
            if (Link.getGnggaData() != null) {
                System.out.println("经度："+ EasyConversion.GNGGA_To_GCJ02(Link.getGnggaData())[0] +
                        "\t纬度："+ EasyConversion.GNGGA_To_GCJ02(Link.getGnggaData())[1]);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
