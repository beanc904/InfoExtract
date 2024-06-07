package com.beanc;

import com.beanc.SerialPort.Link;

public class Test {
    public static void main(String[] args) {
        new Link(); //打开串口控制

        System.out.println("现有串口有：");
        for (String s: Link.getSystemPortName()) {
            System.out.println(s);
        }

        while (true) {
            System.out.println(Link.getGnggaData());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
