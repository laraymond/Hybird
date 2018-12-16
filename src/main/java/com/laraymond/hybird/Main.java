package com.laraymond.hybird;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        SensorFusion sensorFusion = new SensorFusion();
        try {
            sensorFusion.run("imu.wxyz", "points_file.xyz");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
