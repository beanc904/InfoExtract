package com.beanc.SerialPort;

import com.fazecast.jSerialComm.SerialPort;

import java.util.LinkedHashSet;
import java.util.Set;

public class Link {
    private static final int PACKET_SIZE = 88; //报文数据字节长度固定为88
    private static final SerialPort[] ports = SerialPort.getCommPorts(); //可使用的串口
    private static Set<String> systemPortName = new LinkedHashSet<>(); //可使用串口的名字
    private static int n = 0; //使用的串口号
    private static SerialPort serialPort = null; //使用的串口实例
    private static volatile String gnggaData; //gngga数据
    private static boolean PortOpen;
    private static boolean PortClose;

    public static void setN(int n) {
        Link.n = n;
    }

    public static boolean isPortOpen() {
        return PortOpen;
    }

    public static boolean isPortClose() {
        return PortClose;
    }

    public static Set<String> getSystemPortName() {
        return systemPortName;
    }

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
        new Thread(() -> {
            try {
                byte[] buffer = new byte[PACKET_SIZE];
                int bufferIndex=0;

                while (true) {
                    if (serialPort.bytesAvailable() > 0) {
                        byte[] readBuffer = new byte[serialPort.bytesAvailable()];
                        int numRead = serialPort.readBytes(readBuffer, readBuffer.length);

                        for (int i = 0; i < numRead; i++) {
                            buffer[bufferIndex++] = readBuffer[i];

                            // 当缓冲区满时，处理报文
                            if (bufferIndex == PACKET_SIZE) {
                                gnggaData = new StringBuilder(new String(buffer)).delete(PACKET_SIZE-2, PACKET_SIZE).toString();
                                //System.out.println(gnggaData);
                                bufferIndex = 0; // 重置缓冲区索引
                            }
                        }
                    }
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
