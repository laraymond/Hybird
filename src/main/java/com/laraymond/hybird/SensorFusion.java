package com.laraymond.hybird;

import com.laraymond.hybird.model.IMU;
import com.laraymond.hybird.model.PtCloud;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.laraymond.hybird.model.ModelMapper.toIMU;
import static com.laraymond.hybird.model.ModelMapper.toPtCloud;


class SensorFusion {

    private static final double TIME_BOUNDARY = 50 * 1e6;
    private String lastIMU = null;

    /**
     * Given two source files representing the readings of a set of PtClouds and IMUs, generates a file result of
     * the fusion between the two sensors synchronized.
     * <p>
     * The output will be saved in a file named ptcloud-rotated-yyyy-mm-dd-HH:mm:ss
     *
     * @param imuFilePath
     * @param ptcloudFilePath
     * @throws IOException
     */
    public void run(
            String imuFilePath,
            String ptcloudFilePath
    ) throws IOException {

        BufferedReader ptCloudReader = getBufferedReader(ptcloudFilePath);
        BufferedReader imuReader = getBufferedReader(imuFilePath);
        PrintWriter writer = getPrintWriter();
        String ptCloudLine = ptCloudReader.readLine();

        while (ptCloudLine != null) {
            PtCloud currentPtCloud = toPtCloud(ptCloudLine);
            long ptCloudTime = currentPtCloud.getEpochTime();

            IMU targetIMU = findMatchingImuLine(imuReader, ptCloudTime);

            if (targetIMU != null) {
                Long timeDiff = timeDiff(targetIMU.getEpochTime(), currentPtCloud.getEpochTime());
                System.out.println("point cloud match " + timeDiff);
                PtCloud transformedPtCloud = rotatePoint(targetIMU, currentPtCloud);
                writer.println(transformedPtCloud.toLine());
            } else {
                System.out.println("point cloud rejected");
            }

            ptCloudLine = ptCloudReader.readLine();


        }
        writer.close();
        ptCloudReader.close();
        imuReader.close();
    }

    private PrintWriter getPrintWriter() throws FileNotFoundException, UnsupportedEncodingException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String formattedTime = sdf.format(date);
        return new PrintWriter("ptcloud-rotated-" + formattedTime + ".txt", "UTF-8");
    }

    private BufferedReader getBufferedReader(String filePath) throws FileNotFoundException {
        return new BufferedReader(new FileReader(filePath));
    }

    /**
     * Given a ptCloud time, tries to find a matching IMU based on its timestamp. A matching IMU:
     * <p>
     * - Has to be within a time window ({@link SensorFusion#TIME_BOUNDARY}
     * - It's the closest IMU in time to the given ptCLoud.
     * <p>
     * During the real time reading, this algorithm will run the imu values until reaching a time higher than the ptCloud.
     * It will select the imu matching by comparing the time difference between immediate previous imu and immediate next imu of the ptCloud.
     *
     * @return a matching IMU, or null, if no match found.
     * If there is no match, the ptCloud should be discarded due to data loss.
     */
    private IMU findMatchingImuLine(BufferedReader imuReader, long ptCloudTime) throws IOException {
        for (String next, line = lastIMU == null ? imuReader.readLine() : lastIMU; line != null; line = next) {
            next = imuReader.readLine();
            IMU nextIMU = toIMU(next);
            IMU currentIMU = toIMU(line);

            if (nextIMU.getEpochTime() > ptCloudTime) {
                if (currentIMU.getEpochTime() < ptCloudTime - TIME_BOUNDARY && nextIMU.getEpochTime() > ptCloudTime + TIME_BOUNDARY) {
                    lastIMU = next;
                    return null;
                } else if (timeDiff(nextIMU.getEpochTime(), ptCloudTime) > timeDiff(currentIMU.getEpochTime(), ptCloudTime)) {
                    lastIMU = next;
                    return currentIMU;
                } else {
                    lastIMU = null;
                    return nextIMU;
                }
            }
        }
        return null;
    }

    /**
     * Rotates a given ptCloud using its corresponding imu
     *
     * @param imu     used to rotate
     * @param ptCloud to be rotated
     * @return a rotated {@link PtCloud} instance.
     */
    private PtCloud rotatePoint(IMU imu, PtCloud ptCloud) {
        Rotation rotQ = new Rotation(
                imu.getQuaternion().getQ0(),
                imu.getQuaternion().getQ1(),
                imu.getQuaternion().getQ2(),
                imu.getQuaternion().getQ3(),
                true
        );
        List<Vector3D> rotated = ptCloud.getVector3D()
                .stream()
                .map(rotQ::applyTo)
                .collect(Collectors.toList());

        return new PtCloud(rotated, ptCloud.getEpochTime());
    }

    private Long timeDiff(long time1, long time2) {
        return Math.abs(time2 - time1);
    }

}
