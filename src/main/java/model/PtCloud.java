package model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PtCloud {
    private List<Vector3D> vector3d;
    private long epochTime;

    public PtCloud(List<Vector3D> vector3d, long epochTime) {
        this.vector3d = vector3d;
        this.epochTime = epochTime;
    }

    public List<Vector3D> getVector3D() {
        return vector3d;
    }

    public long getEpochTime() {
        return epochTime;
    }

    @Override
    public String toString() {
        Date date = new Date(epochTime / 1000000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:ss:SS");
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        return "" + formattedDate;
    }

    public String toLine() {
        StringBuilder result = new StringBuilder();
        result.append(getEpochTime());
        result.append(" ");
        for (int i = 0; i < getVector3D().size(); i++) {
            Vector3D v = getVector3D().get(i);
            result.append(v.getX());
            result.append(" ");
            result.append(v.getY());
            result.append(" ");
            result.append(v.getZ());

        }
        return result.toString();
    }
}

