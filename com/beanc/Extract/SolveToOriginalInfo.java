package com.beanc.Extract;

public class SolveToOriginalInfo {

    private int checkGNGGA;//检测是否为GNGGA数据(1为正确，0为非GNGGA数据，-1为非完整GNGGA数据)
    private String utcTime;//utc时间
    private String latitude;//纬度
    private String latitudeDirection;//南北纬方向
    private String longitude;//经度
    private String longitudeDirection;//东西经方向
    private String fixQuality;//定位质量指示符
    private String numberOfSatellites;//使用卫星颗数
    private String horizontalDilution;//水平精度因子
    private String altitude;//海拔高度
    private String altitudeUnits;//高度单位
    private String geoidSeparation;//大地水准面高度
    private String geoidSeparationUnits;//大地水准面高度单位
    private String checksum;//校验和


    /**
     * 输入原始GNGGA数据，并解算出相应数据到实例中
     * @param gnggaData  串口接收到的原始数据
     */
    public SolveToOriginalInfo(String gnggaData) {
        if (!gnggaData.startsWith("$GNGGA")) {
            checkGNGGA = 0;//非GNGGA数据
            return;
        } else {
            checkGNGGA = 1;
        }

        String[] data = gnggaData.split(",");

        if (data.length < 15) {
            checkGNGGA = -1;//非完整GNGGA数据
            return;
        } else {
            checkGNGGA = 1;
        }

        utcTime = data[1];
        latitude = data[2];
        latitudeDirection = data[3];
        longitude = data[4];
        longitudeDirection = data[5];
        fixQuality = data[6];
        numberOfSatellites = data[7];
        horizontalDilution = data[8];
        altitude = data[9];
        altitudeUnits = data[10];
        geoidSeparation = data[11];
        geoidSeparationUnits = data[12];
        checksum = data[14].split("\\*")[1];
    }

    /**
     * 获取GNGGA数据检验(错误原因)
     * @return  (int)0为非GNGGA数据，-1为非完整GNGGA数据；1为正确
     */
    public int getCheckGNGGA() {
        return checkGNGGA;
    }

    /**
     * 获取原始数据中的UTC时间
     * 需要有参数，参数分别为枚举常量：HOURS、MINUTES、SECONDS
     * @param option  包Extract中UTC的枚举常量
     * @return  (double)返回相应的所需数据，default为0
     */
    public double getUtcTime(UTC option) {
        switch (option) {
            case HOURS:
                return Double.parseDouble(utcTime.substring(0, 2));
            case MINUTES:
                return Double.parseDouble(utcTime.substring(2, 4));
            case SECONDS:
                return Double.parseDouble(utcTime.substring(4));
            default:
                return 0;
        }
    }

    /**
     * UTC时间方法重载
     * @return  (double)格式为hhmmss.ss
     */
    public double getUtcTime() {
        return Double.parseDouble(utcTime);
    }

    /**
     * 获取原始数据中的纬度信息
     * 需要有参数，参数分别为枚举常量：DEGREES、MINUTES
     * @param option  包Extract中GRADUATION的枚举常量
     * @return  (double)返回相应的所需数据，default为0
     */
    public double getLatitude(GRADUATION option) {
        switch (option) {
            case DEGREES:
                return Double.parseDouble(latitude.substring(0, 2));
            case MINUTES:
                return Double.parseDouble(latitude.substring(2));
            default:
                return 0;
        }
    }

    /**
     * 纬度方法重载
     * @return  (double)格式为ddmm.mmmm
     */
    public double getLatitude() {
        return Double.parseDouble(latitude);
    }

    /**
     * @return  (String)纬度方向数据
     */
    public String getLatitudeDirection() {
        return latitudeDirection;
    }

    /**
     * 获取原始数据中的经度信息
     * 需要有参数，参数分别为枚举常量：DEGREES、MINUTES
     * @param option  包Extract中GRADUATION的枚举常量
     * @return  (double)返回相应的所需数据，default为0
     */
    public double getLongitude(GRADUATION option) {
        switch (option) {
            case DEGREES:
                return Double.parseDouble(longitude.substring(0, 3));
            case MINUTES:
                return Double.parseDouble(longitude.substring(3));
            default:
                return 0;
        }
    }

    /**
     * 经度方法重载
     * @return  (double)格式为dddmm.mmmm
     */
    public double getLongitude() {
        return Double.parseDouble(longitude);
    }

    /**
     * @return  (String)经度方向数据
     */
    public String getLongitudeDirection() {
        return longitudeDirection;
    }

    /**
     * @return  (int)定位质量指示符
     */
    public int getFixQuality() {
        return Integer.parseInt(fixQuality);
    }

    /**
     * @return  (int)使用卫星数量
     */
    public int getNumberOfSatellites() {
        return Integer.parseInt(numberOfSatellites);
    }

    /**
     * @return  (double)水平精度因子
     */
    public double getHorizontalDilution() {
        return Double.parseDouble(horizontalDilution);
    }

    /**
     * @return  (double)海拔高度
     */
    public double getAltitude() {
        return Double.parseDouble(altitude);
    }

    /**
     * @return  (String)海拔高度单位
     */
    public String getAltitudeUnits() {
        return altitudeUnits;
    }

    /**
     * @return  (double)大地水准面高度
     */
    public double getGeoidSeparation() {
        return Double.parseDouble(geoidSeparation);
    }

    /**
     * @return  (String)大地水准面高度单位
     */
    public String getGeoidSeparationUnits() {
        return geoidSeparationUnits;
    }

    /**
     * @return  (String)校验和
     */
    public String getChecksum() {
        return checksum;
    }
}
