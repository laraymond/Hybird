package model;

import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class ModelMapper {
    public static IMU toIMU(String line) {
        String[] split = line.split(" ");
        long time = Long.parseLong(split[0]);
        Quaternion quaternion = new Quaternion(
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Double.parseDouble(split[4])
        );
        return new IMU(quaternion, time);

    }

    public static PtCloud toPtCloud(String line) {
        String[] split = line.split(" ");
        long time = Long.parseLong(split[0]);
        List<Vector3D> vector = new ArrayList<>();
        int counter = 0;
        for (int i = 1; i < split.length; i++) {
            counter++;
            if (counter % 3 == 0) {
                Vector3D pt = new Vector3D(
                        Double.parseDouble(split[i - 2]),
                        Double.parseDouble(split[i - 1]),
                        Double.parseDouble(split[i]));
                vector.add(pt);
            }
        }

        return new PtCloud(vector, time);
    }
}
