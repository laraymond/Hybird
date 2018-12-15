import java.io.IOException;

public class SensorFusion {

    public static final String strDate = "14/12/2018 13:12:35:434";
    public static Long millis = 0L;

    public static void main(String[] args) {
        System.out.println("Hello World!");
        DataReader dataReader = new DataReader();
        try {
            dataReader.read("files/imu.wxyz", "files/points_file.xyz");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
