package com.beanc.SerialPort;

import com.fazecast.jSerialComm.SerialPort;

import java.util.LinkedHashSet;
import java.util.Set;

public class Link {
    private static final SerialPort[] ports = SerialPort.getCommPorts(); //可使用的串口
    private static Set<String> systemPortName = new LinkedHashSet<>(); //可使用串口的名字
    private static int n = 0; //使用的串口号
    private static SerialPort serialPort = null; //使用的串口实例
    private static volatile String gnggaData; //gngga数据
    private static boolean PortOpen;
    private static boolean PortClose;

    /**
     * 选择使用的串口
     * @param n 第一项为0，依次增加
     */
    public static void setN(int n) {
        Link.n = n;
    }

    /**
     * 核查串口是否正常打开
     * @return  正常打开为true，反则false
     */
    public static boolean isPortOpen() {
        return PortOpen;
    }

    /**
     * 核查串口是否正常关闭
     * @return  正常关闭为true，反则false
     */
    public static boolean isPortClose() {
        return PortClose;
    }

    /**
     * 目前可用使用串口信息
     * @return  012，选项依次，使用setN(n)方法设置
     */
    public static Set<String> getSystemPortName() {
        return systemPortName;
    }

    /**
     * 串口接收到的gngga数据
     * @return  获取86字符的$GNGGA原始报文数据
     */
    public static String getGnggaData() {
        return gnggaData;
    }

    public Link() {
        // 列出所有可用的串口
        for (SerialPort port : ports) {
            systemPortName.add(port.getSystemPortName());
        }
        // 出错
        if (ports.length == 0) {
            systemPortName = null;
            return;
        }

        // 选择一个可用的串口
        serialPort = ports[n];
        //System.out.println("使用串口：" + serialPort.getSystemPortName());

        // 设置串口参数
        serialPort.setComPortParameters(115200, 8, SerialPort.TWO_STOP_BITS, SerialPort.NO_PARITY);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        // 打开串口
        if (serialPort.openPort()) {
            //System.out.println("成功打开串口");
            PortOpen = true;
        } else {
            //System.out.println("串口打开失败");
            PortOpen = false;
            return;
        }

        // 创建线程读取串口数据(需要使用设备测试的代码段)
        StringBuilder buffer = new StringBuilder();

        new Thread(() -> {
            try {
                while (true) {
                    if (serialPort.bytesAvailable() > 0) {
                        byte[] readBuffer = new byte[serialPort.bytesAvailable()];
                        int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                        buffer.append(new String(readBuffer, 0, numRead));

                        int endOfLineIndex;
                        while ((endOfLineIndex = buffer.indexOf("\r\n")) != -1) {
                            gnggaData = buffer.substring(0, endOfLineIndex);
                            buffer.delete(0, endOfLineIndex + 2); // 清除缓存区内容
                        }
                    }
//这边的进程似乎不需要睡眠，获取多少个数据是由设备说了算。
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        //(结束)

        // 关闭串口
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (serialPort.closePort()) {
                //System.out.println("串口关闭成功");
                PortClose = true;
            } else {
                //System.out.println("串口关闭失败");
                PortClose = false;
            }
        }));
    }
}
