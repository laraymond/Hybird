import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        SensorFusion sensorFusion = new SensorFusion();
        try {
            sensorFusion.run("files/imu.wxyz", "files/points_file.xyz");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
