package com.beanc.RMSE;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.OptionalDouble;
import java.util.Set;

public class Calculation {
    // 给定的WGS-84参数
    private static final double a = 6378137.0;//长半轴
    private static final double f = 1 / 298.257223563;// 扁率
    private static final double e2 = 2 * f - f * f;// 第一偏心率的平方

    /**
     * 将WGS-84的经纬度、椭球高度转化为地心坐标系的XYZ
     * @param B 纬度
     * @param L 经度
     * @param H 椭球高度
     * @return  double[3]的地心坐标系XYZ
     */
    public static double[] BLHtoXYZ(double B, double L, double H) {
        // 将度数转换为弧度
        double B_rad = Math.toRadians(B);
        double L_rad = Math.toRadians(L);

        // 计算卯酉圈曲率半径N
        double N = a / Math.sqrt(1 - e2 * Math.sin(B_rad) * Math.sqrt(B_rad));

        // 计算地心坐标
        double X = (N + H) * Math.cos(B_rad) * Math.cos(L_rad);
        double Y = (N + H) * Math.cos(B_rad) * Math.sin(L_rad);
        double Z = (N * (1 - e2) + H) * Math.sin(B_rad);

        return new double[] {X, Y, Z};
    }

    private static Set<Double> X = new LinkedHashSet<>();
    private static Set<Double> Y = new LinkedHashSet<>();
    private static Set<Double> Z = new LinkedHashSet<>();
    private static int n=0;// 地心坐标系组的数量

    /**
     * 求平均值
     * @param numbers Double类型的Set集合
     * @return  正确返回平均值，错误返回0
     */
    private static double average(Set<Double> numbers) {
        // 使用Stream API计算平均值
        OptionalDouble average = numbers.stream()
                .mapToDouble(Double::doubleValue)
                .average();

        // 输出平均值
        if (average.isPresent()) {
            return average.getAsDouble();
        } else {
            return 0;
        }
    }

    /**
     * 求多个Double类型Set集合中数据的和
     * @param sets 多个Set集合
     * @return  返回和
     */
    @SafeVarargs
    private static double mergeAndSumSets(Set<Double>... sets) {
        // 创建一个新的Set，用于存放合并后的所有值
        Set<Double> mergedSet = new HashSet<>();

        // 将所有Set中的值合并到新的Set中
        for (Set<Double> set : sets) {
            mergedSet.addAll(set);
        }

        // 使用流操作将所有值求和
        return mergedSet.stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    /**
     * 设置地心坐标系XYZ
     * @param X 坐标系X
     * @param Y 坐标系Y
     * @param Z 坐标系Z
     */
    public static void setXYZ(double X, double Y, double Z) {
        Calculation.X.add(X);
        Calculation.Y.add(Y);
        Calculation.Z.add(Z);
        n+=3;
    }

    /**
     * 求均方根误差
     * @return  均方根误差值
     */
    public static double RMSE() {
        double RMSE;
        // 使用平均值代替实际值
        double averageX = average(X);// X的平均值
        double averageY = average(Y);// Y的平均值
        double averageZ = average(Z);// Z的平均值
        // 观测值减去实际值的平方
        Set<Double> sqrtX = new LinkedHashSet<>();
        Set<Double> sqrtY = new LinkedHashSet<>();
        Set<Double> sqrtZ = new LinkedHashSet<>();

        for (double x: X) {
            sqrtX.add((x - averageX) * (x - averageX));
        }
        for (double y: Y) {
            sqrtY.add((y - averageY) * (y - averageY));
        }
        for (double z: Z) {
            sqrtZ.add((z - averageZ) * (z - averageZ));
        }

        RMSE = Math.sqrt(mergeAndSumSets(sqrtX, sqrtY, sqrtZ) / n);
        return RMSE;
    }
}
