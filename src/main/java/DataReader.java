import model.IMU;
import model.PtCloud;
import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import util.EveryNth;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataReader {

    public void read(
            String imuFileName,
            String ptcloudFileName
    ) throws IOException {

        ClassLoader classLoader = getClass().getClassLoader();
        File firstFile = new File(classLoader.getResource(imuFileName).getFile());
        File secondFile = new File(classLoader.getResource(ptcloudFileName).getFile());

        Stream<String> imuFile = Files.lines(firstFile.toPath());
        Stream<String> ptcloudFile = Files.lines(secondFile.toPath());
        //Stream<String> secondFile = Files.lines(Paths.get(secondFilePath));

        imuFile.map(this::toIMU)
                .forEach(System.out::println);
        System.out.println("...........");
        ptcloudFile.map(this::toPtCloud)
                .forEach(System.out::println);

    }

    private IMU toIMU(String line) {
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

    private PtCloud toPtCloud(String line) {
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
