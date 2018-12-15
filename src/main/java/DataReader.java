import model.IMU;
import model.PtCloud;
import org.apache.commons.math3.complex.Quaternion;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DataReader {

    public void read(
            String imuFileName,
            String ptcloudFileName
    ) throws IOException {

        BufferedReader ptCloudReader = getBufferedReader(ptcloudFileName);
        BufferedReader imuReader = getBufferedReader(imuFileName);
        PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
        String ptCloudLine = ptCloudReader.readLine();

        while (ptCloudLine != null) {
            PtCloud currentPtCloud = toPtCloud(ptCloudLine);
            long ptCloudTime = currentPtCloud.getEpochTime();
            IMU targetIMU = findMatchingImuLine(imuReader, ptCloudTime);
            ptCloudLine = ptCloudReader.readLine();
            PtCloud transformedPtCloud = transform(targetIMU, currentPtCloud);

            writer.println(transformedPtCloud.toLine());

        }
        writer.close();
        ptCloudReader.close();
        imuReader.close();
    }

    private BufferedReader getBufferedReader(String ptcloudFileName) {
        return new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + ptcloudFileName)));
    }

    private IMU findMatchingImuLine(BufferedReader imuReader, long ptCloudTime) throws IOException {
        for (String next, line = imuReader.readLine(); line != null; line = next) {
            next = imuReader.readLine();
            long nextIMUTime = toIMU(next).getEpochTime();
            long currentIMUTime = toIMU(line).getEpochTime();
            if (nextIMUTime > ptCloudTime) {
                if (timediff(nextIMUTime, ptCloudTime) > timediff(currentIMUTime, ptCloudTime)) {
                    return toIMU(line);
                } else {
                    return toIMU(next);
                }
            }
        }
        return null;
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

    private PtCloud transform(IMU imu, PtCloud ptcloud) {
        long diff = imu.getEpochTime() - ptcloud.getEpochTime();
        //System.out.println(diff / 1000000);

        Rotation rotQ = new Rotation(
                imu.getQuaternion().getQ0(),
                imu.getQuaternion().getQ1(),
                imu.getQuaternion().getQ2(),
                imu.getQuaternion().getQ3(),
                true
        );
        List<Vector3D> rotated = ptcloud.getVector3D().stream()
                .map(rotQ::applyTo)
                .collect(Collectors.toList());

        return new PtCloud(rotated, ptcloud.getEpochTime());
    }

    private Long timediff(long time1, long time2) {
        return Math.abs(time2 - time1);
    }

}
