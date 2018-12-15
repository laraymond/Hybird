package model;

import org.apache.commons.math3.complex.Quaternion;
public class IMU {

    private Quaternion quaternion;
    private long epochTime;

    public IMU(Quaternion quaternion, long epochTime) {
        this.quaternion = quaternion;
        this.epochTime = epochTime;
    }

    @Override
    public String toString() {
        /*Date date = new Date(epochTime/1000000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:ss:SS");
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);*/
        return "" + epochTime + " " + quaternion.toString();
    }

}
